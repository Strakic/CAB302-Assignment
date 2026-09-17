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

    private EnclosureCatalog() {}

    public static List<PlacementTarget> all() {
        return TARGETS;
    }
}
