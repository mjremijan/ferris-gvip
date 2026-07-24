package com.gumdojourney.gvip.youtube;

import com.gumdojourney.gvip.config.AppConfig;

import java.io.IOException;

public class YouTubeClientFactory {
    public static YouTubeClient create(AppConfig cfg) throws IOException {
        // If credentials file exists, create real client; otherwise fallback to stub.
        String creds = cfg.getYoutubeCredentialsFile();
        if (creds != null && !creds.trim().isEmpty()) {
            try {
                return new YouTubeClientImpl(cfg);
            } catch (Exception e) {
                // fall back to stub if real client cannot be constructed
                return new YouTubeClientStub();
            }
        }
        return new YouTubeClientStub();
    }
}
