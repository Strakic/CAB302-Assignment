package com.trashslammers.service;

import com.trashslammers.model.qte.GoldenTrashEvent;
import com.trashslammers.model.qte.QteReward;

import java.util.Optional;
import java.util.Random;

/**
 * Decides when a golden trash quick time event appears and which reward it pays.
 * Takes its Random so tests can hand it a seeded one.
 */
public class QteService {

    private final Random random;
    private final double spawnDelay = 12.0;
    private final double windowSeconds = 5.0;
    private double timer = spawnDelay;

    public QteService(Random random) {
        this.random = random;
    }

    /**
     * Advances the spawn timer by one game tick.
     *
     * @return the new event on the tick it spawns, otherwise empty
     */
    public Optional<GoldenTrashEvent> tick(double deltaSeconds) {
        timer -= deltaSeconds;
        if (timer > 0) {
            return Optional.empty();
        }

        timer = spawnDelay;
        return Optional.of(new GoldenTrashEvent(windowSeconds));
    }

    /** Points paid out by the cash bonus reward. */
    public int cashBonus() {
        throw new UnsupportedOperationException("not implemented");
    }

    /** Factor the point multiplier reward applies to correct sorts. */
    public int boostMultiplier() {
        throw new UnsupportedOperationException("not implemented");
    }

    /** How long the point multiplier reward lasts. */
    public double boostSeconds() {
        throw new UnsupportedOperationException("not implemented");
    }

    /** How many extra items the trash burst reward drops. */
    public int burstSize() {
        throw new UnsupportedOperationException("not implemented");
    }

    /** Picks the reward a successful claim pays out. */
    public QteReward rollReward() {
        QteReward[] rewards = QteReward.values();
        return rewards[random.nextInt(rewards.length)];
    }
}
