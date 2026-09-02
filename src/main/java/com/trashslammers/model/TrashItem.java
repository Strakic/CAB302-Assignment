package com.trashslammers.model;

public class TrashItem {

    public enum WasteType {
        RECYCLING("Recycling Sprite.png"),
        CHEMICAL("Chemical waste Sprite.png"),
        GENERAL("General Waste Sprite.png"),
        GREEN("green waste Sprite.png");

        private final String fileName;

        WasteType(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private final String name;
    private final WasteType correctBin;

    public TrashItem(String name, WasteType correctBin) {
        this.name = name;
        this.correctBin = correctBin;
    }

    public String getName() {
        return name;
    }

    public WasteType getCorrectBin() {
        return correctBin;
    }
}