package org.spoty.lite.service;

import org.spoty.lite.database.dao.SongDAO;
import org.spoty.lite.model.Song;

import java.util.List;

public class SongService {
    private SongDAO songDAO;

    public SongService(SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    public void getSongById(int song_id) {
        if (song_id < 1 || song_id > 60) {
            throw new IllegalArgumentException("El ID de la canción debe estar entre 1 y 60.");
        }
        songDAO.getSongById(song_id);
    }

    public void songStatus(int song_id, int status) {
        if (song_id < 1 || song_id > 60) {
            throw new IllegalArgumentException("El ID de la canción debe estar entre 1 y 60.");
        }
        if (status < 0 || status > 1) {
            throw new IllegalArgumentException("El estado de la canción debe ser 0 o 1.");
        }
        songDAO.songStatus(song_id, status);
    }

    public List<Song> getAllSongs() {
        List<Song> songs = songDAO.getAllSongs();
        if(songs.isEmpty()){
            System.out.println("No hay canciones en la base de datos.");
        }
        return songs;
    }

    public void getSongsByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("El género de la canción no puede ser nulo o vacío.");
        }

        songDAO.getSongsByGenre(genre);
    }

    public void getSongsByArtist(String artist) {
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("El artista de la canción no puede ser nulo o vacío.");
        }
        songDAO.getSongsByArtist(artist);
    }

    public void getSongsByAlbum(String album) {
        if (album == null || album.trim().isEmpty()) {
            throw new IllegalArgumentException("El álbum de la canción no puede ser nulo o vacío.");
        }
        songDAO.getSongsByAlbum(album);
    }

    public void getSongsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la canción no puede ser nulo o vacío.");
        }
        songDAO.getSongsByTitle(title);
    }

    public void getSongsByStatus(int status) {
        if (status < 0 || status > 1) {
            throw new IllegalArgumentException("El estado de la canción debe ser 0 o 1.");
        }
        songDAO.getSongsByStatus(status);
    }
}
