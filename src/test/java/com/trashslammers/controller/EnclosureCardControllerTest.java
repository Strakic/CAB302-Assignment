package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class EnclosureCardControllerTest {

    private EnclosureCardController controller;
    private Animal sampleAnimal;
    private OwnedAnimal unplacedOwnedAnimal;
    private OwnedAnimal placedOwnedAnimal;

    @BeforeEach
    void setUp() {
        controller = new EnclosureCardController();

        sampleAnimal = new Animal(
                "koala",
                "Koala",
                "Phascolarctos cinereus",
                Rarity.COMMON,
                100,
                "/sprites/koala.png",
                "Eucalypt Forest",
                "Sleeps up to 20 hours a day."
        );

        unplacedOwnedAnimal = new OwnedAnimal(sampleAnimal);

        placedOwnedAnimal = new OwnedAnimal(sampleAnimal);
        placedOwnedAnimal.setEnclosureId("koala-sanctuary");
    }

    @Test
    void setCardDataPopulatesTitleWithAnimalName() {
        controller.setCardData(sampleAnimal, unplacedOwnedAnimal, null);

        assertEquals("Koala", controller.getTitleText());
    }

    @Test
    void setCardDataShowsHabitatWhenAnimalIsUnplaced() {
        controller.setCardData(sampleAnimal, unplacedOwnedAnimal, null);

        assertEquals("Habitat: Eucalypt Forest", controller.getStatsText());
    }

    @Test
    void setCardDataShowsEnclosureIdWhenAnimalIsPlaced() {
        controller.setCardData(sampleAnimal, placedOwnedAnimal, null);

        assertEquals("Enclosure: koala-sanctuary", controller.getStatsText());
    }

    @Test
    void viewActionTriggersCallbackWithCorrectOwnedAnimal() {
        AtomicBoolean actionTriggered = new AtomicBoolean(false);

        controller.setCardData(sampleAnimal, placedOwnedAnimal, target -> {
            actionTriggered.set(true);
            assertEquals("koala-sanctuary",
                    target.getEnclosureId());
        });

        // Simulates clicking the view action
        controller.handleViewAction();

        assertTrue(actionTriggered.get(), "Expected view action callback to be executed.");
    }

    @Test
    void nullActionCallbackDoesNotThrowException() {
        controller.setCardData(sampleAnimal, unplacedOwnedAnimal, null);

        assertDoesNotThrow(() -> controller.handleViewAction());
    }
}