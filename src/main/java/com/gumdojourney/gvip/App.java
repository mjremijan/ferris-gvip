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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gumdojourney.gvip.config.AppConfig;
import com.gumdojourney.gvip.ignore.IgnoreStore;
import com.gumdojourney.gvip.model.Metadata;
import com.gumdojourney.gvip.parser.ParserManager;
import com.gumdojourney.gvip.shortcut.WindowsShortcutResolver;
import com.gumdojourney.gvip.state.StateStore;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    //private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        AppConfig config = AppConfig.load();
        String root = config.getVideoRootDirectory();
        if (root == null || root.trim().isEmpty()) {
            LOG.error("Error: video.rootDirectory property is not set in config/gvip.properties");
            System.exit(1);
        }

        LOG.info("video.rootDirectory property = {}", config.getVideoRootDirectory());
        LOG.info("Root directory configured as: {}", root);
        LOG.info("Starting GVIP scan in {}", root);

        ParserManager pm = new ParserManager();
        StateStore state = new StateStore(config.getStateFilePath(), config.isDryRun());
        IgnoreStore ignore = new IgnoreStore(config.getIgnoreFilePath());
        WindowsShortcutResolver resolver = new WindowsShortcutResolver();

        List<Path> files = discoverMp4Files(Paths.get(root), resolver);
        
        
        LOG.info("Discovered {} video file(s) under {}", files.size(), root);
        for (Path p : files) {
            LOG.info("----------------------------------------");            
            LOG.info("FOUND_VIDEO: {}", p);
            processFile(p, pm, ignore, state, config);
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

    private static void processFile(Path p, ParserManager pm, IgnoreStore ignore, StateStore state, AppConfig config) {
        String filename = p.getFileName().toString();
        try {
            if (ignore.isIgnored(p)) {
                LOG.info(" | IGNORING");
                return;
            }

            if (state.isUploaded(p)) {
                LOG.info(" | IS_UPLOADED");
                return;
            }

            Metadata md = pm.parseFilename(p).orElse(null);
            {
                if (md == null) {
                    LOG.info(" | SKIPPING - parsing file name failed");
                    return;
                }

                LOG.info(" | TO_UPLOAD");  
                LOG.info(" | - filename='{}'", filename); 
                LOG.info(" | - title='{}'", md.getTitle()); 
                LOG.info(" | - description='{}'", md.getDescription()); 
                LOG.info(" | - recordingDate='{}'", md.getRecordingDate()); 
                LOG.info(" | - playlists='{}'", md.getPlaylists()); 
                LOG.info(" | - tags='{}'", md.getTags()); 
                LOG.info(" | - madeForKids='{}'", md.isMadeForKids()); 
                LOG.info(" | - sourceFilename='{}'", md.getSourceFilename()); 
            }

            if (config.isDryRun()) {                
                return;
            }

            // System.out.printf("%s\n", md.getSourceFilename());
            // System.out.print("-> Upload to YouTube (y/n/exit)? ");
            // String userInput = scanner.nextLine().trim().toLowerCase();
            // if (userInput.equals("exit")) {
            //     System.exit(0);
            // }
            // if (userInput.equals("y") || userInput.equals("yes")) {
            //     LOG.info("Working...."); 
            // } else {
            //     LOG.info("Skipping upload for {}", filename);
            //     return;
            // }

            // create YouTube client and upload (real client if configured, otherwise stub)
            com.gumdojourney.gvip.youtube.YouTubeClient yt = com.gumdojourney.gvip.youtube.YouTubeClientFactory.create(config);
            String videoId = yt.uploadVideo(md, p);
            LOG.info("Uploaded {} -> videoId={}", filename, videoId);

            state.markUploaded(p, md);
            LOG.info("Marked as uploaded: {}", filename);
            
            randomSleep();
        } catch (Exception e) {
            LOG.error("Upload error for {}: {}", filename, e.getMessage(), e);
            throw new RuntimeException(e);
        } 
    }

    private static void randomSleep() {
        // Random number of minutes: 4, 5, 6, 7, or 8
        long minutes = ThreadLocalRandom.current().nextLong(4, 9);

        LOG.info("Sleeping for " + minutes + " minute(s)...");
        try {
            TimeUnit.MINUTES.sleep(minutes);
        } catch (InterruptedException e) {
            throw new RuntimeException("Sleep interrupted", e);
        }
    }
   
}
