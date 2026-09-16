package com.trashslammers.model.gamestates;

import com.trashslammers.model.OwnedAnimal;

import java.util.Collections;
import java.util.List;

public class GameState {

    public GameState() {
    }

    public GameState(int score, List<OwnedAnimal> animals) {
    }

    public int getScore() {
        return 0;
    }

    public List<OwnedAnimal> getAnimals() {
        return Collections.emptyList();
    }

    public int getAnimalCount() {
        return 0;
    }
}