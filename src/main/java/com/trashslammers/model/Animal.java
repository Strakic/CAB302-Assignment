package com.trashslammers.model;

public class Animal {
    private final String name;
    private final int cost;
    private final String spriteFile;

    public Animal(String name, int cost, String spriteFile) {
        this.name = name;
        this.cost = cost;
        this.spriteFile = spriteFile;
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public String getSpriteFile() { return spriteFile; }
}
