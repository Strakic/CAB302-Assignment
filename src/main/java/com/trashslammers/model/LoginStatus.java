package com.trashslammers.model;

/**
 * The possible results of a login attempt, each with a message to show the player.
 */
public enum LoginStatus {
    SUCCESS("Login successful"),
    EMAIL_NOT_FOUND("Email not found"),
    INCORRECT_PASSWORD("Password incorrect"),
    EMPTY_FIELDS("Please fill in all fields");

    private final String message;

    LoginStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
