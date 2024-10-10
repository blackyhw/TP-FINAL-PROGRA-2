import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import model.Song;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MusicPlayer extends PlaybackListener {
    private static final Object playSignal = new Object();

    private MusicPlayerGUI musicPlayerGUI;
    private Song currentSong;
    private AdvancedPlayer advancedPlayer;
    private ArrayList<Song> playlist;
    private boolean isPaused;
    private boolean songFinished;
    private boolean pressedNext,pressedPrev;
    private  int currentFrame;
    private int currentTimeInMilli;
    private int currentPlaylistIndex;

    public MusicPlayer(MusicPlayerGUI musicPlayerGUI) {
        this.musicPlayerGUI = musicPlayerGUI;
    }

    public void setCurrentFrame(int frame){
        currentFrame = frame;
    }

    public void setCurrentTimeInMilli(int timeInMilli){
        this.currentTimeInMilli = timeInMilli;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void loadPlaylist(File playlistFile){
        playlist = new ArrayList<>();

        try{
            FileReader fileReader = new FileReader(playlistFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String songPath;
            while((songPath = bufferedReader.readLine()) != null){
                Song song = new Song(songPath);

                playlist.add(song);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if(playlist.size() > 0){
            musicPlayerGUI.setPlaybackSliderValue(0);
            currentTimeInMilli = 0;

            currentSong = playlist.get(0);


            currentFrame = 0;

            musicPlayerGUI.enablePauseButtonDisablePlayButton();
            musicPlayerGUI.updateSongTitleAndArtist(currentSong);
            musicPlayerGUI.updatePlaybackSlider(currentSong);


            playCurrentSong();
        }
    }

    public void loadSong(Song song) {
        this.currentSong = song;

        playlist = null;

        if (!songFinished) {
            stopSong();
        }
        if (this.currentSong != null) {
            currentFrame = 0;

            currentTimeInMilli = 0;

            musicPlayerGUI.setPlaybackSliderValue(0);
            
            playCurrentSong();
        }
    }

    public void pauseSong() {
        if (advancedPlayer != null) {
            this.isPaused = true;

            stopSong();
        }
    }


    public void stopSong() {
        if (advancedPlayer != null) {
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }


    public void prevSong(){
        if(playlist == null) return;

        if (currentPlaylistIndex - 1 < 0 )return;

        pressedPrev = true;

        if (!songFinished) {
            stopSong();
        }

        currentPlaylistIndex--;

        currentSong = playlist.get(currentPlaylistIndex);

        currentFrame = 0;

        currentTimeInMilli = 0;

        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);


        playCurrentSong();
    }



    public void nextSong(){
        if(playlist == null) return;

        if(currentPlaylistIndex +1 > playlist.size()-1)return;

        pressedNext = true;

        if (!songFinished) {
            stopSong();
        }
        currentPlaylistIndex++;

        currentSong = playlist.get(currentPlaylistIndex);

        currentFrame = 0;

        currentTimeInMilli = 0;

        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);


        playCurrentSong();
    }


    public void playCurrentSong() {
        if(currentSong == null)return;
        try {
            FileInputStream fileImputStream = new FileInputStream(currentSong.getFilepath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileImputStream);

            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);


            startMusicThread();

            starPlayBackSliderThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMusicThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isPaused){
                        synchronized (playSignal){
                            isPaused = false;

                            playSignal.notify();
                        }
                        advancedPlayer.play(currentFrame,Integer.MAX_VALUE);
                    }else {
                        advancedPlayer.play();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void starPlayBackSliderThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isPaused)
                    try{
                        synchronized (playSignal){
                            playSignal.wait();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                while (!isPaused && !songFinished && !pressedNext && !pressedPrev){
                   try{
                       currentTimeInMilli++;

                       int calculatedFrame = (int) ((double)currentTimeInMilli * 2.08 * currentSong.getFrameRatePerMilliseconds());

                       musicPlayerGUI.setPlaybackSliderValue(calculatedFrame);

                       Thread.sleep(1);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        System.out.println("Comienza la lista de reproduccion");
        songFinished = false;
        pressedNext = false;
        pressedPrev = false;
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        System.out.println("Lista de reproduccion terminada");

        if(isPaused){
            currentFrame += (int) ((double) evt.getFrame() * currentSong.getFrameRatePerMilliseconds());
        }else {
            if(pressedNext || pressedPrev) return;
            songFinished = true;

            if (playlist == null){
                musicPlayerGUI.enablePlayButtonDisablePauseButton();
            }else {
                if (currentPlaylistIndex == playlist.size() - 1){
                    musicPlayerGUI.enablePlayButtonDisablePauseButton();
                }else {
                    nextSong();
                }
            }
        }
    }
}
