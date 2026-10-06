package com.trashslammers.controller;

import com.trashslammers.database.InMemoryAnimalRepository;
import com.trashslammers.model.Animal;
import com.trashslammers.model.PlayerSession;
import com.trashslammers.model.Score;
import com.trashslammers.model.gamestates.AnimalCatalog;
import com.trashslammers.service.AnimalShopService;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameControllerTest {

    private GameController gameController;
    private AnimalShopService shopService;
    private VBox mockContainer;

    private Animal penguin;
    private Animal turtle;

    @BeforeAll
    static void initJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {

        }
    }

    @BeforeEach
    void setUp() {
        Score score = new Score(5000);
        shopService = new AnimalShopService(score, new InMemoryAnimalRepository());
        PlayerSession.getInstance().setShopService(shopService);

        penguin = AnimalCatalog.findById("galapagos-penguin").orElseThrow();
        turtle = AnimalCatalog.findById("hawksbill-turtle").orElseThrow();

        gameController = new GameController();
        mockContainer = new VBox();
        gameController.setAnimalContainer(mockContainer);
    }

    @Test
    void sidebarIsEmptyWhenPlayerOwnsNoAnimals() {
        gameController.refreshAnimalList();

        assertEquals(0, mockContainer.getChildren().size());
    }

    @Test
    void sidebarPopulatesCardsFromPlayerSessionOwnedAnimals() {
        shopService.purchase(penguin);
        shopService.purchase(turtle);

        gameController.refreshAnimalList();

        assertEquals(2, mockContainer.getChildren().size());
    }

    @Test
    void refreshAnimalListClearsExistingCardsBeforeRepopulating() {
        shopService.purchase(penguin);
        gameController.refreshAnimalList();
        assertEquals(1, mockContainer.getChildren().size());

        gameController.refreshAnimalList();
        assertEquals(1, mockContainer.getChildren().size());
    }
}