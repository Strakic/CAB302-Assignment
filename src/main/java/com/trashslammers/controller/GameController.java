package com.trashslammers.controller;

import com.trashslammers.service.DraggableMaker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Pane fallZone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // make sprite and immediately attach drag using spawn trash sprite method
        spawnTrashSprite("/com/trashslammers/Sprites/TrashSoda.png", 100, 100);
    }

    private void spawnTrashSprite(String path, double x, double y) {
        // use create sprite method to make a sprite.
        ImageView sprite = createSprite(path, x, y, 120, 120);
        // connect DraggableMaker directly to the new sprite
        DraggableMaker.makeDraggable(sprite);
        // add new sprite as child to fallzone
        fallZone.getChildren().add(sprite);
    }
    // find the image at image path and build up sprite
    private ImageView createSprite(String path, double x, double y, double width, double height) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Resource not found: " + path);
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
}