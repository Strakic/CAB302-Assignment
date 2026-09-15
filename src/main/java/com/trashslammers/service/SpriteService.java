package com.trashslammers.service;

import com.trashslammers.model.TrashItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class SpriteService {

    private final Random random = new Random();
    private static final String SPRITE_BASE_PATH = "/com/trashslammers/Sprites/";

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

    public TrashItem getRandomTrashItem(List<TrashItem> availableTrash) {
        if (availableTrash == null || availableTrash.isEmpty()) {
            throw new IllegalArgumentException("Trash pool cannot be empty.");
        }
        return availableTrash.get(random.nextInt(availableTrash.size()));
    }
}