package com.trashslammers.model.qte;

/**
 * A temporary point multiplier won from a quick time event. Reports a multiplier
 * of 1 while nothing is running, so scoring can always multiply by it.
 */
public class ActiveBoost {

    private int multiplier = 1;

    /** The factor to apply to points earned right now. */
    public int currentMultiplier() {
        return multiplier;
    }

    /** Starts a boost, replacing any that is already running. */
    public void activate(int multiplier, double durationSeconds) {
        this.multiplier = multiplier;
    }

    public void tick(double deltaSeconds) {
    }
}
