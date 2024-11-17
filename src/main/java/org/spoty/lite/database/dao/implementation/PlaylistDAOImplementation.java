package org.spoty.lite.database.dao.implementation;

import org.spoty.lite.database.DataBaseConnection;
import org.spoty.lite.model.PlaylistxSong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlaylistDAOImplementation {
    private Connection connection;

    public PlaylistDAOImplementation() {
        try {
            connection = DataBaseConnection.getConnection();
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

    public int createPlaylist(String title, int user_id, String description) {
        String sql = "INSERT INTO playlist (title, user_id, description) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setString(3, description);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo crear la lista playlist.");
            }
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la lista de reproducción: " + e.getMessage());
        }
    }

    public void addSongToPlaylist(int playlist_id, int song_id) {


    }


}
