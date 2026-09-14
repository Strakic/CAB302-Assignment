package com.trashslammers.model;

import com.trashslammers.database.AnimalRepository;
import com.trashslammers.database.InMemoryAnimalRepository;
import com.trashslammers.service.AnimalShopService;

/**
 * The score and animal collection belonging to whoever is playing  now.
 * Screens share one instance so points earned in the game can be spent in the shop.
 */
public final class PlayerSession {

    private static PlayerSession instance;

    private final Score score;
    private final AnimalRepository collection;
    private final AnimalShopService shopService;

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
}
