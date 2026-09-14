package com.trashslammers.service;

import com.trashslammers.database.AnimalRepository;
import com.trashslammers.model.Animal;
import com.trashslammers.model.AnimalCatalog;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.Score;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Buying animals and putting them in enclosures. Holds the shop rules so the
 * screens only have to draw what it reports.
 */
public class AnimalShopService {

    private final Score score;
    private final AnimalRepository collection;

    public AnimalShopService(Score score, AnimalRepository collection) {
        if (score == null || collection == null) {
            throw new IllegalArgumentException("Shop service needs both a score and a collection");
        }
        this.score = score;
        this.collection = collection;
    }

    public PurchaseResult purchase(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot purchase a null animal");
        }
        if (collection.isOwned(animal.getId())) {
            return PurchaseResult.ALREADY_OWNED;
        }
        if (score.getValue() < animal.getCost()) {
            return PurchaseResult.INSUFFICIENT_POINTS;
        }

        score.spendOnAnimal(animal.getCost());
        collection.save(new OwnedAnimal(animal));
        return PurchaseResult.SUCCESS;
    }

    /**
     * Records that an owned animal now lives in the given enclosure.
     *
     * @return false if the animal is not owned, or the enclosure id is blank
     */
    public boolean placeInEnclosure(String animalId, String enclosureId) {
        if (enclosureId == null || enclosureId.isBlank()) {
            return false;
        }

        Optional<OwnedAnimal> owned = collection.findByAnimalId(animalId);
        if (owned.isEmpty()) {
            return false;
        }

        OwnedAnimal ownedAnimal = owned.get();
        ownedAnimal.placeIn(enclosureId);
        collection.save(ownedAnimal);
        return true;
    }

    public List<Animal> listCatalog(SortMode sortMode, OwnershipFilter filter) {
        Stream<Animal> animals = AnimalCatalog.all().stream()
                .filter(animal -> filter.matches(isOwned(animal.getId())));

        Comparator<Animal> ordering = sortMode.comparator();
        if (ordering != null) {
            animals = animals.sorted(ordering);
        }

        return animals.toList();
    }

    public boolean isOwned(String animalId) {
        return collection.isOwned(animalId);
    }

    public Optional<OwnedAnimal> findOwned(String animalId) {
        return collection.findByAnimalId(animalId);
    }

    public List<OwnedAnimal> ownedAnimals() {
        return collection.findAll();
    }

    public List<OwnedAnimal> placedAnimals() {
        return collection.findAll().stream()
                .filter(OwnedAnimal::isPlaced)
                .toList();
    }

    public int getPointBalance() {
        return score.getValue();
    }
}
