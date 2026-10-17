package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.service.AnimalShopService;
/**
 * A pop-up opened from the animal shop that is given the animal the player clicked on.
 */
public interface AnimalDialogController {
    /**
     * Gives the pop-up the animal to show and the shop service to use.
     * @param animal The animal the player clicked on
     * @param shopService The shop service used to buy or place the animal
     */
    void setAnimal(Animal animal, AnimalShopService shopService);
}
