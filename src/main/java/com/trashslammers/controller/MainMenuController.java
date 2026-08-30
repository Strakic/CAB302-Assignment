package com.trashslammers.controller;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.io.IOException;
import java.net.URL;

public class MainMenuController {

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        try {
            //Locate the login FXML view
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/login-view.fxml");
            Parent loginRoot = FXMLLoader.load(fxmlUrl);

            //Get the current Stage (window) from the clicked button
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Swap the scene on the current stage
            stage.setScene(new Scene(loginRoot, 400, 300));
            stage.setTitle("TrashSlammers Login");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load");
        }
    }

    @FXML
    private Button playButton;

    @FXML
    public void initialize(){
        pulse(playButton);
        shimmer(titleText);
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