package com.gumdojourney.gvip.state;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gumdojourney.gvip.model.Metadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StateStore {
    private final Path file;
    private final ObjectMapper mapper = new ObjectMapper();
    private final boolean dryRun;
    private Map<String, Object> data = new HashMap<>();

    public StateStore(String path) throws IOException {
        this(path, false);
    }

    public StateStore(String path, boolean dryRun) throws IOException {
        this.file = Paths.get(path);
        this.dryRun = dryRun;
        if (Files.exists(this.file)) {
            data = mapper.readValue(Files.newBufferedReader(this.file), new TypeReference<Map<String,Object>>(){});
        } else if (!dryRun) {
            Files.createDirectories(this.file.getParent());
            save();
        }
    }

    public boolean isUploaded(Path p) {
        return data.containsKey(p.toAbsolutePath().toString());
    }

    public void markUploaded(Path p, Metadata md) throws IOException {
        if (dryRun) {
            return;
        }
        Map<String,Object> entry = new HashMap<>();
        entry.put("title", md.getTitle());
        entry.put("recordingDate", md.getRecordingDate() != null ? md.getRecordingDate().toString() : null);
        entry.put("sourceFilename", md.getSourceFilename());
        data.put(p.toAbsolutePath().toString(), entry);
        save();
    }

    private void save() throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(Files.newBufferedWriter(this.file), data);
    }
}
