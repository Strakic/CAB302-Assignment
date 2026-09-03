package com.trashslammers.service;

import com.trashslammers.model.IUserDAO;
import com.trashslammers.model.User;
import com.trashslammers.model.UserDAO;
import com.trashslammers.util.EmailValidator;
import com.trashslammers.util.PasswordUtil;

public class AuthenticationService implements IAuthenticationService {

    private final IUserDAO userDAO;

    // Default constructor uses your actual SQLite DAO
    public AuthenticationService() {
        this(new UserDAO());
    }


    public AuthenticationService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User signUp(String username, String password) {
        if (!EmailValidator.isValid(username)) {
            throw new IllegalArgumentException("Username must be a valid email address");
        }


        if (userDAO.getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken");
        }


        String passwordHash = PasswordUtil.hashPassword(password);
        User newUser = new User(username, passwordHash);
        userDAO.addUser(newUser);

        return newUser;
    }

    @Override
    public boolean logIn(String username, String password) {
        User existingUser = userDAO.getUserByUsername(username);
        return existingUser != null
                && PasswordUtil.verifyPassword(password, existingUser.getPasswordHash());
    }
}