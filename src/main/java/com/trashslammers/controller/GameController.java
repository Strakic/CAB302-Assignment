package com.trashslammers.controller;

import com.trashslammers.model.Score;
import com.trashslammers.service.DraggableMaker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Pane fallZone;

    @FXML private VBox bucketOrganic;
    @FXML private VBox bucketGeneral;
    @FXML private VBox bucketRecycle;
    @FXML private Label scoreLabel;

    private final Score score = new Score();
    private List<Node> buckets;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // collect the buckets once so collision checks can loop over them
        buckets = List.of(bucketOrganic, bucketGeneral, bucketRecycle);

        // make sprite and immediately attach drag using spawn trash sprite method
        spawnTrashSprite("/com/trashslammers/Sprites/TrashSoda.png", 100, 100);

        updateScoreLabel();
    }

    private void spawnTrashSprite(String path, double x, double y) {
        // use create sprite method to make a sprite.
        ImageView sprite = createSprite(path, x, y, 120, 120);
        // connect DraggableMaker directly to the new sprite, and tell us when it's dropped
        DraggableMaker.makeDraggable(sprite, this::onDropped);
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

    // called by DraggableMaker when the user lets go of a sprite
    private void onDropped(Node trash) {
        if (bucketUnder(trash) == null) return;

        fallZone.getChildren().remove(trash);
        score.addForCorrectSort();
        updateScoreLabel();
    }

    // returns the bucket the sprite's centre is sitting in, or null if none
    private Node bucketUnder(Node trash) {
        Bounds t = trash.localToScene(trash.getBoundsInLocal());
        double cx = t.getCenterX();
        double cy = t.getCenterY();

        for (Node bucket : buckets) {
            Bounds b = bucket.localToScene(bucket.getBoundsInLocal());
            if (b.contains(cx, cy)) return bucket;
        }
        return null;
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score.getValue());
    }
}