package com.trashslammers.service;

import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.Rarity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveGameServiceTest {

    private Connection connection;
    private SaveGameService saveGameService;

    @BeforeEach
    void setUp() throws SQLException {
        // Use an in-memory SQLite database for isolated test runs
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        // Initialize the DB schema required for save metadata
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS save_files (" +
                    "user_id INTEGER PRIMARY KEY, " +
                    "file_path TEXT NOT NULL)");
        }

        saveGameService = new SaveGameService(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }



    @Test
    void saveGameCreatesCsvFileAndRecordsPathInDatabase(@TempDir Path tempDir) throws IOException, SQLException {
        Path saveFilePath = tempDir.resolve("save_user_1.csv");
        int userId = 1;
        int score = 250;
        List<OwnedAnimal> animals = List.of(
                new OwnedAnimal(new Animal("koala", "Koala", "Phascolarctos cinereus", Rarity.COMMON, 100, null, "Eucalypt Forest", "Fact 1")),
                new OwnedAnimal(new Animal("orangutan", "Orangutan", "Pongo pygmaeus", Rarity.RARE, 700, null, "Lowland Forest", "Fact 2"))
        );

        saveGameService.saveGame(userId, saveFilePath, score, animals);

        // Verify CSV file exists and content matches
        assertTrue(Files.exists(saveFilePath));
        List<String> lines = Files.readAllLines(saveFilePath);
        assertEquals("SCORE,250", lines.get(0));
        assertEquals("TOTAL_ANIMALS,2", lines.get(1));
        assertTrue(lines.contains("ANIMAL,koala"));
        assertTrue(lines.contains("ANIMAL,orangutan"));

        // Verify database path entry
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT file_path FROM save_files WHERE user_id = 1")) {
            assertTrue(rs.next());
            assertEquals(saveFilePath.toAbsolutePath().toString(), rs.getString("file_path"));
        }
    }

    @Test
    void loadGameRestoresCorrectScoreAndAnimals(@TempDir Path tempDir) throws IOException, SQLException {
        Path saveFilePath = tempDir.resolve("save_user_2.csv");
        int userId = 2;
        int score = 500;
        List<OwnedAnimal> animals = List.of(
                new OwnedAnimal(new Animal("blue-whale", "Blue Whale", "Balaenoptera musculus", Rarity.LEGENDARY, 3750, null, "Open Ocean", "Fact 3"))
        );

        saveGameService.saveGame(userId, saveFilePath, score, animals);
        GameState loadedState = saveGameService.loadGame(userId);

        assertEquals(500, loadedState.getScore());
        assertEquals(1, loadedState.getAnimals().size());
        assertEquals("blue-whale", loadedState.getAnimals().get(0).getAnimalId());
    }



    @Test
    void lowerBoundZeroScoreAndZeroAnimals(@TempDir Path tempDir) throws IOException, SQLException {
        Path saveFilePath = tempDir.resolve("save_zero.csv");
        int userId = 10;
        int score = 0; // Lower bound for score
        List<OwnedAnimal> animals = List.of(); // Lower bound for collection size

        saveGameService.saveGame(userId, saveFilePath, score, animals);
        GameState loadedState = saveGameService.loadGame(userId);

        assertEquals(0, loadedState.getScore());
        assertEquals(0, loadedState.getAnimalCount());
        assertTrue(loadedState.getAnimals().isEmpty());
    }

    @Test
    void saveRejectsNegativeScore(@TempDir Path tempDir) {
        Path saveFilePath = tempDir.resolve("save_negative.csv");
        assertThrows(IllegalArgumentException.class, () ->
                saveGameService.saveGame(1, saveFilePath, -1, List.of())
        );
    }



    @Test
    void upperBoundLargeScoreAndDuplicateAnimalsHandledCorrectly(@TempDir Path tempDir) throws IOException, SQLException {
        Path saveFilePath = tempDir.resolve("save_large.csv");
        int userId = 999;
        int maxScore = Integer.MAX_VALUE; // Upper bound for score

        Animal koala = new Animal("koala", "Koala", "Phascolarctos cinereus", Rarity.COMMON, 100, null, "Forest", "Fact");
        // Upper bound check: user owns multiple instances of the same animal type
        List<OwnedAnimal> largeCollection = List.of(
                new OwnedAnimal(koala),
                new OwnedAnimal(koala),
                new OwnedAnimal(koala)
        );

        saveGameService.saveGame(userId, saveFilePath, maxScore, largeCollection);
        GameState loadedState = saveGameService.loadGame(userId);

        assertEquals(Integer.MAX_VALUE, loadedState.getScore());
        assertEquals(3, loadedState.getAnimalCount());
        assertEquals(3, loadedState.getAnimals().size());
    }



    @Test
    void savingAgainUpdatesExistingDatabaseRecordAndOverwritesFile(@TempDir Path tempDir) throws IOException, SQLException {
        Path firstPath = tempDir.resolve("save_v1.csv");
        Path secondPath = tempDir.resolve("save_v2.csv");
        int userId = 5;

        // First save
        saveGameService.saveGame(userId, firstPath, 100, List.of());

        // Second save (updating state and path)
        saveGameService.saveGame(userId, secondPath, 300, List.of());

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT file_path FROM save_files WHERE user_id = 5")) {
            assertTrue(rs.next());
            assertEquals(secondPath.toAbsolutePath().toString(), rs.getString("file_path"));
            assertFalse(rs.next(), "Database should only have one row per user_id");
        }
    }

    @Test
    void loadGameThrowsExceptionWhenUserHasNoSaveRecord() {
        assertThrows(IllegalArgumentException.class, () -> saveGameService.loadGame(99999));
    }
}