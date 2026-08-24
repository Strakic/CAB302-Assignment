package com.trashslammers.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EnclosureController {

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