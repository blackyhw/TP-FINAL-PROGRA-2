package org.spoty.lite.database.dao.implementation;

import org.spoty.lite.database.DataBaseConnection;
import org.spoty.lite.database.dao.ModPlaylistDAO;
import org.spoty.lite.model.ModPlaylist;
import org.spoty.lite.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModPlaylistDAOImplementation implements ModPlaylistDAO {
    private Connection connection;
    ModPlaylistxSongDAOImplementation modPlaylistxSongDAOImplementation;

    public ModPlaylistDAOImplementation(Connection connection) {
        try {
            connection = DataBaseConnection.getConnection();
            modPlaylistxSongDAOImplementation = new ModPlaylistxSongDAOImplementation();
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createPlaylist(String title, int mod_id, String description) {
        String sql = "INSERT INTO modplaylist (title, mod_id, description) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, mod_id);
            preparedStatement.setString(3, description);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows <= 0) {
                throw new SQLException("No se pudo crear la lista de reproducción.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void addSongToPlaylist(int modlist_id, int song_id) {
        try {
            modPlaylistxSongDAOImplementation.addRelation(modlist_id, song_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al añadir la canción a la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void deleteSongFromPlaylist(int modlist_id, int song_id) {
        try {
            modPlaylistxSongDAOImplementation.deleteRelation(modlist_id, song_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la canción de la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void updatePlaylist(String title, String description) {
        String sql = "UPDATE modplaylist SET title = ?, description = ? WHERE modlist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo actualizar la lista de reproducción.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void enablePlaylist(int modlist_id) {
        String sql = "UPDATE modplaylist SET status = 1 WHERE modlist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, modlist_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo habilitar la lista de reproducción.");
            }
            System.out.println("Lista de reproducción habilitada.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al habilitar la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void disablePlaylist(int modlist_id) {
        String sql = "UPDATE modplaylist SET status = 0 WHERE modlist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, modlist_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo eliminar la lista de reproducción.");
            }
            System.out.println("Lista de reproducción eliminada.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public List<Song> getPlaylistById(int modlist_id) {
        List<Integer> songsIds = modPlaylistxSongDAOImplementation.getRelations(modlist_id);
        List<Song> songs = new ArrayList<>();
        for (int songId : songsIds) {
            Song song = new SongDAOImplementation().getSongById(songId);
            songs.add(song);
        }
        return songs;
    }

    @Override
    public List<ModPlaylist> getPlaylistsByMod(int mod_id) {
        List<ModPlaylist> modPlaylists = new ArrayList<>();
        String sql = "SELECT * FROM modplaylist WHERE mod_id = ? AND status = 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, mod_id);
            preparedStatement.executeQuery();
            while (preparedStatement.getResultSet().next()) {
                ModPlaylist modPlaylist = new ModPlaylist();
                modPlaylist.setModlist_id(preparedStatement.getResultSet().getInt("modlist_id"));
                modPlaylist.setMod_id(preparedStatement.getResultSet().getInt("mod_id"));
                modPlaylist.setTitle(preparedStatement.getResultSet().getString("title"));
                modPlaylist.setDescription(preparedStatement.getResultSet().getString("description"));
                modPlaylist.setStatus(preparedStatement.getResultSet().getInt("status"));
                modPlaylist.setCreation_date(preparedStatement.getResultSet().getTimestamp("creation_date"));
                modPlaylists.add(modPlaylist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las listas de reproducción del moderador: " + e.getMessage());
        }
        return modPlaylists;
    }

    public List<ModPlaylist> getDisabledPlaylistsByMod(int mod_id) {
        List<ModPlaylist> disabledModPlaylists = new ArrayList<>();
        String sql = "SELECT * FROM modplaylist WHERE mod_id = ? AND status = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, mod_id);
            preparedStatement.executeQuery();
            while (preparedStatement.getResultSet().next()) {
                ModPlaylist modPlaylist = new ModPlaylist();
                modPlaylist.setModlist_id(preparedStatement.getResultSet().getInt("modlist_id"));
                modPlaylist.setMod_id(preparedStatement.getResultSet().getInt("mod_id"));
                modPlaylist.setTitle(preparedStatement.getResultSet().getString("title"));
                modPlaylist.setDescription(preparedStatement.getResultSet().getString("description"));
                modPlaylist.setStatus(preparedStatement.getResultSet().getInt("status"));
                modPlaylist.setCreation_date(preparedStatement.getResultSet().getTimestamp("creation_date"));
                disabledModPlaylists.add(modPlaylist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las listas de reproducción del moderador: " + e.getMessage());
        }
        return disabledModPlaylists;
    }

    @Override
    public boolean doesPlaylistExist(int modlist_id) {
        String sql = "SELECT 1 FROM modplaylist WHERE modlist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, modlist_id);
            preparedStatement.executeQuery();
            return preparedStatement.getResultSet().next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la playlist: " + e.getMessage());
        }
    }
}

