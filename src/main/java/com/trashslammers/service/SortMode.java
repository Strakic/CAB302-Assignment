package com.trashslammers.service;

import com.trashslammers.model.Animal;

import java.util.Comparator;

public enum SortMode {
    CATALOG_ORDER("Catalog order", null),
    RARITY_LOW_TO_HIGH("Rarity: low to high", Comparator.comparing(Animal::getRarity)),
    RARITY_HIGH_TO_LOW("Rarity: high to low", Comparator.comparing(Animal::getRarity).reversed()),
    COST_LOW_TO_HIGH("Cost: cheapest first", Comparator.comparingInt(Animal::getCost));

    private final String displayName;
    private final Comparator<Animal> comparator;

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
