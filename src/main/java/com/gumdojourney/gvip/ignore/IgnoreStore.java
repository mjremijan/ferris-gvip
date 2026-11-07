package com.gumdojourney.gvip.ignore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class IgnoreStore {
    private final Path file;
    private final List<String> regexs;

    public IgnoreStore(String path) throws IOException {
        this.file = Paths.get(path);
        if (Files.exists(this.file)) {
            regexs = Files.readAllLines(this.file);
        } else {
            throw new RuntimeException("Ignore file does not exist: " + this.file.toAbsolutePath().toString());
        }
    }

    public boolean isIgnored(Path p) {
        final String filename = p.getFileName().toString();

        for (String regex : regexs) {
            if (filename.matches(regex)) {
                return true;
            }
        }
        return false;
    }
}
