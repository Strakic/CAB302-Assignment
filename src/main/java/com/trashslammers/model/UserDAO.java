package com.trashslammers.model;

import com.trashslammers.database.DatabaseConnection;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getInstance();
        //runs create table for this object
        createTable();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "username VARCHAR NOT NULL UNIQUE, "
                            + "passwordHash VARCHAR NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }

    }

    // dont forget to use ? to parameterise SQL queries
    @Override
    public void addUser(User user) {
        String query = "INSERT INTO users (username, passwordHash) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query.toLowerCase(), Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.executeUpdate();

            ResultSet generateadKeys = statement.getGeneratedKeys();
            if (generateadKeys.next()) {
                user.setId(generateadKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
