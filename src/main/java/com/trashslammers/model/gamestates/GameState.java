package com.trashslammers.model.gamestates;

import com.trashslammers.model.OwnedAnimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A saved game, made up of the player's score and the animals they own.
 */
public class GameState {

    private final int score;
    private final List<OwnedAnimal> animals;
    /**
     * Constructs an empty GameState with a score of 0 and no animals.
     */
    public GameState() {
        this(0, Collections.emptyList());
    }

    /**
     * Constructs a new GameState with the specified score and animals.
     * @param score The player's score, which can't be negative
     * @param animals The animals the player owns
     */
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

    /**
     * Gets the saved animals.
     * @return A read-only list of the player's animals
     */
    public List<OwnedAnimal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    /**
     * Gets the number of saved animals.
     * @return The number of animals the player owns
     */
    public int getAnimalCount() {
        return animals.size();
    }
}