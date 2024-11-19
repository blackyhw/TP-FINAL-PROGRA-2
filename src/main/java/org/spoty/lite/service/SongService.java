package org.spoty.lite.service;

import org.spoty.lite.database.dao.SongDAO;
import org.spoty.lite.model.Song;

import java.util.List;

public class SongService {
    private static final String CANT_BE_NULL_OR_EMPTY = "de la canción no puede ser nulo o vacío.";
    private SongDAO songDAO;

    public SongService(SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    public void getSongById(int song_id) {
        songIdVerification(song_id);
        songDAO.getSongById(song_id);
    }

    public void songStatus(int song_id, int status) {
        songIdVerification(song_id);
        songStatusVerification(status);
        songDAO.songStatus(song_id, status);
    }

    public List<Song> getAllSongs() {
        List<Song> songs = songDAO.getAllSongs();
        if(songs.isEmpty()){
            System.out.println("No hay canciones en la base de datos.");
        }
        if(songs.size() < 60){
            System.out.println("La base de datos tiene problemas recuperando las canciones.\nEn total deberían haber 60 canciones.");
        }
        return songs;
    }

    public void getSongsByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("El género " + CANT_BE_NULL_OR_EMPTY);
        }
        songDAO.getSongsByGenre(genre);
    }

    public void getSongsByArtist(String artist) {
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("El artista " + CANT_BE_NULL_OR_EMPTY);
        }
        songDAO.getSongsByArtist(artist);
    }

    public void getSongsByAlbum(String album) {
        if (album == null || album.trim().isEmpty()) {
            throw new IllegalArgumentException("El álbum " + CANT_BE_NULL_OR_EMPTY);
        }
        songDAO.getSongsByAlbum(album);
    }

    public void getSongsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título " + CANT_BE_NULL_OR_EMPTY);
        }
        songDAO.getSongsByTitle(title);
    }

    public void getSongsByStatus(int status) {
        songStatusVerification(status);
        songDAO.getSongsByStatus(status);
    }

    private static void songIdVerification(int song_id) {
        if (song_id < 1 || song_id > 60) {
            throw new IllegalArgumentException("El ID de la canción debe estar entre 1 y 60.");
        }
    }

    private static void songStatusVerification(int status) {
        if (status < 0 || status > 1) {
            throw new IllegalArgumentException("El estado de la canción debe ser 0 o 1.");
        }
    }

}
