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
    private void handlePlayButtonClick(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/game-view.fxml");
            Parent gameRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(gameRoot, 600, 400));
            stage.setTitle("Trash Slammers - Play");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load game-view.fxml");
        }
    }

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/login-view.fxml");
            Parent loginRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(loginRoot, 400, 300));
            stage.setTitle("TrashSlammers Login");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load");
        }
    }

    @FXML
    private void handleEnclosureButtonClick(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/enclosure-list-view.fxml");
            Parent enclosureListRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(enclosureListRoot, 800, 600));
            stage.setTitle("Trash Slammers - Enclosures");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load enclosure-list-view.fxml");
        }
    }
}