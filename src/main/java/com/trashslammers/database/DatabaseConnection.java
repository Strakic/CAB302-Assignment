package com.trashslammers.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection instance = null;

    /*
    explanation from canvas:
    "Even though this code doesn't seem to do anything,
    it will actually create a new SQLite database file named database.db in
    the root directory of the project (but if the file already exists,
    it will simply connect to it"
     */

    private DatabaseConnection() {
        String url = "jdbc:sqlite:database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }
        return instance;
    }
}
