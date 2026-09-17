package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.service.AnimalShopService;

public interface AnimalDialogController {

    void setAnimal(Animal animal, AnimalShopService shopService);
}
