package com.trashslammers.controller;

import com.trashslammers.util.SceneFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for the animal info screen, which shows the details of an animal in the enclosure.
 */
public class AnimalInfoController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label speciesLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label descriptionLabel;

    /**
     * Shows the given animal's details on the screen.
     * @param name The name of the animal
     * @param species The species of the animal
     * @param rating The star rating of the animal
     * @param description A short description of the animal
     */
    public void setAnimalData(String name, String species, String rating, String description) {
        nameLabel.setText(name);
        speciesLabel.setText(species);
        ratingLabel.setText(rating);
        descriptionLabel.setText(description);
    }

    /**
     * Returns to the enclosure screen when the Back button is clicked.
     * @param event The button click event
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/enclosure-view.fxml");
            Parent enclosureRoot = FXMLLoader.load(fxmlUrl);

            SceneFactory.swap(event.getSource(), enclosureRoot, "Trash Slammers - Enclosure");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load enclosure-view.fxml");
        }
    }
}