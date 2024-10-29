package org.spoty.lite.db.access;

import java.sql.Timestamp;
import java.util.Objects;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private Timestamp registrationDate;
    private int status;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = 1;
    }

    public int getId() {
        return id;
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

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return    "----------------User----------------" +
                "\n-Id.................." + id +
                "\n-Username............" + username +
                "\n-Password............" + password +
                "\n-Email..............." + email +
                "\n-RegistrationDate...." + registrationDate +
                "\n-Status.............." + status +
                "\n------------------------------------";
    }




}
