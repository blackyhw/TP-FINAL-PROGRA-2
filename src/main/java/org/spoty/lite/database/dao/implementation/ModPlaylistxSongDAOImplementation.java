package org.spoty.lite.database.dao.implementation;

import org.spoty.lite.database.DataBaseConnection;
import org.spoty.lite.database.dao.RelationDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModPlaylistxSongDAOImplementation implements RelationDAO {
    private Connection connection;

    public ModPlaylistxSongDAOImplementation() {
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
    public int addRelation(int modlist_id, int song_id) {
        String sql = "INSERT INTO modplaylistxsong (modlist_id, song_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, modlist_id);
            preparedStatement.setInt(2, song_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar la canción en la lista de reproducción.");
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
    public void deleteRelation(int modlist_id, int song_id) {
        String sql = "UPDATE modplaylistxsong SET status = 0 WHERE modlist_id = ? AND song_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, modlist_id);
            preparedStatement.setInt(2, song_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo eliminar la canción de la lista de reproducción.");
            }
            System.out.println("Canción eliminada de la lista de reproducción.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la canción: " + e.getMessage());
        }
    }

    @Override
    public List<Integer>getRelations(int modlist_id) {
        if (!doesPlaylistExist(modlist_id)) {
            throw new IllegalArgumentException("La playlist con ID " + modlist_id + " no existe.");
        }
        List<Integer> songsInModPlaylist = new ArrayList<>();
        String sql = "SELECT song_id FROM modplaylistxsong WHERE modlist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, modlist_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                songsInModPlaylist.add(resultSet.getInt("song_id"));
            }
            return songsInModPlaylist;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las canciones: " + e.getMessage());
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
