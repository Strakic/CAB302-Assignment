package com.trashslammers.service;

import com.trashslammers.model.User;

/**
 * Holds whoever is currently logged in

 */
public final class Session {

    private static User currentUser;

    private Session() {

    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void clear() {
        currentUser = null;
    }
}