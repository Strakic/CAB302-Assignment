package com.trashslammers.model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A players score. Increases when trash is sorted correctly.
 * Decreases when an animal is purchased. Value can not go below 0.
 */

public class Score {
    /** Default award for a correct sort, used when no item value is given. */
    public static final int CORRECT_SORT_POINTS = 10;
    private final IntegerProperty value = new SimpleIntegerProperty();

    /** Creates a new score starting at 0, like for a new account. */
    public Score() {
        this(0);
    }

    /**
     * Restores the score loaded from an account
     *
     * @param value - saved score for an account. Must be 0 or greater
     */

    public Score(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        this.value.set(value);
    }
    public int getValue() {
        return value.get();
    }
    /** Lets the UI bind to the score so it updates by itself. */
    public ReadOnlyIntegerProperty valueProperty() {
        return value;
    }

    /**
     * Adds a specific number of points.
     *
     * @param points the points to add, can't be negative
     */
    public void add(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        value.set(value.get() + points);
    }


    /** Adds points for placing trash in the correct bin with a multiplier*/
    public void addForCorrectSort(int multiplier) {
        add(CORRECT_SORT_POINTS * multiplier);
    }


    /**
     * Attempts to pay for an animal
     *
     * @param cost - the points to subtract, can't be negative
     * @return true if the purchase happened, false if the score was too low
     * (score is left unchanged when false)
     */
    public boolean spendOnAnimal(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        if (value.get() < cost) {
            return false;
        }
        value.set(value.get() - cost);
        return true;

    }
}


