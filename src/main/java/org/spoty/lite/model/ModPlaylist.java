package org.spoty.lite.model;

import java.sql.Timestamp;

public class ModPlaylist {
    private int modlist_id;
    private int mod_id;
    private String title;
    private String description;
    private int status;
    private Timestamp creation_date;

    public ModPlaylist() {
    }

    public int getModlist_id() {
        return modlist_id;
    }

    public void setModlist_id(int modlist_id) {
        this.modlist_id = modlist_id;
    }

    public int getMod_id() {
        return mod_id;
    }

    public void setMod_id(int mod_id) {
        this.mod_id = mod_id;
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
