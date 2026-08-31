package com.trashslammers.service;

import com.trashslammers.database.InMemoryUserRepository;
import com.trashslammers.database.UserRepository;
import com.trashslammers.model.User;
import com.trashslammers.util.EmailValidator;
import com.trashslammers.util.PasswordUtil;
import java.util.Optional;

public class AuthenticationService implements IAuthenticationService{



    /**
     * Authenticates a user against the hardcoded admin credentials.
     *
     * @param username The entered username
     * @param password The entered password
     * @return 1 if credentials match, 0 otherwise
     */

    private static final UserRepository DEFAULT_REPOSITORY = new InMemoryUserRepository();

    private final UserRepository userRepository;

    public AuthenticationService() {
        this(DEFAULT_REPOSITORY);
    }

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User signUp(String username, String password) {
        if (!EmailValidator.isValid(username)) {
            throw new IllegalArgumentException("Username must be a valid email address");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken");
        }

        String passwordHash = PasswordUtil.hashPassword(password);
        User newUser = new User(username, passwordHash);
        return userRepository.save(newUser);
    }



    @Override
    public boolean logIn(String username, String password) {

        Optional<User> existingUser = userRepository.findByUsername(username);
        return existingUser.isPresent()
                && PasswordUtil.verifyPassword(password, existingUser.get().getPasswordHash());
    }
}
