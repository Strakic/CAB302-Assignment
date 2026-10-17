package com.trashslammers.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads bin sprites and caches them so each one is only loaded once.
 */
public final class SpriteLoader {

    private static final Map<String, Image> CACHE = new HashMap<>();
    private static final String PATH = "/com/trashslammers/Sprites/Bin Sprites/";

    private SpriteLoader() {}

    /**
     * Gets a bin sprite, loading it the first time it's needed.
     * @param fileName The file name of the sprite
     * @return The sprite image
     */
    public static Image load(String fileName) {
        return CACHE.computeIfAbsent(fileName, f -> {
            InputStream in = SpriteLoader.class.getResourceAsStream(PATH + f);
            if (in == null) {
                throw new IllegalStateException("Missing sprite: " + PATH + f);
            }
            return new Image(in);
        });
    }
}