package org.spoty.lite.database.dao;

import org.spoty.lite.model.ModPlaylist;

import java.util.List;

public interface ModPlaylistDAO extends PlaylistDAO {
    List<ModPlaylist> getPlaylistsByMod(int mod_id);
    List<ModPlaylist> getDisabledPlaylistsByMod(int mod_id);
}
