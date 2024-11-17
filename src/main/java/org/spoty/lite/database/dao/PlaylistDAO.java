package org.spoty.lite.database.dao;

public interface PlaylistDAO {
    int createPlaylist(String name, int user_id);
    void addSongToPlaylist(int playlist_id, int song_id);
    void deleteSongFromPlaylist(int playlist_id, int song_id);
    void deletePlaylist(int playlist_id);
    void getPlaylistById(int playlist_id);
    void getPlaylistsByUser(int user_id);
    void getPlaylistSongs(int playlist_id);
}
