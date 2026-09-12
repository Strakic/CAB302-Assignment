package com.trashslammers.model.usertype;

public enum Role {

    STANDARD, PREMIUM, ADMIN;

    public static Role fromDb(String value) {
        if (value == null || value .isBlank()) return STANDARD;
        try {
            return Role.valueOf(value.trim().toUpperCase());

        } catch (IllegalArgumentException e) {
            return STANDARD;
        }
    }
}
