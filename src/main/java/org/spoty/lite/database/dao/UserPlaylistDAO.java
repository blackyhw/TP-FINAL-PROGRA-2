package org.spoty.lite.database.dao;

import org.spoty.lite.model.UserPlaylist;

import java.util.List;

public interface UserPlaylistDAO extends PlaylistDAO {
    List<UserPlaylist> getPlaylistsByUser(int user_id);
    List<UserPlaylist> getDisabledPlaylistsByUser(int user_id);
}
