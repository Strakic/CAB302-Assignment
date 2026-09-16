package com.trashslammers.model.gamestates;

public interface IGameStateDAO {
    void initializeTable();
    void saveFilePath(int userId, String filePath);
    String getFilePath(int userId);
}