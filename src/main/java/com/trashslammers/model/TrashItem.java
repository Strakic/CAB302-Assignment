package com.trashslammers.model;

/**
 * A type of trash in the game and the bin it should be sorted into.
 */
public class TrashItem {


    public enum WasteType {
        RECYCLING("Recycling Sprite.png"),
        CHEMICAL("Chemical waste Sprite.png"),
        GENERAL("General Waste Sprite.png"),
        GREEN("green waste Sprite.png");

        private final String fileName;

        /**
         * Constructs a new WasteType with the specified sprite file.
         * @param fileName The file name of the bin's sprite
         */
        WasteType(String fileName) {
            this.fileName = fileName;
        }

        /**
         * Gets the file name of the bin's sprite.
         * @return The sprite file name
         */
        public String getFileName() {
            return fileName;
        }
    }

    private final String name;
    private final WasteType correctBin;

    /**
     * Constructs a new TrashItem with the specified sprite and correct bin.
     * @param name The file name of the trash's sprite
     * @param correctBin The bin the trash should be sorted into
     */
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