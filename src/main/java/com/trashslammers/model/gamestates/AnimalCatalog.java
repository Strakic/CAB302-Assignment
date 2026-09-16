package com.trashslammers.model.gamestates;

import com.trashslammers.model.Animal;
import java.util.List;
import java.util.Optional;

public class AnimalCatalog {

    public static List<Animal> all() {
        return List.of();
    }

    public static Optional<Animal> findById(String id) {
        return Optional.empty();
    }
}