package com.trashslammers.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class GameController {

    @FXML private Pane fallZone;
    @FXML private VBox bucketOrganic;
    @FXML private VBox bucketGeneral;
    @FXML private VBox bucketRecycle;
    @FXML private Label scoreLabel;
    @FXML private Label difficultyLabel;
    @FXML private ProgressBar tierProgressBar;

    // stores all the trash shapes currently falling on screen
    ArrayList<Shape> trashList = new ArrayList<>();

    Random rand = new Random();
    int score = 0;

    // how many pixels the trash moves down every tick, keeps it a constant speed
    double fallSpeed = 2;

    // used when dragging so the shape doesn't jump to the mouse position
    double offsetX;
    double offsetY;
    boolean isDragging = false;

    Timeline gameLoop;
    Timeline spawner;

    @FXML
    public void initialize() {

        scoreLabel.setText("Score: " + score);

        // this makes new trash appear every 1.5 seconds
        spawner = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            spawnTrash();
        }));
        spawner.setCycleCount(Timeline.INDEFINITE);
        spawner.play();

        // this moves everything down, runs constantly
        gameLoop = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            moveTrashDown();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        spawnTrash();
    }

    public void moveTrashDown() {

        ArrayList<Shape> toDelete = new ArrayList<>();

        for (int i = 0; i < trashList.size(); i++) {
            Shape s = trashList.get(i);

            // if its being dragged dont move it automatically
            if (s.getProperties().get("dragging") == Boolean.TRUE) {
                continue;
            }

            s.setLayoutY(s.getLayoutY() + fallSpeed);

            // if it goes past the bottom of the fall zone just remove it
            if (s.getLayoutY() > fallZone.getHeight()) {
                toDelete.add(s);
            }
        }

        for (Shape s : toDelete) {
            trashList.remove(s);
            fallZone.getChildren().remove(s);
        }
    }

    public void spawnTrash() {

        // pick a random category, 0 = organic, 1 = general, 2 = recycle
        int type = rand.nextInt(3);

        Shape shape;

        // just alternate between circle and square for some variety
        if (rand.nextBoolean()) {
            shape = new Circle(17);
        } else {
            Rectangle r = new Rectangle(30, 30);
            r.setArcWidth(5);
            r.setArcHeight(5);
            shape = r;
        }

        shape.setStroke(Color.BLACK);
        shape.setStrokeType(StrokeType.INSIDE);

        if (type == 0) {
            shape.setFill(Color.web("#8dc63f"));
            shape.getProperties().put("category", "organic");
        } else if (type == 1) {
            shape.setFill(Color.web("#ef5b3b"));
            shape.getProperties().put("category", "general");
        } else {
            shape.setFill(Color.web("#ffc71f"));
            shape.getProperties().put("category", "recycle");
        }

        double zoneWidth = fallZone.getWidth();
        if (zoneWidth <= 0) {
            zoneWidth = 600; // fallback in case the pane hasnt loaded its size yet
        }

        double startX = 20 + rand.nextDouble() * (zoneWidth - 60);
        shape.setLayoutX(startX);
        shape.setLayoutY(-20);

        shape.getProperties().put("dragging", false);

        // mouse events for dragging
        shape.setOnMousePressed(e -> {
            shape.getProperties().put("dragging", true);
            shape.toFront();
            offsetX = e.getSceneX() - shape.getLayoutX();
            offsetY = e.getSceneY() - shape.getLayoutY();
        });

        shape.setOnMouseDragged(e -> {
            shape.setLayoutX(e.getSceneX() - offsetX);
            shape.setLayoutY(e.getSceneY() - offsetY);
        });

        shape.setOnMouseReleased(e -> {
            shape.getProperties().put("dragging", false);
            checkIfInBin(shape);
        });

        trashList.add(shape);
        fallZone.getChildren().add(shape);
    }

    public void checkIfInBin(Shape shape) {

        String category = (String) shape.getProperties().get("category");

        Bounds shapeBounds = shape.localToScene(shape.getBoundsInLocal());

        if (shapeBounds.intersects(bucketOrganic.localToScene(bucketOrganic.getBoundsInLocal()))) {
            if (category.equals("organic")) {
                score = score + 10;
            } else {
                score = score - 5;
            }
            scoreLabel.setText("Score: " + score);
            trashList.remove(shape);
            fallZone.getChildren().remove(shape);

        } else if (shapeBounds.intersects(bucketGeneral.localToScene(bucketGeneral.getBoundsInLocal()))) {
            if (category.equals("general")) {
                score = score + 10;
            } else {
                score = score - 5;
            }
            scoreLabel.setText("Score: " + score);
            trashList.remove(shape);
            fallZone.getChildren().remove(shape);

        } else if (shapeBounds.intersects(bucketRecycle.localToScene(bucketRecycle.getBoundsInLocal()))) {
            if (category.equals("recycle")) {
                score = score + 10;
            } else {
                score = score - 5;
            }
            scoreLabel.setText("Score: " + score);
            trashList.remove(shape);
            fallZone.getChildren().remove(shape);
        }
    }
}