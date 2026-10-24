package com.trashslammers.model;

import com.trashslammers.model.usertype.Role;

/**
 * A standard user account, which premium and admin users are built on.
 */


public class User {

    private int id = -1; // -1 is when the data is not yet saved to the bd

    private String username;

    // The salted/hashed password - never the plaintext
    private String passwordHash;

    /**
     * Constructs a new User with the specified username and password hash.
     * @param username The username of the user
     * @param passwordHash The hashed password of the user
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

    /**
     * Gets the hashed password of the user, never the plain text password.
     * @return The hashed password of the user
     */
    public String getPasswordHash() {
        return passwordHash;
    }


    public String getDisplayName() {
        int at = username.indexOf('@');
        return at > 0 ? username.substring(0, at) : username;
    }
}