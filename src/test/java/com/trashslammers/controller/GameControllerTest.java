package com.trashslammers.controller;

import com.trashslammers.database.InMemoryAnimalRepository;
import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.PlayerSession;
import com.trashslammers.model.Rarity;
import com.trashslammers.model.Score;
import com.trashslammers.service.AnimalShopService;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameControllerTest {

    private GameController gameController;
    private AnimalShopService shopService;
    private VBox mockContainer;

    private static final Animal KOALA = new Animal("koala", "Koala", "Phascolarctos cinereus",
            Rarity.COMMON, 100, null, "Eucalypt Forest", "Sleeps up to 20 hours a day.");

    private static final Animal CROC = new Animal("croc", "Saltwater Crocodile", "Crocodylus porosus",
            Rarity.LEGENDARY, 3000, null, "Mangrove Wetland", "Can go months between meals.");

    @BeforeEach
    void setUp() {
        Score score = new Score(5000);
        shopService = new AnimalShopService(score, new InMemoryAnimalRepository());
        PlayerSession.getInstance().setShopService(shopService);

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
        shopService.purchase(KOALA);
        shopService.purchase(CROC);

        gameController.refreshAnimalList();

        assertEquals(2, mockContainer.getChildren().size());
    }

    @Test
    void refreshAnimalListClearsExistingCardsBeforeRepopulating() {
        shopService.purchase(KOALA);
        gameController.refreshAnimalList();
        assertEquals(1, mockContainer.getChildren().size());

        gameController.refreshAnimalList();
        assertEquals(1, mockContainer.getChildren().size());
    }
}