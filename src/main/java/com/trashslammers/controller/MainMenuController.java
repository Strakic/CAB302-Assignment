package com.trashslammers.controller;

import com.trashslammers.service.Session;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import com.trashslammers.model.User;
import com.trashslammers.service.Session;
import com.trashslammers.util.SceneFactory;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Shape;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class MainMenuController {
    @FXML
    StackPane rootContainer;
    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        try {
            //Locate the login FXML view
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/login-view.fxml");
            Parent loginRoot = FXMLLoader.load(fxmlUrl);

            //Get the current Stage (window) from the clicked button
            //Swap the scene on the current stage
            SceneFactory.swap(event.getSource(), loginRoot, "Trash Slammers - Login");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load");
        }
    }

    @FXML
    private void handlePlayButtonClick(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/game-view.fxml");
            if (fxmlUrl == null) {
                System.err.println("Could not find game-view.fxml");
                return;
            }


            Parent gameRoot = FXMLLoader.load(fxmlUrl);


            Node sourceNode = (Node) event.getSource();
            Scene scene = sourceNode.getScene();
            double w = scene.getWidth() > 0 ? scene.getWidth() : 800;
            double h = scene.getHeight() > 0 ? scene.getHeight() : 600;

            // Setup full screen mask and central hole
            Rectangle fullScreenRect = new Rectangle(w, h, Color.BLACK);
            Circle hole = new Circle(w / 2.0, h / 2.0, 0);

            // create circle shape
            Shape irisOverlay = Shape.subtract(fullScreenRect, hole);
            irisOverlay.setFill(Color.BLACK);
            irisOverlay.setMouseTransparent(true);


            StackPane gameWrapper = new StackPane(gameRoot, irisOverlay);

            // swap the scene for the animation
            SceneFactory.swap(event.getSource(), gameWrapper, "Trash Slammers");

            // circle animates outward
            double maxRadius = Math.hypot(w, h);
            DoubleProperty radiusProp = new SimpleDoubleProperty(0);

            Timeline irisTimeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(3000),
                            new KeyValue(radiusProp, maxRadius, Interpolator.EASE_OUT)
                    )
            );


            radiusProp.addListener((obs, oldVal, newVal) -> {
                hole.setRadius(newVal.doubleValue());
                Shape updatedOverlay = Shape.subtract(fullScreenRect, hole);
                updatedOverlay.setFill(Color.BLACK);
                updatedOverlay.setMouseTransparent(true);

                gameWrapper.getChildren().set(1, updatedOverlay);
            });


            irisTimeline.setOnFinished(e -> gameWrapper.getChildren().remove(1));

            irisTimeline.play();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load game view: " + e.getMessage());
        }
    }

    @FXML
    private void handleOptionsButtonClick(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/options-view.fxml");

            if (fxmlUrl == null) {
                return;
            }

            Parent gameRoot = FXMLLoader.load(fxmlUrl);

            SceneFactory.swap(event.getSource(), gameRoot, "Trash Slammers - Options");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load game view: " + e.getMessage());
        }
    }
    @FXML
    private void handleExitButtonClick(ActionEvent event) {
        Platform.exit();
    }





    @FXML
    private Button playButton;

    @FXML
    private Text messageText;

    @FXML
    public void initialize() {
        if (playButton != null) {
            pulse(playButton);
        }
        if (titleText != null) {
            shimmer(titleText);
        } else {
            System.err.println("Warning: titleText is null. Check fx:id=\"titleText\" in FXML.");
        }
        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        if (messageText == null) {
            System.err.println("Warning: messageText is null");
            return;
        }

        User user = Session.getCurrentUser();
        messageText.setText(user == null ? "" : "Hello " + user.getDisplayName());
    }

    private void pulse(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(600), button);
        st.setFromX(1);
        st.setFromY(1.0);
        st.setToX(1.08);
        st.setToY(1.08);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
    }

    @FXML
    private Text titleText;

    private void shimmer(Text title) {
        Color baseColor = Color.BLACK; // normal title colour
        Color shimmerColor = Color.WHITE; // colour shimmering


        AnimationTimer timer = new AnimationTimer() {
            long startTime = -1;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;

                double elapsedSeconds = (now - startTime) / 1_000_000_000.0; // convert nanosecond to seconds

                // positioning
                double cycle = 2.0;
                double t = (elapsedSeconds % cycle) / cycle; // 0..1
                double bandCenter = -0.3 + t * 1.6; // ensures it comes from off the screen on the left to off the screen on the right

                Stop[] stops = new Stop[]{
                        new Stop(clamp(bandCenter - 0.15), baseColor),
                        new Stop(clamp(bandCenter), shimmerColor),
                        new Stop(clamp(bandCenter + 0.15), baseColor)
                };

                title.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops));
            }

            private double clamp(double v) {
                return Math.max(0, Math.min(1, v));
            }
        };

        timer.start();

    }




}