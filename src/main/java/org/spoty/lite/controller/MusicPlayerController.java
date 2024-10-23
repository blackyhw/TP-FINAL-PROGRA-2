package org.spoty.lite.controller;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.spoty.lite.model.Song;
import com.mpatric.mp3agic.*;

import java.io.FileInputStream;
import java.io.IOException;

public class MusicPlayerController {
    private AdvancedPlayer player;
    private Song song;
    private long startTime;
    private long pausedPosition;
    private boolean isPlaying;
    private Slider songSlider;
    private Label totalDurationLabel;

    public MusicPlayerController(Slider songSlider, Label totalDurationLabel) {
        this.songSlider = songSlider;
        this.totalDurationLabel = totalDurationLabel;
    }

    public void initialize(Song song) {
        this.song = song;
        try {
            Mp3File mp3file = new Mp3File(song.getFilePath());
            if (mp3file.hasId3v2Tag()) {
                song.setDuration(mp3file.getLengthInSeconds());
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (player == null) {
            try {
                FileInputStream fis = new FileInputStream(song.getFilePath());
                player = new AdvancedPlayer(fis);
                startTime = System.currentTimeMillis() - pausedPosition * 1000;
                isPlaying = true;
                new Thread(() -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (player != null) {
            pausedPosition = getCurrentPosition();
            player.close();
            player = null;
            isPlaying = false;
        }
    }

    public void stop() {
        if (player != null) {
            player.close();
            player = null;
            pausedPosition = 0;
            isPlaying = false;
        }
    }

    public void seek(long position) {
        if (player != null) {
            stop();
            pausedPosition = position;
            play();
        }
    }

    public long getSongDuration() {
        return song.getDuration();
    }

    public long getCurrentPosition() {
        if (player != null) {
            return (System.currentTimeMillis() - startTime) / 1000;
        }
        return pausedPosition;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setVolume(double v) {
    }

    public void previousTrack() {
    }

    public void nextTrack() {

    }

    public void setPosition(double position) {
    }

    public double getPosition() {
        return 0;
    }

    public double getTotalDuration() {
        return 0;
    }

    public void loadSong(double songDuration) {
        songSlider.setMax(songDuration);
        totalDurationLabel.setText(formatTime(songDuration));
    }

    public void onSongLoaded(double songDuration) {
        loadSong(songDuration);
    }

    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int secs = (int) seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
}