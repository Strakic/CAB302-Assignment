package com.trashslammers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private IUserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    void testAddUserAndSetID() {
        // Appends current epoch timestamp to guarantee a unique username every run
        String uniqueUsername = "bryn_" + UUID.randomUUID().toString().substring(0, 8);
        User newUser = new User(uniqueUsername, "password123");

        assertEquals(-1, newUser.getId());
        userDAO.addUser(newUser);

        assertNotEquals(-1, newUser.getId());
        User retrievedUser = userDAO.getUserByUsername(uniqueUsername);
        assertNotNull(retrievedUser);
        assertEquals(uniqueUsername, retrievedUser.getUsername());
        assertEquals("password123", retrievedUser.getPasswordHash());
    }
}