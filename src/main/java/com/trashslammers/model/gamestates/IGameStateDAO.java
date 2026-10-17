package com.trashslammers.model.gamestates;

/**
 * Stores and looks up the location of each user's save file.
 */
public interface IGameStateDAO {
    /**
     * Creates the table that stores save file locations, if it doesn't exist yet.
     */
    void initializeTable();
    /**
     * Saves the location of a user's save file.
     * @param userId The id of the user
     * @param filePath The path of the save file
     */
    void saveFilePath(int userId, String filePath);
    /**
     * Gets the location of a user's save file.
     * @param userId The id of the user
     * @return The path of the save file, or null if the user has no save
     */
    String getFilePath(int userId);
}