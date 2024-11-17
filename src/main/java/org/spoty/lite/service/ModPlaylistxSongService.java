package org.spoty.lite.service;

import org.spoty.lite.database.dao.RelationDAO;

public class ModPlaylistxSongService {
    private RelationDAO relationDAO;

    public ModPlaylistxSongService(RelationDAO relationDAO) {
        this.relationDAO = relationDAO;
    }

    public void addSongToPlaylist(int listId, int songId) {
        addVerification(listId, songId);
        relationDAO.addRelation(listId, songId);
    }

    public void deleteSongFromPlaylist(int listId, int songId) {
        deleteVerification(listId, songId);
        relationDAO.deleteRelation(listId, songId);
    }

    public void getPlaylistSongs(int listId) {
        getVerification(listId);
        relationDAO.getRelations(listId);
    }

    private void getVerification(int listId) {
        positiveIdVerification(listId);
        if(relationDAO.getRelations(listId).isEmpty()){
            System.out.println("No hay canciones en la lista de reproducción.");
        }
    }

    private void deleteVerification(int listId, int songId) {
        positiveIdVerification(listId);
        rangeVerification(songId);
        if(!relationDAO.getRelations(listId).contains(songId)){
            throw new IllegalArgumentException("La canción no está en la lista de reproducción.");
        }
    }

    private void addVerification(int listId, int songId) {
        positiveIdVerification(listId);
        rangeVerification(songId);
        if(relationDAO.getRelations(listId).contains(songId)){
            throw new IllegalArgumentException("La canción ya está en la lista de reproducción.");
        }
    }

    private static void rangeVerification(int songId) {
        if (songId < 1 || songId > 60) {
            throw new IllegalArgumentException("El ID de la canción debe estar entre 1 y 60.");
        }
    }

    private static void positiveIdVerification(int listId) {
        if (listId < 1) {
            throw new IllegalArgumentException("El ID de la lista de reproducción debe ser mayor a 0.");
        }
    }

}
