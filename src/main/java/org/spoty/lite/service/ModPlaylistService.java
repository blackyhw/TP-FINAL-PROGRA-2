package org.spoty.lite.service;

import org.spoty.lite.database.dao.ModPlaylistDAO;

public class ModPlaylistService {
    private ModPlaylistDAO modPlaylistDAO;

    public ModPlaylistService(ModPlaylistDAO modPlaylistDAO) {
        this.modPlaylistDAO = modPlaylistDAO;
    }

    public void createPlaylist(String title, int mod_id, String description) {
        createVerification(title, mod_id, description);
        modPlaylistDAO.createPlaylist(title, mod_id, description);
    }

    public void addSongToPlaylist(int modPlaylist_id, int song_id) {
        listIdVerification(modPlaylist_id);
        songIdVerification(song_id);
        listExistenceVerification(modPlaylist_id);
        if (modPlaylistDAO.getPlaylistById(modPlaylist_id).contains(song_id)) {
            throw new IllegalArgumentException("La canción ya está en la lista de reproducción.");
        }
        modPlaylistDAO.addSongToPlaylist(modPlaylist_id, song_id);
    }

    public void deleteSongFromPlaylist(int modPlaylist_id, int song_id) {
        listIdVerification(modPlaylist_id);
        songIdVerification(song_id);
        listExistenceVerification(modPlaylist_id);
        if (!modPlaylistDAO.getPlaylistById(modPlaylist_id).contains(song_id)) {
            throw new IllegalArgumentException("La canción no está en la lista de reproducción.");
        }
        modPlaylistDAO.deleteSongFromPlaylist(modPlaylist_id, song_id);
    }

    public void updatePlaylist(String title, String description) {
        updateVerification(title, description);
        modPlaylistDAO.updatePlaylist(title, description);
    }

    public void disablePlaylist(int modPlaylist_id) {
        listIdVerification(modPlaylist_id);
        listExistenceVerification(modPlaylist_id);
        modPlaylistDAO.disablePlaylist(modPlaylist_id);
    }

    public void enablePlaylist(int modPlaylist_id) {
        listIdVerification(modPlaylist_id);
        listExistenceVerification(modPlaylist_id);
        modPlaylistDAO.enablePlaylist(modPlaylist_id);
    }

    public void getPlaylistById(int modPlaylist_id) {
        listIdVerification(modPlaylist_id);
        listExistenceVerification(modPlaylist_id);
        if (modPlaylistDAO.getPlaylistById(modPlaylist_id).isEmpty()) {
            System.out.println("La lista de reproducción está vacía.");
        }
        modPlaylistDAO.getPlaylistById(modPlaylist_id);
    }

    public void getPlaylistsByMod(int mod_id) {
        modIdVerification(mod_id);
        if (modPlaylistDAO.getPlaylistsByMod(mod_id).isEmpty()) {
            System.out.println("No hay listas de reproducción asociadas al moderador.");
        }
        modPlaylistDAO.getPlaylistsByMod(mod_id);
    }

    public void getDisabledPlaylistsByMod(int mod_id) {
        modIdVerification(mod_id);
        if (modPlaylistDAO.getDisabledPlaylistsByMod(mod_id).isEmpty()) {
            System.out.println("No hay listas de reproducción deshabilitadas.");
        }
        modPlaylistDAO.getDisabledPlaylistsByMod(mod_id);
    }

    private static void modIdVerification(int mod_id) {
        if(mod_id < 1) {
            throw new IllegalArgumentException("El ID del moderador debe ser mayor a 0.");
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

    private static void createVerification(String name, int mod_id, String description) {
        modIdVerification(mod_id);
        updateVerification(name, description);
    }

    private static void songIdVerification(int song_id) {
        if(song_id < 1 || song_id > 60) {
            throw new IllegalArgumentException("El ID de la canción debe estar entre 1 y 60.");
        }
    }

    private static void listIdVerification(int modPlaylist_id) {
        if(modPlaylist_id < 1) {
            throw new IllegalArgumentException("El ID de la lista de reproducción debe ser mayor a 0.");
        }
    }

    private void listExistenceVerification(int modPlaylist_id) {
        if(!modPlaylistDAO.doesPlaylistExist(modPlaylist_id)) {
            throw new IllegalArgumentException("La lista de reproducción no existe.");
        }
    }
}
