package com.trashslammers.model;

public class Enclosure {

    private final String name;
    private final String emoji;
    private final String subtitle;
    private final String fxmlPath;

    public Enclosure(String name, String emoji, String subtitle, String fxmlPath) {
        this.name = name;
        this.emoji = emoji;
        this.subtitle = subtitle;
        this.fxmlPath = fxmlPath;
    }

    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getSubtitle() { return subtitle; }
    public String getFxmlPath() { return fxmlPath; }
}