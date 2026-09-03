package com.trashslammers.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private IUserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    void testAddUserandSetID() {
        String uniqueUsername = "bryn1";
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
