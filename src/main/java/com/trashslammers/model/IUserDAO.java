package com.trashslammers.model;

import java.util.List;

public interface IUserDAO {
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
}
