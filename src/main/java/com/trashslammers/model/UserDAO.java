package com.trashslammers.model;

import com.trashslammers.database.DatabaseConnection;
import com.trashslammers.model.usertype.Role;
import com.trashslammers.model.usertype.UserFactory;
import com.trashslammers.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private final Connection connection;

    private static final String ADMIN_USERNAME = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "Admin1234";

    public UserDAO() {
        this.connection = DatabaseConnection.getInstance();
        //runs create table for this object
        createTable();
        addRoleColumnIfMissing();
        seedAdminIfMissing();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "username VARCHAR NOT NULL UNIQUE, "
                            + "passwordHash VARCHAR NOT NULL,"
                            + "role VARCHAR NOT NULL DEFAULT 'STANDARD'"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }

    }


    private void addRoleColumnIfMissing() {
        if (hasRoleColumn()) {
            return;
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "ALTER TABLE users ADD COLUMN role VARCHAR NOT NULL DEFAULT 'STANDARD'");
        } catch (SQLException ex) {
            System.err.println("Could not add role column: " + ex);
        }
    }

    private boolean hasRoleColumn() {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("PRAGMA table_info(users)")) {
            while (rs.next()) {
                if ("role".equalsIgnoreCase(rs.getString("name"))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Could not inspect users table: " + ex);
        }
        return false;
    }

    /** Creates the default admin if the database contains no admin at all. */
    private void seedAdminIfMissing() {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        } catch (SQLException ex) {
            System.err.println("Could not check for an existing admin: " + ex);
            return;
        }

        User admin = UserFactory.create(
                Role.ADMIN,
                ADMIN_USERNAME,
                PasswordUtil.hashPassword(ADMIN_PASSWORD));
        addUser(admin);
        System.out.println("Seeded default admin account: " + ADMIN_USERNAME);
    }



    // dont forget to use ? to parameterise SQL queries
    @Override
    public void addUser(User user) {
        String query = "INSERT INTO users (username, passwordHash, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getRole().name());
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

        String query = "UPDATE users SET username = ?, passwordHash = ?, role = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getRole().name());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update user " + user.getUsername(), e);
        }
    }

    @Override
    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete user with id " + id, e);
        }
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String query = "SELECT id, username, passwordHash, role FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not look up user " + username, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, passwordHash, role FROM users ORDER BY id";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not list users", e);
        }
        return users;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /** Makes one row into the user subclass that its role column names */
    private User mapRow(ResultSet rs) throws SQLException {
        return UserFactory.create(
                Role.fromDb(rs.getString("role")),
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("passwordHash"));
    }
}
