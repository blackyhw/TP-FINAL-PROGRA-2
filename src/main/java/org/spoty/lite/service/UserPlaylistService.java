package org.spoty.lite.service;

import org.spoty.lite.database.dao.UserPlaylistDAO;

public class UserPlaylistService {
    private UserPlaylistDAO userPlaylistDAO;

    public UserPlaylistService(UserPlaylistDAO userPlaylistDAO) {
        this.userPlaylistDAO = userPlaylistDAO;
    }

    public void createPlaylist(String name, int user_id, String description) {
        createVerification(name, user_id, description);
        userPlaylistDAO.createPlaylist(name, user_id, description);
    }

    public void addSongToPlaylist(int userPlaylist_id, int song_id) {
        listIdVerification(userPlaylist_id);
        songIdVerification(song_id);
        listExistenceVerification(userPlaylist_id);
        if(userPlaylistDAO.getPlaylistById(userPlaylist_id).contains(song_id)) {
            throw new IllegalArgumentException("La canción ya está en la lista de reproducción.");
        }
        userPlaylistDAO.addSongToPlaylist(userPlaylist_id, song_id);
    }

    public void deleteSongFromPlaylist(int userPlaylist_id, int song_id) {
        listIdVerification(userPlaylist_id);
        songIdVerification(song_id);
        listExistenceVerification(userPlaylist_id);
        if(!userPlaylistDAO.getPlaylistById(userPlaylist_id).contains(song_id)) {
            throw new IllegalArgumentException("La canción no está en la lista de reproducción.");
        }
        userPlaylistDAO.deleteSongFromPlaylist(userPlaylist_id, song_id);
    }

    public void updatePlaylist(String title, String description) {
        updateVerification(title, description);
        userPlaylistDAO.updatePlaylist(title, description);
    }

    public void disablePlaylist(int userPlaylist_id) {
        listIdVerification(userPlaylist_id);
        listExistenceVerification(userPlaylist_id);
        userPlaylistDAO.disablePlaylist(userPlaylist_id);
    }

    public void enablePlaylist(int userPlaylist_id) {
        listIdVerification(userPlaylist_id);
        listExistenceVerification(userPlaylist_id);
        userPlaylistDAO.enablePlaylist(userPlaylist_id);
    }

    public void getPlaylistById(int userPlaylist_id) {
        listIdVerification(userPlaylist_id);
        listExistenceVerification(userPlaylist_id);
        if(userPlaylistDAO.getPlaylistById(userPlaylist_id).isEmpty()) {
            System.out.println("La lista de reproducción está vacía.");
        }
        userPlaylistDAO.getPlaylistById(userPlaylist_id);
    }

    public void getPlaylistsByUser(int user_id) {
        userIdVerification(user_id);
        if(userPlaylistDAO.getPlaylistsByUser(user_id).isEmpty()) {
            System.out.println("No hay listas de reproducción asociadas al usuario.");
        }
        userPlaylistDAO.getPlaylistsByUser(user_id);
    }

    public void getDisabledPlaylistsByUser(int user_id) {
        userIdVerification(user_id);
        if(userPlaylistDAO.getDisabledPlaylistsByUser(user_id).isEmpty()) {
            System.out.println("No hay listas de reproducción deshabilitadas asociadas al usuario.");
        }
        userPlaylistDAO.getDisabledPlaylistsByUser(user_id);
    }

    private static void userIdVerification(int user_id) {
        if(user_id < 1) {
            throw new IllegalArgumentException("El ID del usuario debe ser mayor a 0.");
        }
    }

    private static void updateVerification(String title, String description) {
        if(title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la lista de reproducción no puede ser nulo o vacío.");
        }
        if(description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la lista de reproducción no puede ser nula o vacía.");
        }
        if(description.length() > 255) {
            throw new IllegalArgumentException("La descripción de la lista de reproducción no puede tener más de 255 caracteres.");
        }
    }

    private static void createVerification(String name, int user_id, String description) {
        userIdVerification(user_id);
        updateVerification(name, description);
    }

    private static void songIdVerification(int song_id) {
        if(song_id < 1 || song_id > 60) {
            throw new IllegalArgumentException("El ID de la canción debe estar entre 1 y 60.");
        }
    }

    private static void listIdVerification(int userPlaylist_id) {
        if(userPlaylist_id < 1) {
            throw new IllegalArgumentException("El ID de la lista de reproducción debe ser mayor a 0.");
        }
    }

    private void listExistenceVerification(int userPlaylist_id) {
        if(!userPlaylistDAO.doesPlaylistExist(userPlaylist_id)) {
            throw new IllegalArgumentException("La lista de reproducción no existe.");
        }
    }
}
