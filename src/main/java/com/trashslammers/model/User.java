package com.trashslammers.model;

/**
 * base representation of an account
 *
 * password is only ever stored as a hashed value
 *
 */


public class User {

    protected int id = -1; // -1 is when the data is not yet saved to the bd

    protected String username;

    // The salted/hashed password - never the plaintext
    protected String passwordHash;

    /**
     * Creates a brand-new user that has not been persisted yet.
     * The id is assigned later by whatever saves it (see UserRepository).
     */
    public User(String username, String passwordHash) {

        this.username = username;
        this.passwordHash = passwordHash;
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

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}