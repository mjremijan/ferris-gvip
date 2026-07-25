package com.gumdojourney.gvip.youtube;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.gumdojourney.gvip.config.AppConfig;
import com.gumdojourney.gvip.model.Metadata;

public class YouTubeClientImpl implements YouTubeClient {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeClientImpl.class);
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES 
        = Arrays.asList(
              "https://www.googleapis.com/auth/youtube.upload"
            , "https://www.googleapis.com/auth/youtube"
        );

    private final NetHttpTransport transport;
    private final HttpRequestFactory requestFactory;

    public YouTubeClientImpl(AppConfig cfg) throws IOException, GeneralSecurityException {
        this.transport = GoogleNetHttpTransport.newTrustedTransport();
        Credential cred = authorize(cfg);
        this.requestFactory = transport.createRequestFactory(cred);
    }

    private Credential authorize(AppConfig cfg) throws IOException {
        String clientSecretsPath = cfg.getYoutubeCredentialsFile();
        if (clientSecretsPath == null) throw new IOException("youtube.credentialsFile not configured");
        GoogleClientSecrets clientSecrets;
        try (FileInputStream fis = new FileInputStream(clientSecretsPath)) {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(fis));
        }

        FileDataStoreFactory dataStoreFactory =
            new FileDataStoreFactory(new File(cfg.getOauthTokensDir()));

        GoogleAuthorizationCodeFlow flow;
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    transport, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(dataStoreFactory)
                    .setAccessType("offline")
                    .build();
        } catch (Exception e) {
            throw new IOException("Failed to build auth flow", e);
        }

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Override
    public String uploadVideo(Metadata metadata, Path videoFile) throws IOException {
        // prepare metadata map
        Map<String, Object> meta = new HashMap<>();
        Map<String, Object> snippet = new HashMap<>();
        snippet.put("title", metadata.getTitle());
        snippet.put("description", metadata.getDescription());
        snippet.put("categoryId", "17"); // Sports category
        snippet.put("defaultLanguage", "en-US"); // English (United States)
        if (metadata.getTags() != null) snippet.put("tags", metadata.getTags());
        meta.put("snippet", snippet);

        Map<String, Object> status = new HashMap<>();
        status.put("privacyStatus", "public");
        status.put("selfDeclaredMadeForKids", metadata.isMadeForKids());
        meta.put("status", status);

        if (metadata.getRecordingDate() != null) {
            Map<String, Object> rec = new HashMap<>();
            rec.put("recordingDate", metadata.getRecordingDate().toString());
            meta.put("recordingDetails", rec);
        }

        // initiate resumable upload
        GenericUrl initUrl = new GenericUrl("https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status,recordingDetails");
        HttpContent initContent = new JsonHttpContent(JSON_FACTORY, meta);
        HttpRequest initReq = requestFactory.buildPostRequest(initUrl, initContent);
        initReq.getHeaders().set("X-Upload-Content-Type", "video/mp4");
        HttpResponse initResp = initReq.execute();
        String uploadUrl = initResp.getHeaders().getLocation();
        if (uploadUrl == null) throw new IOException("Resumable upload init failed: no Location header");

        // upload file data in one PUT request (suitable for small files)
        GenericUrl uploadGeneric = new GenericUrl(uploadUrl);
        InputStreamContent mediaContent = new InputStreamContent("video/mp4", java.nio.file.Files.newInputStream(videoFile));
        mediaContent.setLength(java.nio.file.Files.size(videoFile));
        HttpRequest uploadReq = requestFactory.buildPutRequest(uploadGeneric, mediaContent);
        HttpResponse uploadResp = uploadReq.execute();
        Map<String, Object> respMap = (Map<String, Object>) JSON_FACTORY.fromInputStream(uploadResp.getContent(), Map.class);
        Object id = respMap.get("id");
        String videoId = id != null ? id.toString() : null;
        LOG.info("YouTube upload completed: id={}", videoId);

        // assign to playlists if they exist
        if (metadata.getPlaylists() != null && !metadata.getPlaylists().isEmpty() && videoId != null) {
            for (String pname : metadata.getPlaylists()) {
                try {
                    String pid = findPlaylistId(pname);
                    if (pid != null) {
                        Map<String, Object> item = new HashMap<>();
                        Map<String, Object> sn = new HashMap<>();
                        sn.put("playlistId", pid);
                        Map<String, Object> rid = new HashMap<>();
                        rid.put("kind", "youtube#video");
                        rid.put("videoId", videoId);
                        sn.put("resourceId", rid);
                        item.put("snippet", sn);
                        GenericUrl playlistItemsUrl = new GenericUrl("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet");
                        HttpRequest plReq = requestFactory.buildPostRequest(playlistItemsUrl, new JsonHttpContent(JSON_FACTORY, item));
                        plReq.execute();
                    } else {
                        LOG.warn("Playlist '{}' not found for user; skipping assignment.", pname);
                    }
                } catch (IOException e) {
                    LOG.warn("Failed to assign playlist '{}' for video {}", pname, videoId);
                    e.printStackTrace();
                }
            }
        }

        return videoId;
    }

    private String findPlaylistId(String title) throws IOException {
        String pageToken = null;
        do {
            GenericUrl url = new GenericUrl("https://www.googleapis.com/youtube/v3/playlists");
            url.put("part", "snippet");
            url.put("mine", "true");
            url.put("maxResults", 50);
            if (pageToken != null) url.put("pageToken", pageToken);
            HttpRequest req = requestFactory.buildGetRequest(url);
            HttpResponse resp = req.execute();
            Map<String, Object> map = (Map<String, Object>) JSON_FACTORY.fromInputStream(resp.getContent(), Map.class);
            List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("items");
            if (items != null) {
                for (Map<String, Object> it : items) {
                    Map<String, Object> sn = (Map<String, Object>) it.get("snippet");
                    if (sn != null) {
                        Object t = sn.get("title");
                        if (t != null && t.toString().equalsIgnoreCase(title)) {
                            return it.get("id").toString();
                        }
                    }
                }
            }
            pageToken = map.get("nextPageToken") != null ? map.get("nextPageToken").toString() : null;
        } while (pageToken != null);
        return null;
    }
}
