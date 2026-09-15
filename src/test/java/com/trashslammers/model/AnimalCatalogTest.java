package com.trashslammers.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Animal catalog tests
 */
class AnimalCatalogTest {

    @Test
    void catalogIsNotEmpty() {
        assertFalse(AnimalCatalog.all().isEmpty());
    }

    @Test
    void everyAnimalHasAUniqueId() {
        Set<String> seen = new HashSet<>();

        for (Animal animal : AnimalCatalog.all()) {
            assertTrue(seen.add(animal.getId()), "Duplicate animal id: " + animal.getId());
        }
    }

    @Test
    void animalsCanBeLookedUpById() {
        String knownId = AnimalCatalog.all().get(0).getId();

        assertTrue(AnimalCatalog.findById(knownId).isPresent());
        assertTrue(AnimalCatalog.findById("not-a-real-animal").isEmpty());
    }
}
