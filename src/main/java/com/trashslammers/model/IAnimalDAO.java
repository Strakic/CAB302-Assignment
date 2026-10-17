package com.trashslammers.model;

import java.util.List;
/**
 * Saves and loads the animals an admin adds to the shop.
 */
public interface IAnimalDAO {


    boolean addAnimal(Animal animal);

    boolean nameExists(String name);

    Animal getAnimalById(String id);

    List<Animal> getAllAnimals();

}
