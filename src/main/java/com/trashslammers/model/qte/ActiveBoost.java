package com.trashslammers.model.qte;

/**
 * A temporary point multiplier won from a quick time event. Reports a multiplier
 * of 1 while nothing is running, so scoring can always multiply by it.
 */
public class ActiveBoost {

    public int currentMultiplier() {
        throw new UnsupportedOperationException("not implemented");
    }

    public void activate(int multiplier, double durationSeconds) {
    }
}
