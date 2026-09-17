package com.trashslammers.model.gamestates;

import com.trashslammers.model.Animal;
import com.trashslammers.model.Rarity;

import java.util.List;
import java.util.Optional;

/**
 * The animals the shop offers. Kept as data rather than screen layout so animals
 * can be added or repriced without touching the shop view.
 */
public final class AnimalCatalog {

    private static final List<Animal> ANIMALS = List.of(
            new Animal("galapagos-penguin", "Galapagos Penguin", "Spheniscus mendiculus",
                    Rarity.COMMON, 100, null, "Galapagos Islands",
                    "One of the smallest penguins, they pant like dogs and stretch their flippers to stay cool."),

            new Animal("hawksbill-turtle", "Hawksbill Turtle", "Eretmochelys imbricata",
                    Rarity.UNCOMMON, 300, null, "Coral Reef",
                    "Named for their hawk-like beak, they eat sea sponges that are toxic to most other animals."),

            new Animal("orangutan", "Orangutan", "Pongo pygmaeus",
                    Rarity.RARE, 700, null, "Lowland Forest",
                    "The name orangutan means man of the forest in Malay, and they live mostly solitary lives."),

            new Animal("african-forest-elephant", "African Forest Elephant", "Loxodonta cyclotis",
                    Rarity.EPIC, 1600, null, "Rainforest",
                    "Nicknamed the mega-gardener of the forest because they spread so many tree seeds."),

            new Animal("blue-whale", "Blue Whale", "Balaenoptera musculus",
                    Rarity.LEGENDARY, 3750, null, "Open Ocean",
                    "The largest animal to have ever lived, a blue whale's heart can weigh as much as a small car.")
    );

    private AnimalCatalog() {}

    public static List<Animal> all() {
        return ANIMALS;
    }

    public static Optional<Animal> findById(String id) {
        return ANIMALS.stream()
                .filter(animal -> animal.getId().equals(id))
                .findFirst();
    }
}
