package org.spoty.lite.database.dao;

import org.spoty.lite.model.Song;

import java.util.List;

public interface SongDAO {
    Song getSongById(int song_id);
    void songStatus(int song_id, int status);
    List<Song> getAllSongs();
    List<Song> getSongsByGenre(String genre);
    List<Song> getSongsByArtist(String artist);
    List<Song> getSongsByAlbum(String album);
    List<Song> getSongsByTitle(String title);
    List<Song> getSongsByStatus(int status);
}
