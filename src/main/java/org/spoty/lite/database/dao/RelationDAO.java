package org.spoty.lite.database.dao;

import java.util.List;

public interface RelationDAO{
    int addRelation(int listId, int songId);
    void deleteRelation(int listId, int songId);
    List<Integer> getRelations(int listId);
    boolean doesPlaylistExist(int listId);
}
