package com.trashslammers.model.qte;

/**
 * A temporary point multiplier won from a quick time event. Reports a multiplier
 * of 1 while nothing is running, so scoring can always multiply by it.
 */
public class ActiveBoost {

    private int multiplier = 1;
    private double remainingSeconds;

    /** The factor to apply to points earned right now. */
    public int currentMultiplier() {
        return multiplier;
    }

    /** Starts a boost, replacing any that is already running. */
    public void activate(int multiplier, double durationSeconds) {
        this.multiplier = multiplier;
        this.remainingSeconds = durationSeconds;
    }

    /** How long the current boost has left, for the HUD countdown. */
    public double remainingSeconds() {
        return remainingSeconds;
    }

    /** Counts the boost down, dropping back to no bonus when it runs out. */
    public void tick(double deltaSeconds) {
        if (remainingSeconds <= 0) {
            return;
        }

        remainingSeconds -= deltaSeconds;
        if (remainingSeconds <= 0) {
            remainingSeconds = 0;
            multiplier = 1;
        }
    }
}
