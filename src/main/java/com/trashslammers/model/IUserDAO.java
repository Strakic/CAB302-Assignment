package com.trashslammers.model;

import java.util.List;

/**
 * Saves, loads, updates and deletes user accounts.
 */
public interface IUserDAO {
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
}
