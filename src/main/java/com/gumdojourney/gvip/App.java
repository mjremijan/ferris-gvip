package com.gumdojourney.gvip;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gumdojourney.gvip.config.AppConfig;
import com.gumdojourney.gvip.model.Metadata;
import com.gumdojourney.gvip.parser.ParserManager;
import com.gumdojourney.gvip.shortcut.WindowsShortcutResolver;
import com.gumdojourney.gvip.state.StateStore;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("d", "dir", true, "Root directory to scan (overrides properties)");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        AppConfig config = AppConfig.load();
        String root = cmd.hasOption("dir") ? cmd.getOptionValue("dir") : config.getVideoRootDirectory();
        if (root == null || root.trim().isEmpty()) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("gvip", options);
            System.exit(1);
        }

        LOG.info("video.rootDirectory property = {}", config.getVideoRootDirectory());
        LOG.info("Root directory configured as: {}", root);
        LOG.info("Starting GVIP scan in {}", root);

        ParserManager pm = new ParserManager();
        StateStore state = new StateStore(config.getStateFilePath(), config.isDryRun());
        WindowsShortcutResolver resolver = new WindowsShortcutResolver();

        List<Path> files = discoverMp4Files(Paths.get(root), resolver);
        LOG.info("Discovered {} video file(s) under {}", files.size(), root);
        for (Path p : files) {
            System.out.println("FOUND_VIDEO=" + p);
            processFile(p, pm, state, config);
        }
        LOG.info("GVIP run complete");
    }

    private static List<Path> discoverMp4Files(Path root, WindowsShortcutResolver resolver) {
        List<Path> result = new ArrayList<>();
        Deque<Path> stack = new ArrayDeque<>();
        Set<Path> visited = new HashSet<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Path dir = stack.pop();
            try {
                Path abs = dir.toAbsolutePath().normalize();
                if (visited.contains(abs)) continue;
                visited.add(abs);
                if (!Files.isDirectory(abs)) continue;

                try (Stream<Path> entries = Files.list(abs)) {
                    entries.forEach(p -> {
                        try {
                            if (Files.isDirectory(p)) {
                                stack.push(p);
                            } else if (p.getFileName().toString().toLowerCase().endsWith(".mp4")) {
                                result.add(p);
                            } else if (p.getFileName().toString().toLowerCase().endsWith(".lnk")) {
                                Optional<Path> target = resolver.resolve(p);
                                target.ifPresent(stack::push);
                            }
                        } catch (Exception e) {
                            LOG.warn("Error inspecting {}: {}", p, e.getMessage());
                        }
                    });
                }
            } catch (IOException e) {
                LOG.warn("Error traversing {}: {}", dir, e.getMessage());
            }
        }
        return result;
    }

    private static void processFile(Path p, ParserManager pm, StateStore state, AppConfig config) {
        String filename = p.getFileName().toString();
        try {
            if (state.isUploaded(p)) {
                LOG.info("Skipping already uploaded: {}", filename);
                return;
            }

            Metadata md = pm.parseFilename(p).orElse(null);
            if (md == null) {
                //LOG.warn("Unsupported filename format: {}", filename);
                return;
            }

            logMetadataSummary(filename, md);

            if (config.isDryRun()) {
                //System.out.println("DRY_RUN_SKIP=file='" + filename + "'");
                return;
            }

            // create YouTube client and upload (real client if configured, otherwise stub)
            com.gumdojourney.gvip.youtube.YouTubeClient yt = com.gumdojourney.gvip.youtube.YouTubeClientFactory.create(config);
            String videoId = yt.uploadVideo(md, p);
            LOG.info("Uploaded {} -> videoId={}", filename, videoId);

            state.markUploaded(p, md);
            LOG.info("Marked as uploaded: {}", filename);
        } catch (IOException e) {
            LOG.error("Error processing file {}: {}", filename, e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("Upload error for {}: {}", filename, e.getMessage(), e);
        }
    }

    private static void logMetadataSummary(String filename, Metadata md) {
        System.out.println(
                "VIDEO_METADATA\n |filename='" + filename + "'\n |title='" + md.getTitle() + "'\n |description='" + md.getDescription() + "'\n |recordingDate=" + md.getRecordingDate() + "\n |playlists=" + md.getPlaylists() + "\n |tags=" + md.getTags() + "\n |madeForKids=" + md.isMadeForKids() + "\n |sourceFilename='" + md.getSourceFilename() + "'"
        );
    }
}
