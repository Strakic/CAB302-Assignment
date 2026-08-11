package com.trashslammers.service;
import com.trashslammers.model.User;

public class AuthenticationService implements IAuthenticationService{

    /**
     * Authenticates a user against the hardcoded admin credentials.
     *
     * @param username The entered username
     * @param password The entered password
     * @return 1 if credentials match, 0 otherwise
     */

    @Override
    public User signUp(String username, String password) {
        return null;
    }

    @Override
    public boolean logIn(String username, String password) {
        // Compare entered values against static User constants
        return username.equals(User.ADMIN_USERNAME) && password.equals(User.ADMIN_PASSWORD);
        // returns true or false
    }
}
