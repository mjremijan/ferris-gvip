package com.gumdojourney.gvip.model;

import java.time.LocalDate;
import java.util.List;

public class Metadata {
    private String title;
    private String description;
    private LocalDate recordingDate;
    private List<String> playlists;
    private List<String> tags;
    private boolean madeForKids = false;
    private String sourceFilename;

    public Metadata() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getRecordingDate() { return recordingDate; }
    public void setRecordingDate(LocalDate recordingDate) { this.recordingDate = recordingDate; }
    public List<String> getPlaylists() { return playlists; }
    public void setPlaylists(List<String> playlists) { this.playlists = playlists; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public boolean isMadeForKids() { return madeForKids; }
    public void setMadeForKids(boolean madeForKids) { this.madeForKids = madeForKids; }
    public String getSourceFilename() { return sourceFilename; }
    public void setSourceFilename(String sourceFilename) { this.sourceFilename = sourceFilename; }

    @Override
    public String toString() {
        return "Metadata{" +
                "title='" + title + '\'' +
                ", recordingDate=" + recordingDate +
                ", playlists=" + playlists +
                ", tags=" + tags +
                ", madeForKids=" + madeForKids +
                ", sourceFilename='" + sourceFilename + '\'' +
                '}';
    }
}
