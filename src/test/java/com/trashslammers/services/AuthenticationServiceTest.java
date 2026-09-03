package com.trashslammers.services;

import com.trashslammers.database.InMemoryUserRepository;
import com.trashslammers.database.UserRepository;
import com.trashslammers.model.User;
import com.trashslammers.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        UserRepository repository = new InMemoryUserRepository();
        authenticationService = new AuthenticationService(repository);

    }

    @Test
    void signUpStoresAHashedPasswordNotThePlaintext() {
        User user = authenticationService.signUp("arran@gmail.com", "password123");

        assertNotNull(user);
        assertEquals("arran@gmail.com", user.getUsername());
        assertNotEquals("password123", user.getPasswordHash());
    }

    @Test
    void logInSucceedsWithCorrectCredentials() {
        authenticationService.signUp("arran@gmail.com", "password123");

        assertTrue(authenticationService.logIn("arran@gmail.com", "password123"));
    }

    @Test
    void logInFailsWithWrongPassword() {
        authenticationService.signUp("arran@gmail.com", "password123");

        assertFalse(authenticationService.logIn("arran@gmail.com", "wrong-password"));
    }

    @Test
    void logInFailsForUnknownUsername() {
        assertFalse(authenticationService.logIn("unregisteredperson", "nothing"));
    }

    @Test
    void signUpRejectsADuplicateUsername() {
        authenticationService.signUp("arran@gmail.com", "first-pw");

        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.signUp("arran@gmail.com", "second-pw"));
    }

    // upper and lower bounds testing
    // upper bounds on more than 20 characters in username
    @Test
    void upperBoundsoverUsernameInvalidLengthCap() {
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.signUp("123456789012345678901", "password123"));
    }
}