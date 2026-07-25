package com.gumdojourney.gvip.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfig {
    private final Properties props = new Properties();

    private AppConfig() {}

    public static AppConfig load() throws IOException {
        AppConfig cfg = new AppConfig();
        Path p = Paths.get("config/gvip.properties");
        if (Files.exists(p)) {
            try (InputStream is = Files.newInputStream(p)) {
                cfg.props.load(is);
                cfg.props.list(System.out);
            }
        }
        return cfg;
    }

    public String getVideoRootDirectory() {
        return props.getProperty("video.rootDirectory");
    }

    public String getStateFilePath() {
        return props.getProperty("state.file", "data/upload-state.json");
    }

    public String getYoutubeCredentialsFile() {
        return props.getProperty("youtube.credentialsFile");
    }

    public boolean isDryRun() {
        return Boolean.parseBoolean(props.getProperty("youtube.dryRun", "false"));
    }

    public String getOauthTokensDir() {
        return props.getProperty("oauth.tokensDir");
    }
}
