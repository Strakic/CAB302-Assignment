package com.trashslammers.services;

import com.trashslammers.database.AnimalRepository;
import com.trashslammers.database.InMemoryAnimalRepository;
import com.trashslammers.model.Animal;
import com.trashslammers.model.gamestates.AnimalCatalog;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.Rarity;
import com.trashslammers.model.Score;
import com.trashslammers.service.AnimalShopService;
import com.trashslammers.service.OwnershipFilter;
import com.trashslammers.service.PurchaseResult;
import com.trashslammers.service.SortMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Animal shop behaviour tests
 */
public class AnimalShopServiceTest {

    private static final Animal KOALA = new Animal("koala", "Koala", "Phascolarctos cinereus",
            Rarity.COMMON, 100, null, "Eucalypt Forest", "Sleeps up to 20 hours a day.");

    private static final Animal CROC = new Animal("croc", "Saltwater Crocodile", "Crocodylus porosus",
            Rarity.LEGENDARY, 3000, null, "Mangrove Wetland", "Can go months between meals.");

    private Score score;
    private AnimalRepository collection;
    private AnimalShopService shopService;

    @BeforeEach
    void setUp() {
        score = new Score(500);
        collection = new InMemoryAnimalRepository();
        shopService = new AnimalShopService(score, collection);
    }

    @Test
    void purchaseDeductsPointsAndUnlocksTheAnimal() {
        assertEquals(PurchaseResult.SUCCESS, shopService.purchase(KOALA));

        assertEquals(400, score.getValue());
        assertTrue(shopService.isOwned("koala"));
    }

    @Test
    void purchaseWithTooFewPointsChangesNothing() {
        assertEquals(PurchaseResult.INSUFFICIENT_POINTS, shopService.purchase(CROC));

        assertEquals(500, score.getValue());
        assertFalse(shopService.isOwned("croc"));
    }

    @Test
    void buyingTheSameAnimalTwiceOnlyCostsOnce() {
        shopService.purchase(KOALA);

        assertEquals(PurchaseResult.ALREADY_OWNED, shopService.purchase(KOALA));
        assertEquals(400, score.getValue());
        assertEquals(1, shopService.ownedAnimals().size());
    }

    @Test
    void purchaseRejectsANullAnimal() {
        assertThrows(IllegalArgumentException.class, () -> shopService.purchase(null));
    }

    @Test
    void placingAnOwnedAnimalRecordsItsEnclosure() {
        shopService.purchase(KOALA);

        assertTrue(shopService.placeInEnclosure("koala", "koala-habitat"));

        OwnedAnimal owned = shopService.findOwned("koala").orElseThrow();
        assertTrue(owned.isPlaced());
        assertEquals("koala-habitat", owned.getEnclosureId());
    }

    @Test
    void cannotPlaceAnAnimalThatIsNotOwned() {
        assertFalse(shopService.placeInEnclosure("koala", "koala-habitat"));
    }

    @Test
    void cannotPlaceIntoABlankEnclosure() {
        shopService.purchase(KOALA);

        assertFalse(shopService.placeInEnclosure("koala", " "));
        assertFalse(shopService.findOwned("koala").orElseThrow().isPlaced());
    }

    @Test
    void placedAnimalsExcludesOnesStillWaiting() {
        score = new Score(5000);
        shopService = new AnimalShopService(score, collection);
        shopService.purchase(KOALA);
        shopService.purchase(CROC);
        shopService.placeInEnclosure("croc", "mangrove");

        List<OwnedAnimal> placed = shopService.placedAnimals();

        assertEquals(1, placed.size());
        assertEquals("croc", placed.get(0).getAnimalId());
    }

    @Test
    void listCatalogCanShowOnlyUnownedAnimals() {
        Animal penguin = AnimalCatalog.findById("galapagos-penguin").orElseThrow();
        int catalogSize = shopService.listCatalog(SortMode.CATALOG_ORDER, OwnershipFilter.ALL).size();

        shopService.purchase(penguin);

        List<Animal> unowned = shopService.listCatalog(SortMode.CATALOG_ORDER, OwnershipFilter.UNOWNED);
        List<Animal> owned = shopService.listCatalog(SortMode.CATALOG_ORDER, OwnershipFilter.OWNED);

        assertEquals(catalogSize - 1, unowned.size());
        assertEquals(1, owned.size());
        assertEquals("galapagos-penguin", owned.get(0).getId());
    }

    @Test
    void listCatalogSortsFromCommonToLegendary() {
        List<Animal> sorted = shopService.listCatalog(SortMode.RARITY_LOW_TO_HIGH, OwnershipFilter.ALL);

        for (int i = 1; i < sorted.size(); i++) {
            Rarity previous = sorted.get(i - 1).getRarity();
            Rarity current = sorted.get(i).getRarity();
            assertTrue(previous.compareTo(current) <= 0,
                    "Expected " + previous + " to come before " + current);
        }
    }

    @Test
    void pointBalanceFollowsTheScore() {
        assertEquals(500, shopService.getPointBalance());

        shopService.purchase(KOALA);

        assertEquals(400, shopService.getPointBalance());
    }
}
