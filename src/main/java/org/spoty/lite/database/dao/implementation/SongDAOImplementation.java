package org.spoty.lite.database.dao.implementation;
import org.spoty.lite.database.DataBaseConnection;
import org.spoty.lite.database.dao.SongDAO;
import org.spoty.lite.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDAOImplementation implements SongDAO {

    private Connection connection;

    public SongDAOImplementation() {
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

    private Song resultSetToSong(ResultSet resultSet) throws SQLException {
        Song song = new Song();
        song.setSong_id(resultSet.getInt("song_id"));
        song.setTitle(resultSet.getString("title"));
        song.setArtist(resultSet.getString("artist"));
        song.setAlbum(resultSet.getString("album"));
        song.setGenre(resultSet.getString("genre"));
        song.setStatus(resultSet.getInt("status"));
        song.setFilePath(resultSet.getString("file_path"));
        return song;
    }

    @Override
    public Song getSongById(int song_id) {
        String sql = "SELECT * FROM song WHERE song_id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, song_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSetToSong(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la canción: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void songStatus(int song_id, int status) {
        String sql = "UPDATE song SET status = ? WHERE song_id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, song_id);
            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Estatus de la canción actualizado correctamente!");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el estatus de la canción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM song ORDER BY title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Song song = resultSetToSong(resultSet);
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las canciones: " + e.getMessage());
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByGenre(String genre) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM song WHERE genre = ? ORDER BY title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Song song = resultSetToSong(resultSet);
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las canciones por género: " + e.getMessage());
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByArtist(String artist) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM song WHERE artist = ? ORDER BY title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, artist);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Song song = resultSetToSong(resultSet);
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las canciones por artista: " + e.getMessage());
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByAlbum(String album) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM song WHERE album = ? ORDER BY title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, album);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Song song = resultSetToSong(resultSet);
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las canciones por álbum: " + e.getMessage());
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByTitle(String title) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM song WHERE title = ? ORDER BY title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Song song = resultSetToSong(resultSet);
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las canciones por título: " + e.getMessage());
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByStatus(int status) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM song WHERE status = ? ORDER BY title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Song song = resultSetToSong(resultSet);
                songs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las canciones por estatus: " + e.getMessage());
            e.printStackTrace();
        }
        return songs;
    }
}
