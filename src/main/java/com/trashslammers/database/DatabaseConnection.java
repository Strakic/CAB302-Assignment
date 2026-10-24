package com.trashslammers.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides the single shared connection to the game's SQLite database.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:database.db";
    private static Connection instance = null;

    // Private constructor prevents direct instantiation
    private DatabaseConnection() {}

    /**
     * Gets the shared database connection, opening a new one if needed.
     * @return The connection to the SQLite database
     */
    public static synchronized Connection getInstance() {
        try {
            // Reopen if instance is null OR if the connection was previously closed
            if (instance == null || instance.isClosed()) {
                instance = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new RuntimeException("Failed to connect to SQLite database", e);
        }
        return instance;
    }
}