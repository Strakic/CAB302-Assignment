package com.trashslammers.service;

import com.trashslammers.model.Animal;

import java.util.Comparator;

/**
 * The options in the animal shop's sort drop-down.
 */

public enum SortMode {
    CATALOG_ORDER("Catalog order", null),
    RARITY_LOW_TO_HIGH("Rarity: low to high", Comparator.comparing(Animal::getRarity)),
    RARITY_HIGH_TO_LOW("Rarity: high to low", Comparator.comparing(Animal::getRarity).reversed()),
    COST_LOW_TO_HIGH("Cost: cheapest first", Comparator.comparingInt(Animal::getCost));

    private final String displayName;
    private final Comparator<Animal> comparator;

    /**
     * Constructs a new SortMode with the specified display name and comparator.
     * @param displayName The text shown in the drop-down
     * @param comparator How to order the animals, or null to keep the catalog's order
     */
    SortMode(String displayName, Comparator<Animal> comparator) {
        this.displayName = displayName;
        this.comparator = comparator;
    }

    // null means leave the animals in the order the catalog lists them
    public Comparator<Animal> comparator() {
        return comparator;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
