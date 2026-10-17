package com.trashslammers.model;

import java.util.List;

/**
 * The enclosures an animal can be sent to. Only the habitat already drawn on the
 * enclosure screen exists so far.
 */
public final class EnclosureCatalog {

    private static final List<PlacementTarget> TARGETS = List.of(
            new PlacementTarget("koala-habitat", "Koala Habitat", "Eucalypt Forest")
    );

    /**
     * Private constructor so the class can't be created, since every method is static.
     */
    private EnclosureCatalog() {}
    /**
     * Gets every enclosure an animal can be placed in.
     * @return A list of all enclosures
     */
    public static List<PlacementTarget> all() {
        return TARGETS;
    }
}
