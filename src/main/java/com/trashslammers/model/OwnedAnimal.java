package com.trashslammers.model;

/**
 * An animal the player has bought. The enclosure is recorded here rather than on
 * the enclosure itself, so the enclosure screen can ask which animals it houses.
 */
public class OwnedAnimal {

    private final Animal animal;

    // null until the player places it, which leaves the animal owned but unplaced
    private String enclosureId;

    public OwnedAnimal(Animal animal) {
        this(animal, null);
    }

    public OwnedAnimal(Animal animal, String enclosureId) {
        if (animal == null) {
            throw new IllegalArgumentException("Owned animal must wrap an animal");
        }
        this.animal = animal;
        this.enclosureId = enclosureId;
    }

    public Animal getAnimal() {
        return animal;
    }

    public String getAnimalId() {
        return animal.getId();
    }

    public String getEnclosureId() {
        return enclosureId;
    }

    public boolean isPlaced() {
        return enclosureId != null;
    }

    public void placeIn(String enclosureId) {
        if (enclosureId == null || enclosureId.isBlank()) {
            throw new IllegalArgumentException("Enclosure id cannot be blank");
        }
        this.enclosureId = enclosureId;
    }
}
