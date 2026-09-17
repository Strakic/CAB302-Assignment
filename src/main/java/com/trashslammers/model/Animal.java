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

    /**
     * @param id   - key used for ownership lookups, never shown to the player
     * @param fact - educational fact revealed when the animal is unlocked
     */
    public Animal(String id, String name, String species, Rarity rarity, int cost,
                  String spriteFile, String habitat, String fact) {
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

        this.id = id;
        this.name = name;
        this.species = species;
        this.rarity = rarity;
        this.cost = cost;
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
}
