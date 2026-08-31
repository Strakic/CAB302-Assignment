package com.trashslammers.controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class EnclosureController {

    @FXML
    private VBox koala1;

    @FXML
    private VBox koala2;

    private final Random random = new Random();

    // Roughly the walkable area inside the enclosure border (330x330 pane,
    // koala node is 80 wide / 70 tall, keep clear of the pond/rock/gate)
    private static final double MIN_X = 10;
    private static final double MAX_X = 220;
    private static final double MIN_Y = 10;
    private static final double MAX_Y = 190;

    @FXML
    private void initialize() {
        setupTooltip(koala1, "Kai", "Australian Koala", "★★★☆☆");
        setupTooltip(koala2, "Willow", "Australian Koala", "★★★★☆");

        wander(koala1, 4.0);
        wander(koala2, 5.5);
    }

    /**
     * Attaches a hover tooltip to a koala node showing its name, species, and rating.
     */
    private void setupTooltip(VBox koala, String name, String species, String rating) {
        Tooltip tooltip = new Tooltip(name + " — " + species + " " + rating);
        tooltip.setShowDelay(Duration.millis(150));
        Tooltip.install(koala, tooltip);
    }

    /**
     * Repeatedly moves the given koala node to a random point inside the
     * enclosure, waiting `periodSeconds` between moves.
     */
    private void wander(VBox koala, double periodSeconds) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(periodSeconds), e -> moveToRandomSpot(koala))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void moveToRandomSpot(VBox koala) {
        double targetX = MIN_X + random.nextDouble() * (MAX_X - MIN_X);
        double targetY = MIN_Y + random.nextDouble() * (MAX_Y - MIN_Y);

        // translateX/Y are relative to the node's original layoutX/layoutY,
        // so we convert the absolute target into an offset.
        double deltaX = targetX - koala.getLayoutX();
        double deltaY = targetY - koala.getLayoutY();

        TranslateTransition move = new TranslateTransition(Duration.seconds(2.5), koala);
        move.setToX(deltaX);
        move.setToY(deltaY);
        move.setInterpolator(Interpolator.EASE_BOTH);
        move.play();
    }

    @FXML
    private void goBack(ActionEvent event) {
        navigateTo(event.getSource(), "/com/trashslammers/views/main-menu-view.fxml", "Trash Slammers", 800, 600);
    }

    @FXML
    private void handleKoala1Click(MouseEvent event) {
        openAnimalInfo(event.getSource(), "Kai", "Australian Koala", "★★★☆☆",
                "Kai is a laid-back koala who spends most of the day sleeping in the treetops.");
    }

    @FXML
    private void handleKoala2Click(MouseEvent event) {
        openAnimalInfo(event.getSource(), "Willow", "Australian Koala", "★★★★☆",
                "Willow is a curious koala who loves munching on fresh eucalyptus leaves.");
    }


    private void openAnimalInfo(Object source, String name, String species, String rating, String description) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/animal-info-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent infoRoot = loader.load();

            AnimalInfoController controller = loader.getController();
            controller.setAnimalData(name, species, rating, description);

            Stage stage = (Stage) ((Node) source).getScene().getWindow();
            stage.setScene(new Scene(infoRoot, 800, 600));
            stage.setWidth(800);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.setTitle("Trash Slammers - " + name);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load animal-info-view.fxml");
        }
    }

    private void navigateTo(Object source, String fxmlPath, String title, int width, int height) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            Parent root = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) source).getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setWidth(width);
            stage.setHeight(height);
            stage.centerOnScreen();
            stage.setTitle(title);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load " + fxmlPath);
        }
    }


}