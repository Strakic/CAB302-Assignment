package com.trashslammers.service;

import com.trashslammers.model.gamestates.*;
import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveGameService {

    private final Connection connection;

    public SaveGameService(Connection connection, IGameStateDAO gameStateDAO) {
        if (connection == null) {
            throw new IllegalArgumentException("Database connection cannot be null");
        }
        this.connection = connection;
    }

    /**
     * Writes the current score and owned animals to a CSV file and updates the file path in SQLite.
     */
    public void saveGame(int userId, Path saveFilePath, int score, List<OwnedAnimal> animals) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        if (saveFilePath == null) {
            throw new IllegalArgumentException("Save file path cannot be null");
        }

        // Write game state to CSV file
        try (BufferedWriter writer = Files.newBufferedWriter(saveFilePath)) {
            writer.write("SCORE," + score);
            writer.newLine();
            writer.write("TOTAL_ANIMALS," + animals.size());
            writer.newLine();

            for (OwnedAnimal owned : animals) {
                String animalId = owned.getAnimalId();
                String enclosure = owned.getEnclosureId() != null ? owned.getEnclosureId() : "";
                writer.write("ANIMAL," + animalId + "," + enclosure);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write save game file", e);
        }

        //Persist CSV file path to SQLite database
        String sql = "INSERT INTO save_files (user_id, file_path) VALUES (?, ?) " +
                "ON CONFLICT(user_id) DO UPDATE SET file_path = excluded.file_path";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, saveFilePath.toAbsolutePath().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update save file path in database", e);
        }
    }

    /**
     * Reads the file path from SQLite and loads the game state from the corresponding CSV.
     */
    public GameState loadGame(int userId) {
        String filePathStr = getSaveFilePath(userId);
        if (filePathStr == null) {
            throw new IllegalArgumentException("No save file found for user ID: " + userId);
        }

        Path filePath = Paths.get(filePathStr);
        if (!Files.exists(filePath)) {
            throw new IllegalStateException("Save file recorded in database does not exist: " + filePathStr);
        }

        int score = 0;
        List<OwnedAnimal> animals = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");

                switch (parts[0]) {
                    case "SCORE" -> score = Integer.parseInt(parts[1]);
                    case "ANIMAL" -> {
                        String animalId = parts[1];
                        String enclosureId = parts.length > 2 && !parts[2].isBlank() ? parts[2] : null;

                        Animal animal = AnimalCatalog.findById(animalId)
                                .orElseThrow(() -> new IllegalArgumentException("Unknown animal ID in save file: " + animalId));

                        animals.add(new OwnedAnimal(animal, enclosureId));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read save game file", e);
        }

        return new GameState(score, animals);
    }

    private String getSaveFilePath(int userId) {
        String sql = "SELECT file_path FROM save_files WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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