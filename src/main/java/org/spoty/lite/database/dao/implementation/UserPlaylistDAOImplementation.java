package org.spoty.lite.database.dao.implementation;

import org.spoty.lite.database.DataBaseConnection;
import org.spoty.lite.database.dao.UserPlaylistDAO;
import org.spoty.lite.model.UserPlaylist;
import org.spoty.lite.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPlaylistDAOImplementation implements UserPlaylistDAO {
    private Connection connection;
    private UserPlaylistxSongDAOImplementation userPlaylistxSongDAOImplementation;

    public UserPlaylistDAOImplementation() {
        try {
            connection = DataBaseConnection.getConnection();
            userPlaylistxSongDAOImplementation = new UserPlaylistxSongDAOImplementation();
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
    public void createPlaylist(String title, int user_id, String description) {
        String sql = "INSERT INTO userplaylist (title, user_id, description) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, user_id);
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
    public void addSongToPlaylist(int userPlaylist_id, int song_id) {
        try {
            userPlaylistxSongDAOImplementation.addRelation(userPlaylist_id, song_id);
            System.out.println("Canción añadida a la lista de reproducción.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al añadir la canción a la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void deleteSongFromPlaylist(int userPlaylist_id, int song_id) {
        try {
            userPlaylistxSongDAOImplementation.deleteRelation(userPlaylist_id, song_id);
            System.out.println("Canción eliminada de la lista de reproducción.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la canción de la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void updatePlaylist(String title, String description) {
        String sql = "UPDATE userplaylist SET title = ?, description = ? WHERE userplaylist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo actualizar la lista de reproducción.");
            }
            System.out.println("Lista de reproducción actualizada.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la lista de reproducción: " + e.getMessage());
        }
    }

    @Override
    public void enablePlaylist(int userPlaylist_id) {
        String sql = "UPDATE userplaylist SET status = 1 WHERE userplaylist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userPlaylist_id);
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
    public void disablePlaylist(int userPlaylist_id) {
        String sql = "UPDATE userplaylist SET status = 0 WHERE userplaylist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userPlaylist_id);
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
    public List<Song> getPlaylistById(int userPlaylist_id) {
        List<Integer> songIds = userPlaylistxSongDAOImplementation.getRelations(userPlaylist_id);
        List<Song> songs = new ArrayList<>();
        for (int songId : songIds) {
            Song song = new SongDAOImplementation().getSongById(songId);
            songs.add(song);
        }
        return songs;
    }

    @Override
    public List<UserPlaylist> getPlaylistsByUser(int user_id) {
        List<UserPlaylist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM userplaylist WHERE user_id = ? AND status = 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserPlaylist playlist = new UserPlaylist();
                playlist.setUserplaylist_id(resultSet.getInt("userplaylist_id"));
                playlist.setUser_id(resultSet.getInt("user_id"));
                playlist.setTitle(resultSet.getString("title"));
                playlist.setDescription(resultSet.getString("description"));
                playlist.setStatus(resultSet.getInt("status"));
                playlist.setCreation_date(resultSet.getTimestamp("creation_date"));
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las listas de reproducción del usuario: " + e.getMessage());
        }
        return playlists;
    }

    @Override
    public List<UserPlaylist> getDisabledPlaylistsByUser(int user_id) {
        List<UserPlaylist> disabledPlaylists = new ArrayList<>();
        String sql = "SELECT * FROM userplaylist WHERE user_id = ? AND status = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserPlaylist playlist = new UserPlaylist();
                playlist.setUserplaylist_id(resultSet.getInt("userplaylist_id"));
                playlist.setUser_id(resultSet.getInt("user_id"));
                playlist.setTitle(resultSet.getString("title"));
                playlist.setDescription(resultSet.getString("description"));
                playlist.setStatus(resultSet.getInt("status"));
                playlist.setCreation_date(resultSet.getTimestamp("creation_date"));
                disabledPlaylists.add(playlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las listas de reproducción del usuario: " + e.getMessage());
        }
        return disabledPlaylists;
    }

    @Override
    public boolean doesPlaylistExist(int userPlaylist_id) {
        String sql = "SELECT 1 FROM userplaylist WHERE userplaylist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userPlaylist_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la playlist: " + e.getMessage());
        }
    }
}
