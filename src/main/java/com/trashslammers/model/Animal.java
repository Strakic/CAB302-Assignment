package com.trashslammers.model;

/**
 * An animal the shop sells. Whether the player owns one, and where they put it,
 * is tracked separately by OwnedAnimal.
 */
public class Animal {
    private final String id;
    private final String name;
    private final String species;
    private final Rarity rarity;
    private final int cost;
    private final String spriteFile;
    private final String habitat;
    private final String fact;
    private final int basePointsPerSecond;

    /**
     * Constructs a new Animal with the specified details.
     * @param id The id of the animal, used for ownership lookups and never shown to the player
     * @param name The name of the animal
     * @param species The species of the animal
     * @param rarity The rarity of the animal
     * @param cost The cost of the animal in points
     * @param spriteFile The file name of the animal's sprite, or null if it doesn't have one
     * @param habitat The habitat of the animal
     * @param fact A fact about the animal, revealed when it is unlocked
     */
    public Animal(String id, String name, String species, Rarity rarity, int cost,
                  int basePointsPerSecond, String spriteFile, String habitat, String fact) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Animal id cannot be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Animal name cannot be blank");
        }
        if (rarity == null) {
            throw new IllegalArgumentException("Animal rarity cannot be null");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        if (basePointsPerSecond < 0) {
            throw new IllegalArgumentException("Base points per second cannot be negative");
        }

        this.id = id;
        this.name = name;
        this.species = species;
        this.rarity = rarity;
        this.cost = cost;
        this.basePointsPerSecond = basePointsPerSecond;
        this.spriteFile = spriteFile;
        this.habitat = habitat;
        this.fact = fact;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public Rarity getRarity() { return rarity; }
    public int getCost() { return cost; }
    public String getSpriteFile() { return spriteFile; }
    public String getHabitat() { return habitat; }
    public String getFact() { return fact; }
    public int getBasePointsPerSecond() { return basePointsPerSecond; }
}
