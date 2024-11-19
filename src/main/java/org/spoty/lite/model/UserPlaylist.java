package org.spoty.lite.model;

import java.sql.Timestamp;

public class UserPlaylist {
    private int userplaylist_id;
    private int user_id;
    private String title;
    private String description;
    private int status;
    private Timestamp creation_date;

    public UserPlaylist() {
    }

    public int getUserplaylist_id() {
        return userplaylist_id;
    }

    public void setUserplaylist_id(int userplaylist_id) {
        this.userplaylist_id = userplaylist_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }


}
