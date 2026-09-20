package com.trashslammers.model;

import java.util.List;

public interface IAnimalDAO {


    boolean addAnimal(Animal animal);

    boolean nameExists(String name);

    Animal getAnimcalByID(String id);

    List<Animal> getAllAnimals();

}
