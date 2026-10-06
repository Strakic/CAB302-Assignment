package com.trashslammers.service;

import com.trashslammers.model.qte.GoldenTrashEvent;

import java.util.Optional;
import java.util.Random;

/**
 * Decides when a golden trash quick time event appears and which reward it pays.
 * Takes its Random so tests can hand it a seeded one.
 */
public class QteService {

    private final Random random;
    private final double spawnDelay = 12.0;
    private final double windowSeconds = 4.0;

    public QteService(Random random) {
        this.random = random;
    }

    /**
     * Advances the spawn timer by one game tick.
     *
     * @return the new event on the tick it spawns, otherwise empty
     */
    public Optional<GoldenTrashEvent> tick(double deltaSeconds) {
        throw new UnsupportedOperationException("not implemented");
    }
}
