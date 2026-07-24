package com.gumdojourney.gvip.youtube;

import com.gumdojourney.gvip.model.Metadata;

import java.nio.file.Path;

public interface YouTubeClient {
    /**
     * Uploads a video and returns the created YouTube video ID.
     * Implementations should perform authentication and API calls.
     */
    String uploadVideo(Metadata metadata, Path videoFile) throws Exception;
}
