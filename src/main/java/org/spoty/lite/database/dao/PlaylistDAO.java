package org.spoty.lite.database.dao;

import org.spoty.lite.model.Song;

import java.util.List;

public interface PlaylistDAO {
    void createPlaylist(String name, int user_id, String description);
    void addSongToPlaylist(int playlist_id, int song_id);
    void deleteSongFromPlaylist(int playlist_id, int song_id);
    void updatePlaylist(String title, String description);
    void disablePlaylist(int playlist_id);
    void enablePlaylist(int playlist_id);
    List<Song> getPlaylistById(int playlist_id);
    boolean doesPlaylistExist(int playlistId);
}
