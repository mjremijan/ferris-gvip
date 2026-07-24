package com.gumdojourney.gvip.model;

import java.time.LocalDate;
import java.util.List;

public class Metadata {
    private String title;
    private String description;
    private LocalDate recordingDate;
    private List<String> playlists;
    private final List<String> tags;
    private boolean madeForKids = false;
    private String sourceFilename;

    public Metadata() {
        tags = List.of(
            "haidong gumdo",
            "해동검도",
            "korean",
            "sword",
            "검",
            "ssangsu gumbup",
            "쌍수검법",
            "gyuk gum",
            "격검",
            "paper cutting",
            "jong-i-pegi",
            "종이 베기",
            "apple cutting",
            "sagwa-pegi",
            "사과베기",
            "candle snuffing",
            "chotbul-kkeugi",
            "촛불끄기"
        );
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        if (this.description.endsWith(".mp4")) {
            this.description = this.description.substring(0, this.description.length() - 4);
        }
    }
    public LocalDate getRecordingDate() { return recordingDate; }
    public void setRecordingDate(LocalDate recordingDate) { this.recordingDate = recordingDate; }
    public List<String> getPlaylists() { return playlists; }
    public void setPlaylists(List<String> playlists) { this.playlists = playlists; }
    public List<String> getTags() { return tags; }
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
