package com.trashslammers.model.gamestates;

import com.trashslammers.database.DatabaseConnection;

import java.sql.*;
/**
 * Stores the location of each user's save file in the SQLite database.
 */
public class GameStateDAO implements IGameStateDAO {

    /**
     * Constructs a new GameStateDAO and creates the save_files table if needed.
     */
    public GameStateDAO() {
        initializeTable();
    }

    /**
     * Creates the save_files table if it doesn't exist yet.
     */
    @Override
    public void initializeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS save_files (" +
                "user_id INTEGER PRIMARY KEY, " +
                "file_path TEXT NOT NULL);";

        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize save_files table", e);
        }
    }

    /**
     * Saves the location of a user's save file, replacing any earlier one.
     * @param userId The id of the user
     * @param filePath The path of the save file
     */
    @Override
    public void saveFilePath(int userId, String filePath) {
        String sql = "INSERT INTO save_files (user_id, file_path) VALUES (?, ?) " +
                "ON CONFLICT(user_id) DO UPDATE SET file_path = excluded.file_path";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, filePath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update save file path in database", e);
        }
    }

    /**
     * Gets the location of a user's save file.
     * @param userId The id of the user
     * @return The path of the save file, or null if the user has no save
     */
    @Override
    public String getFilePath(int userId) {
        String sql = "SELECT file_path FROM save_files WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("file_path");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving save file path", e);
        }
        return null;
    }
}