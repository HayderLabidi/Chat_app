package com.example.my_application;

public class AppUser {
    private String username;
    private String uid;

    // Constructor
    public AppUser(String username, String uid) {
        this.username = username;
        this.uid = uid;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}