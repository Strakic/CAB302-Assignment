package com.trashslammers.services;

import com.trashslammers.model.IUserDAO;
import com.trashslammers.model.User;
import com.trashslammers.model.UserDAO;
import com.trashslammers.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private String randomEmail() {
        // Generates a short, valid email under 20 chars total (e.g., "u_a1b2c3@t.co")
        return "u_" + UUID.randomUUID().toString().substring(0, 6) + "@t.co";
    }

    @BeforeEach
    void setUp() {
        IUserDAO userDAO = new UserDAO();
        authenticationService = new AuthenticationService(userDAO);
    }

    @Test
    void signUpStoresAHashedPasswordNotThePlaintext() {
        String email = randomEmail();
        User user = authenticationService.signUp(email, "password123");

        assertNotNull(user);
        assertEquals(email, user.getUsername());
        assertNotEquals("password123", user.getPasswordHash());
    }

    @Test
    void logInSucceedsWithCorrectCredentials() {
        String email = randomEmail();
        authenticationService.signUp(email, "password123");

        User loggedInUser = authenticationService.logIn(email, "password123");
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getUsername());
    }

    @Test
    void logInFailsWithWrongPassword() {
        String email = randomEmail();
        authenticationService.signUp(email, "password123");

        // Returns null when authentication fails
        User loggedInUser = authenticationService.logIn(email, "wrong-password");
        assertEquals(null, loggedInUser);
    }

    @Test
    void signUpRejectsADuplicateUsername() {
        String email = randomEmail();
        authenticationService.signUp(email, "first-pw");

        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.signUp(email, "second-pw"));
    }

    // upper and lower bounds testing
    // upper bounds on more than 20 characters in username
    @Test
    void upperBoundsoverUsernameInvalidLengthCap() {
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.signUp("123456789012345678901", "password123"));
    }
}