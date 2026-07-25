package com.gumdojourney.gvip.youtube;

import java.nio.file.Path;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gumdojourney.gvip.model.Metadata;

public class YouTubeClientStub implements YouTubeClient {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeClientStub.class);

    @Override
    public String uploadVideo(Metadata metadata, Path videoFile) {
        LOG.info("Simulating upload of {} with metadata: {}", videoFile, metadata.getTitle());
        // return a fake video id
        return "stub-" + Instant.now().toEpochMilli();
    }
}
