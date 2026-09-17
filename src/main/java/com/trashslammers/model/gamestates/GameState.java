package com.trashslammers.model.gamestates;

import com.trashslammers.model.OwnedAnimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {

    private final int score;
    private final List<OwnedAnimal> animals;

    public GameState() {
        this(0, Collections.emptyList());
    }

    public GameState(int score, List<OwnedAnimal> animals) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.score = score;
        this.animals = animals != null ? new ArrayList<>(animals) : Collections.emptyList();
    }

    public int getScore() {
        return score;
    }

    public List<OwnedAnimal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public int getAnimalCount() {
        return animals.size();
    }
}