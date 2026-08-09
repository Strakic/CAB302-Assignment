package com.trashslammers.model;

public class TrashItem {

    public enum WasteType {
        RECYCLING("recycling.png"), // recycling bin
        CHEMICAL("chemical_waste.png"), //chemical waste bin
        GENERAL("general_waste.png"), //general waste bin
        GREEN("green_waste.png"); // green waste bin

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