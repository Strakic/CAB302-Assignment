package com.trashslammers.model;

/**
 * An enclosure that an owned animal can be placed in.
 */
public class PlacementTarget {

    private final String id;
    private final String displayName;
    private final String habitat;

    /**
     * Constructs a new PlacementTarget with the specified id, display name and habitat.
     * @param id The id of the enclosure
     * @param displayName The name of the enclosure shown to the player
     * @param habitat The habitat inside the enclosure
     */
    public PlacementTarget(String id, String displayName, String habitat) {
        this.id = id;
        this.displayName = displayName;
        this.habitat = habitat;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHabitat() {
        return habitat;
    }

    @Override
    public String toString() {
        return displayName + " (" + habitat + ")";
    }
}
