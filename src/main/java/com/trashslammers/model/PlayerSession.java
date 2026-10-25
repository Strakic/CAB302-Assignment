package com.trashslammers.model;

import com.trashslammers.database.AnimalRepository;
import com.trashslammers.database.InMemoryAnimalRepository;
import com.trashslammers.service.AnimalShopService;

public final class PlayerSession {

    private static PlayerSession instance;

    private final Score score;
    private final AnimalRepository collection;
    private AnimalShopService shopService;

    private PlayerSession() {
        this.score = new Score();
        this.collection = new InMemoryAnimalRepository();
        this.shopService = new AnimalShopService(score, collection);
    }

    public static PlayerSession getInstance() {
        if (instance == null) {
            instance = new PlayerSession();
        }
        return instance;
    }

    public Score getScore() {
        return score;
    }

    public AnimalShopService getShopService() {
        return shopService;
    }

    public void setShopService(AnimalShopService shopService) {
        this.shopService = shopService;
    }
}