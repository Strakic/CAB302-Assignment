package com.trashslammers.model;

import java.util.ArrayList;

public class User {
    // Static admin credentials for testing
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin";

    private String username;
    private String password;
    private Boolean signedIn;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
