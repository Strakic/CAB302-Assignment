package com.trashslammers.controller;


import com.trashslammers.model.Animal;
import com.trashslammers.model.Rarity;
import com.trashslammers.model.gamestates.AnimalCatalog;
import com.trashslammers.model.User;
import com.trashslammers.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import java.io.IOException;
import java.util.UUID;

/**
 * Controller for the Add Animal screen, where an admin can add a new animal to the shop.
 */

public class AddAnimalController {

    @FXML
    private TextField nameText;

    @FXML
    private TextField speciesText;

    @FXML
    private ComboBox<Rarity> rarityBox;

    @FXML
    private TextField costText;

    @FXML
    private TextField habitatText;

    @FXML
    private TextArea factArea;

    @FXML
    private Text statusText;

    /**
     * Fills the rarity drop-down with every rarity when the screen loads.
     */
    @FXML
    public void initialize(){

        rarityBox.getItems().setAll(Rarity.values());

        rarityBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Rarity rarity) {
                return rarity == null ? "" : rarity.getDisplayName();
            }

            @Override
            public Rarity fromString(String text) {
                // the box is not editable so this will never be called
                return null;
            }
        });

    }


    /**
     * Checks the form and, if everything is valid, saves the new animal to the catalog.
     */
    @FXML
    private void handleAddAnimal() {
        // Re-check here, not just when showing the screen, same as OptionsController
        User user = Session.getCurrentUser();
        if (user == null || !user.canManageAnimals()) {
            showError("Only admins can add animals");
            return;
        }

        String name = nameText.getText().trim();
        String species = speciesText.getText().trim();
        String habitat = habitatText.getText().trim();
        String fact = factArea.getText().trim();
        Rarity rarity = rarityBox.getValue();

        if (name.isEmpty()) {
            showError("Enter a name");
            return;
        }
        if (rarity == null) {
            showError("Choose a rarity");
            return;
        }

        int cost;
        try {
            cost = Integer.parseInt(costText.getText().trim());
        } catch (NumberFormatException e) {
            showError("Cost must be a whole number");
            return;
        }
        if (cost < 0) {
            showError("Cost cannot be negative");
            return;
        }
        if (AnimalCatalog.nameTaken(name)) {   // checks built-in animals too
            showError("An animal called " + name + " already exists");
            return;
        }

        Animal animal;
        try {
            animal = new Animal(makeId(name), name, species, rarity, cost,
                    null, habitat, fact);   // no sprite yet, same as the built-in animals
        } catch (IllegalArgumentException e) {
            showError(e.getMessage()); // Animal's own validation is the final safety net
            return;
        }

        if (AnimalCatalog.add(animal)) {
            showSuccess(name + " added to the shop");
            clearForm();
        } else {
            showError("Could not save the animal. Try again.");
        }
    }

    /**
     * Returns to the options screen when the Back button is clicked.
     * @param event The button click event
     * @throws IOException If the options view can't be loaded
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        // ASSUMPTION: the admin reached this screen from the options view
        Parent root = FXMLLoader.load(getClass().getResource("/com/trashslammers/views/options-view.fxml"));
        ((Node) event.getSource()).getScene().setRoot(root);
    }


    /**
     * Turns an animal's name into an id, for example {@code "Red Panda"} becomes {@code "red-panda"}.
     * @param name The name of the animal
     * @return The id for the animal, or a random UUID if the name has no letters or digits
     */
    private static String makeId(String name) {
        String slug = name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
        return slug.isEmpty() ? UUID.randomUUID().toString() : slug;
    }


    /**
     * Clears every field on the form, ready for the next animal.
     */
    private void clearForm() {
        nameText.clear();
        speciesText.clear();
        costText.clear();
        habitatText.clear();
        factArea.clear();
        rarityBox.setValue(null);
    }

    /**
     * Shows an error message in red.
     * @param message The error message to show
     */
    private void showError(String message) {
        statusText.setFill(Color.web("#c62828"));
        statusText.setText(message);
    }

    /**
     * Shows a success message in green.
     * @param message The success message to show
     */
    private void showSuccess(String message) {
        statusText.setFill(Color.web("#2e7d32"));
        statusText.setText(message);
    }









}
