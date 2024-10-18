package org.SpotyLite.controller;

import org.SpotyLite.gui.MusicPlayerGUI;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.SpotyLite.model.Song;

import java.io.FileInputStream;
import java.io.IOException;

public class MusicPlayerController {
    private AdvancedPlayer player;
    private Thread playThread;
    private Song song;
    private long startTime;
    private long pausedPosition;
    private boolean isPlaying;

    public void initialize(Song song) {
        this.song = song;
    }

    public void play() {
        if (player == null) {
            playThread = new Thread(() -> {
                try (FileInputStream fis = new FileInputStream(song.getFilePath())) {
                    player = new AdvancedPlayer(fis);
                    startTime = System.currentTimeMillis() - pausedPosition * 1000;
                    player.setPlayBackListener(new PlaybackListener() {
                        @Override
                        public void playbackFinished(PlaybackEvent evt) {
                            player = null;
                            MusicPlayerGUI.getInstance().stopSliderUpdate();
                        }
                    });
                    isPlaying = true;
                    MusicPlayerGUI.getInstance().startSliderUpdate();
                    player.play((int) pausedPosition, Integer.MAX_VALUE);
                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                }
            });
            playThread.setDaemon(true);
            playThread.start();
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
}