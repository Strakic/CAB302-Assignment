package com.trashslammers.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
            System.err.println("Couldn't load");
        }
    }

    @FXML
    private void handlePlayButtonClick(ActionEvent event) {
        try {
            //Locate the login FXML view
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/game-view.fxml");
            Parent GameRoot = FXMLLoader.load(fxmlUrl);

            //Get the current Stage (window) from the clicked button
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Swap the scene on the current stage
            stage.setScene(new Scene(GameRoot, 600, 500));
            stage.setTitle("TrashSlammers");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't load");
        }
    }
}