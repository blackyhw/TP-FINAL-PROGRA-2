package org.spoty.lite.model;

import java.sql.Timestamp;

public class User {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private int status;
    private Timestamp registration_date;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = 1;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setRegistration_date(Timestamp registration_date) {
        this.registration_date = registration_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getStatus() {
        return status;
    }

    public Timestamp getRegistration_date() {
        return registration_date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(int status) {
        this.status = status;
    }



}
