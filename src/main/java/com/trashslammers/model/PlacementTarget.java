package com.trashslammers.model;

public class PlacementTarget {

    private final String id;
    private final String displayName;
    private final String habitat;

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
