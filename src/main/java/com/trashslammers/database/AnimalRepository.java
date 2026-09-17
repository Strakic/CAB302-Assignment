package com.trashslammers.database;

import com.trashslammers.model.OwnedAnimal;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository {

    List<OwnedAnimal> findAll();

    Optional<OwnedAnimal> findByAnimalId(String animalId);

    boolean isOwned(String animalId);

    OwnedAnimal save(OwnedAnimal ownedAnimal);
}
