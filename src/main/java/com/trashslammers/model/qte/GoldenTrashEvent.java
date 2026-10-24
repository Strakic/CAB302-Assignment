package com.trashslammers.model.qte;

/**
 * A single golden trash QTE. Tracks how long the item has been on
 * screen so the game can tell whether the player reacted in time.
 */
public class GoldenTrashEvent {

    private final double windowSeconds;
    private double elapsedSeconds;
    private boolean claimed;

    public GoldenTrashEvent(double windowSeconds) {
        this.windowSeconds = windowSeconds;
    }

    /** How long the player has to react, sprite can be paced to match. */
    public double windowSeconds() {
        return windowSeconds;
    }

    /** Advances by one tick. */
    public void tick(double deltaSeconds) {
        elapsedSeconds += deltaSeconds;
    }

    /** True while the player can still react to this event. */
    public boolean isActive() {
        return !claimed && elapsedSeconds < windowSeconds;
    }

    /**
     * Attempts to collect this event's reward. Only the first call succeeds, so double click cannot be paid out twice.
     *
     * @return true if the player reacted in time
     */
    public boolean claim() {
        if (!isActive()) {
            return false;
        }
        claimed = true;
        return true;
    }
}
