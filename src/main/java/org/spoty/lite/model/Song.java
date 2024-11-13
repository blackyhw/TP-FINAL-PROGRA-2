package org.spoty.lite.model;

public class Song {
    private int song_id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private long duration;
    private String filePath;
    private int status;

    public Song(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public Song setTitle(String title) {
        this.title = title;
        return this;
    }


}