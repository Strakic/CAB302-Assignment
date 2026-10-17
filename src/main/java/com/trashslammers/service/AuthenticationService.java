package com.trashslammers.service;

import com.trashslammers.model.IUserDAO;
import com.trashslammers.model.User;
import com.trashslammers.model.UserDAO;
import com.trashslammers.model.usertype.Role;
import com.trashslammers.model.usertype.UserFactory;
import com.trashslammers.util.EmailValidator;
import com.trashslammers.util.PasswordUtil;

/**
 * Handles signing up, logging in and upgrading user accounts.
 */
public class AuthenticationService implements IAuthenticationService {

    private final IUserDAO userDAO;

    // Default constructor uses your actual SQLite DAO
    public AuthenticationService() {
        this(new UserDAO());
    }

    /**
     * Constructs a new AuthenticationService with the specified user DAO.
     * @param userDAO The DAO used to store user accounts
     */
    public AuthenticationService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Creates a new account with a hashed password.
     * @param username The email address of the new user
     * @param password The password of the new user
     * @return The new user
     */
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

    /**
     * Checks a username and password against the saved accounts.
     * @param username The username entered
     * @param password The password entered
     * @return The matching user, or null if the details are wrong
     */
    @Override
    public User logIn(String username, String password) {
        User existingUser = userDAO.getUserByUsername(username);

        if (existingUser == null) {
            return null;
        }

        if (!PasswordUtil.verifyPassword(password, existingUser.getPasswordHash())) {
            return null;
        }

        return existingUser;
    }

    /**
     * Upgrades a standard user to premium and saves the change.
     * @param user The user to upgrade
     * @return The upgraded user, or the same user if they weren't standard
     */
    @Override
    public User upgradeToPremium(User user) {
        if (user == null) {
            throw new IllegalArgumentException("There is no user to upgrade");
        }

        // Admins are already outranking premium
        if (user.getRole() != Role.STANDARD) {
            return user;
        }
        User upgraded = UserFactory.create(
                Role.PREMIUM,
                user.getId(),
                user.getUsername(),
                user.getPasswordHash());

        userDAO.updateUser(upgraded);
        return upgraded;
    }
}