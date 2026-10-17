package com.trashslammers.service;

import com.trashslammers.model.TrashItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * Loads trash sprites and picks random trash for the game.
 */
public class SpriteService {

    private final Random random = new Random();
    private static final String SPRITE_BASE_PATH = "/com/trashslammers/Sprites/";

    /**
     * Loads a sprite and places it at the given position.
     * @param fileName The file name of the sprite
     * @param x The x position of the sprite
     * @param y The y position of the sprite
     * @param width The width of the sprite
     * @param height The height of the sprite
     * @return The sprite, or an empty image view if the file can't be found
     */
    public ImageView createSprite(String fileName, double x, double y, double width, double height) {
        String fullPath = SPRITE_BASE_PATH + fileName;
        InputStream stream = getClass().getResourceAsStream(fullPath);

        if (stream == null) {
            System.err.println("Resource not found: " + fullPath);
            return new ImageView();
        }

        ImageView imageView = new ImageView(new Image(stream));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        return imageView;
    }

    /**
     * Picks a random trash item from a list.
     * @param availableTrash The trash items to pick from
     * @return A random trash item
     */
    public TrashItem getRandomTrashItem(List<TrashItem> availableTrash) {
        if (availableTrash == null || availableTrash.isEmpty()) {
            throw new IllegalArgumentException("Trash pool cannot be empty.");
        }
        return availableTrash.get(random.nextInt(availableTrash.size()));
    }
}