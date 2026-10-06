package com.trashslammers.model.qte;

/**
 * A single golden trash quick time event. Tracks how long the item has been on
 * screen so the game can tell whether the player reacted in time.
 */
public class GoldenTrashEvent {

    private final double windowSeconds;
    private double elapsedSeconds;

    public GoldenTrashEvent(double windowSeconds) {
        this.windowSeconds = windowSeconds;
    }

    /** Advances the event by one game tick. */
    public void tick(double deltaSeconds) {
        elapsedSeconds += deltaSeconds;
    }

    /** True while the player can still react to this event. */
    public boolean isActive() {
        return elapsedSeconds < windowSeconds;
    }

    public boolean claim() {
        throw new UnsupportedOperationException("not implemented");
    }
}
