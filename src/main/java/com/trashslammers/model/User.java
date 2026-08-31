package com.trashslammers.model;

import java.util.ArrayList;

/**
 * base representation of an account
 *
 * password is only ever stored as a hashed value
 *
 */

public class User {
    // Static admin credentials for testing
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin";

    protected int id = -1; // -1 is when the data is not yet saved to the bd

    protected String username;
    protected String password;
    private Boolean signedIn;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public String getUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
