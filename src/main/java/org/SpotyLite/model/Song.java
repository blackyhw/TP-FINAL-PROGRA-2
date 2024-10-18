package org.SpotyLite.model;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

public class Song {
    private String title;
    private String artist;
    private String filePath;
    private long duration;

    public Song(String title, String artist, String filePath) {
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.duration = calculateDuration(filePath);
    }

    private long calculateDuration(String filePath) {
        try {
            Mp3File mp3File = new Mp3File(filePath);
            return mp3File.getLengthInSeconds();
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getDuration() {
        return duration;
    }

}