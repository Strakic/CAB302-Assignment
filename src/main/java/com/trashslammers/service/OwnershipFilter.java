package com.trashslammers.service;

public enum OwnershipFilter {
    ALL("All animals"),
    OWNED("Owned only"),
    UNOWNED("Not yet owned");

    private final String displayName;

    OwnershipFilter(String displayName) {
        this.displayName = displayName;
    }

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
