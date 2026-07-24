package com.gumdojourney.gvip.shortcut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowsShortcutResolver {
    private static final Logger LOG = LoggerFactory.getLogger(WindowsShortcutResolver.class);
    private static final String POWERSHELL_EXE = "C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe";

    public Optional<Path> resolve(Path lnkFile) {
        if (lnkFile == null || !Files.exists(lnkFile)) return Optional.empty();
        try {
            String script = String.format("(New-Object -ComObject WScript.Shell).CreateShortcut('%s').TargetPath", lnkFile.toAbsolutePath().toString().replace("'","''"));
            ProcessBuilder pb = new ProcessBuilder(POWERSHELL_EXE, "-NoProfile", "-NonInteractive", "-Command", script);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = br.readLine();
                p.waitFor();
                if (line != null && !line.trim().isEmpty()) {
                    Path target = java.nio.file.Paths.get(line.trim());
                    if (Files.exists(target)) {
                        return Optional.of(target);
                    } else {
                        LOG.warn("Resolved shortcut target does not exist: {} -> {}", lnkFile, target);
                    }
                } else {
                    LOG.warn("No target returned resolving shortcut: {}", lnkFile);
                }
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("Error resolving shortcut {}: {}", lnkFile, e.getMessage());
            Thread.currentThread().interrupt();
        }
        return Optional.empty();
    }
}
