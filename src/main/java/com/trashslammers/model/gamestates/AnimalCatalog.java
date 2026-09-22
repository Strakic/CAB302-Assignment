package com.trashslammers.model.gamestates;

import com.trashslammers.model.Animal;
import com.trashslammers.model.AnimalDAO;
import com.trashslammers.model.IAnimalDAO;
import com.trashslammers.model.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

/**
 * The animals the shop offers. Kept as data rather than screen layout so animals
 * can be added or repriced without touching the shop view.
 */
public final class AnimalCatalog {

    private static final List<Animal> BUILT_IN = List.of(
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

    private static IAnimalDAO customAnimals;

    private AnimalCatalog() {}

    private static IAnimalDAO customAnimals() {
        if (customAnimals == null) {
            customAnimals = new AnimalDAO();
        }
        return customAnimals;
    }

    static void setCustomAnimalSource(IAnimalDAO source) {
        customAnimals = source;
    }

    /** ordered by the built in animals first followed by the admin added animals after */
    public static List<Animal> all() {
        List<Animal> animals = new ArrayList<>(BUILT_IN);
        animals.addAll(customAnimals().getAllAnimals());
        animals.sort(Comparator.comparingInt(Animal::getCost).thenComparing(Animal::getName));
        return List.copyOf(animals);
    }

    public static Optional<Animal> findById(String id) {
        return all().stream()
                .filter(animal -> animal.getId().equals(id))
                .findFirst();
    }

    public static boolean nameTaken(String name) {
        return all().stream()
                .anyMatch(animal -> animal.getName().equalsIgnoreCase(name.trim()));
    }

    /** @return true if saved. Rejects an id that is already in use. */
    public static boolean add(Animal animal) {
        if (findById(animal.getId()).isPresent()) {
            return false;
        }
        return customAnimals().addAnimal(animal);
    }
}
