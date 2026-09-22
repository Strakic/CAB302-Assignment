package com.trashslammers.model;

import com.trashslammers.model.usertype.Role;

/**
 * base representation of an account
 *
 * password is only ever stored as a hashed value
 *
 */


public class User {

    private int id = -1; // -1 is when the data is not yet saved to the bd

    private String username;

    // The salted/hashed password - never the plaintext
    private String passwordHash;

    /**
     * Creates a brand-new user that has not been persisted yet.
     * The id is assigned later by whatever saves it (see UserRepository).
     */
    public User(String username, String passwordHash) {

        this.username = username;
        this.passwordHash = passwordHash;
    }


    public Role getRole() {
        return Role.STANDARD;
    }

    public boolean canManageAnimals() {
        return false;
    }

    public int getPointMultiplyer() {
        return 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** Returns the stored hash value and never the plaintext password. */
    public String getPasswordHash() {
        return passwordHash;
    }


    public String getDisplayName() {
        int at = username.indexOf('@');
        return at > 0 ? username.substring(0, at) : username;
    }
}