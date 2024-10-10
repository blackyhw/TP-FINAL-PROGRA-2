package model;

import com.mpatric.mp3agic.Mp3File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;


import java.io.File;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String filepath;
    private String songLength;
    private Mp3File mp3File;
    private double frameRatePerMilliseconds;

    public Song(String filepath) {
        this.filepath = filepath;
        try{
            mp3File = new Mp3File(filepath);
            frameRatePerMilliseconds = (double) mp3File.getFrameCount() / mp3File.getLengthInMilliseconds();
            songLength = convertToSongLenghtFormat();


            AudioFile audioFile = AudioFileIO.read(new File(filepath));
            Tag tag = audioFile.getTag();
            if(tag!= null){
                this.title = tag.getFirst(FieldKey.KEY.TITLE);
                this.artist = tag.getFirst(FieldKey.KEY.ARTIST);
            }else{
                this.title = "N/A";
                this.artist = "N/A";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String convertToSongLenghtFormat(){
        long minutes = mp3File.getLengthInSeconds() / 60;
        long seconds = mp3File.getLengthInSeconds() % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);

        return formattedTime;
    }


    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getSongLength() {
        return songLength;
    }

   public Mp3File getMp3File(){return mp3File;}

   public double getFrameRatePerMilliseconds(){return frameRatePerMilliseconds;}
}