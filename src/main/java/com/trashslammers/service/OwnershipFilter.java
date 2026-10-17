package com.trashslammers.service;

/**
 * The options in the animal shop's filter drop-down.
 */
public enum OwnershipFilter {
    ALL("All animals"),
    OWNED("Owned only"),
    UNOWNED("Not yet owned");

    private final String displayName;

    OwnershipFilter(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Checks whether an animal should be shown with this filter.
     * @param owned Whether the player owns the animal
     * @return true if the animal should be shown, otherwise false
     */
    public boolean matches(boolean owned) {
        return switch (this) {
            case ALL -> true;
            case OWNED -> owned;
            case UNOWNED -> !owned;
        };
    }

    @Override
    public String toString() {
        return displayName;
    }
}
