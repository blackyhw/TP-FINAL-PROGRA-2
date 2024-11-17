package org.spoty.lite.database.dao.implementation;

import org.spoty.lite.database.DataBaseConnection;
import org.spoty.lite.database.dao.RelationDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikesxSongDAOImplementation implements RelationDAO {
    private Connection connection;

    public LikesxSongDAOImplementation() {
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

    @Override
    public int addRelation(int likes_id, int song_id) {
        String sql = "INSERT INTO likesxsong (user_id, song_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, likes_id);
            preparedStatement.setInt(2, song_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo agregar la canción a Likes.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado de la canción.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar la canción: " + e.getMessage());
        }
    }

    @Override
    public void deleteRelation(int likes_id, int song_id) {
        String sql = "UPDATE playlistxsong SET status = 0 WHERE likes_id = ? AND song_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, likes_id);
            preparedStatement.setInt(2, song_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo deslikear la canción.");
            }
            System.out.println("Canción eliminada de Likes.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la canción: " + e.getMessage());
        }
    }

    @Override
    public List<Integer> getRelations(int likes_id) {
        if (!doesPlaylistExist(likes_id)) {
            throw new IllegalArgumentException("La playlist con ID " + likes_id + " no existe.");
        }
        List<Integer> songsInLikes = new ArrayList<>();
        String sql = "SELECT song_id FROM likesxsong WHERE likes_id = ? AND status = 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, likes_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                songsInLikes.add(resultSet.getInt("song_id"));
            }
            return songsInLikes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las canciones de la lista de Likes: " + e.getMessage());
        }
    }

    public boolean doesPlaylistExist(int list_id) {
        String sql = "SELECT 1 FROM playlist WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, list_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la playlist. " + e.getMessage());
        }
    }
}
