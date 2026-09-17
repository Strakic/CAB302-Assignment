package com.trashslammers.database;

import com.trashslammers.model.OwnedAnimal;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAnimalRepository implements AnimalRepository {

    private final Map<String, OwnedAnimal> ownedByAnimalId = new ConcurrentHashMap<>();

    @Override
    public List<OwnedAnimal> findAll() {
        return List.copyOf(ownedByAnimalId.values());
    }

    @Override
    public Optional<OwnedAnimal> findByAnimalId(String animalId) {
        return Optional.ofNullable(ownedByAnimalId.get(animalId));
    }

    @Override
    public boolean isOwned(String animalId) {
        return ownedByAnimalId.containsKey(animalId);
    }

    @Override
    public OwnedAnimal save(OwnedAnimal ownedAnimal) {
        ownedByAnimalId.put(ownedAnimal.getAnimalId(), ownedAnimal);
        return ownedAnimal;
    }
}
