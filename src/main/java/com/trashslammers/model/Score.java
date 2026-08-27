package com.trashslammers.model;

/**
 * A players score. Increases when trash is sorted correctly.
 * Decreases when an animal is purchased. Value can not go below 0.
 */

public class Score {
    public static final int CORRECT_SORT_POINTS = 10;

    private int value;

    public Score() {
        this(0);
    }

    /**
     *
     * @param value - saved score for this account. Must be 0 or greater
     */

    public Score(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        this.value = value;
    }



    public int getValue() {
        return value;
    }


    /** Adds points for placing trash in the correct bin */
    public void addForCorrectSort() {
        this.value += CORRECT_SORT_POINTS;
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
        if (value < cost){
            return false;
        }
        value -= cost;
        return true;
    }
}
