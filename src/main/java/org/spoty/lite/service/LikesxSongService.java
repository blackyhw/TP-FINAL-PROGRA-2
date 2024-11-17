package org.spoty.lite.service;

import org.spoty.lite.database.dao.RelationDAO;

public class LikesxSongService {
    private RelationDAO relationDAO;

    public LikesxSongService(RelationDAO relationDAO) {
        this.relationDAO = relationDAO;
    }

    public void addSongToLikes(int userId, int songId) {
        addVerification(userId, songId);
        relationDAO.addRelation(userId, songId);
    }

    public void deleteSongFromLikes(int userId, int songId) {
        deleteVerification(userId, songId);
        relationDAO.deleteRelation(userId, songId);
    }

    public void getLikedSongs(int userId) {
        getVerification(userId);
        relationDAO.getRelations(userId);
    }

    private void getVerification(int userId) {
        positiveIdVerification(userId);
        if(relationDAO.getRelations(userId).isEmpty()){
            System.out.println("No hay canciones en la lista de reproducción.");
        }
    }

    private void deleteVerification(int userId, int songId) {
        positiveIdVerification(userId);
        rangeVerification(songId);
        if(!relationDAO.getRelations(userId).contains(songId)){
            throw new IllegalArgumentException("La canción no está en la lista de reproducción.");
        }
    }

    private void addVerification(int userId, int songId) {
        positiveIdVerification(userId);
        rangeVerification(songId);
        if(relationDAO.getRelations(userId).contains(songId)){
            throw new IllegalArgumentException("La canción ya está en la lista de reproducción.");
        }
    }

    private static void rangeVerification(int songId) {
        if (songId < 1 || songId > 60) {
            throw new IllegalArgumentException("El ID de la canción debe estar entre 1 y 60.");
        }
    }

    private static void positiveIdVerification(int userId) {
        if (userId < 1) {
            throw new IllegalArgumentException("El ID de la lista de reproducción debe ser mayor a 0.");
        }
    }
}
