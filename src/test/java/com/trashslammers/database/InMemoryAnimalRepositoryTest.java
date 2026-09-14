package com.trashslammers.database;

import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *  tests
 */
public class InMemoryAnimalRepositoryTest {

    private static final Animal KOALA = new Animal("koala", "Koala", "Phascolarctos cinereus",
            Rarity.COMMON, 100, null, "Eucalypt Forest", "Sleeps up to 20 hours a day.");

    private static final Animal PENGUIN = new Animal("little-penguin", "Little Penguin", "Eudyptula minor",
            Rarity.RARE, 850, null, "Rocky Coastline", "Only comes ashore after dark.");

    private AnimalRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAnimalRepository();
    }

    @Test
    void anEmptyCollectionOwnsNothing() {
        assertFalse(repository.isOwned("koala"));
        assertTrue(repository.findByAnimalId("koala").isEmpty());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void savedAnimalsCanBeFoundAgainById() {
        repository.save(new OwnedAnimal(KOALA));

        assertTrue(repository.isOwned("koala"));
        assertEquals("Koala", repository.findByAnimalId("koala").orElseThrow().getAnimal().getName());
    }

    @Test
    void findAllReturnsEverySavedAnimal() {
        repository.save(new OwnedAnimal(KOALA));
        repository.save(new OwnedAnimal(PENGUIN));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void savingTheSameAnimalAgainUpdatesItRatherThanDuplicating() {
        OwnedAnimal owned = new OwnedAnimal(KOALA);
        repository.save(owned);

        owned.placeIn("koala-habitat");
        repository.save(owned);

        assertEquals(1, repository.findAll().size());
        assertEquals("koala-habitat", repository.findByAnimalId("koala").orElseThrow().getEnclosureId());
    }
}
