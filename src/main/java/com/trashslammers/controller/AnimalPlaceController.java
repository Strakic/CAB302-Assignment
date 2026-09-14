package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.model.EnclosureCatalog;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.PlacementTarget;
import com.trashslammers.service.AnimalShopService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Optional;

public class AnimalPlaceController implements AnimalDialogController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label factLabel;

    @FXML
    private ComboBox<PlacementTarget> enclosureComboBox;

    @FXML
    private Label messageLabel;

    @FXML
    private Button placeButton;

    @FXML
    private Button closeButton;

    private Animal animal;
    private AnimalShopService shopService;

    @Override
    public void setAnimal(Animal animal, AnimalShopService shopService) {
        this.animal = animal;
        this.shopService = shopService;

        nameLabel.setText(animal.getName());
        factLabel.setText(animal.getFact());

        enclosureComboBox.getItems().setAll(EnclosureCatalog.all());
        enclosureComboBox.setValue(EnclosureCatalog.all().isEmpty() ? null : EnclosureCatalog.all().get(0));

        showCurrentPlacement();
    }

    private void showCurrentPlacement() {
        Optional<OwnedAnimal> owned = shopService.findOwned(animal.getId());
        if (owned.isEmpty()) {
            messageLabel.setText("Buy " + animal.getName() + " before placing it.");
            placeButton.setDisable(true);
            return;
        }

        OwnedAnimal ownedAnimal = owned.get();
        if (ownedAnimal.isPlaced()) {
            messageLabel.setText(animal.getName() + " already lives in "
                    + nameOf(ownedAnimal.getEnclosureId()) + ". Placing again moves them.");
            placeButton.setText("MOVE HERE");
        } else {
            messageLabel.setText(animal.getName() + " is waiting to be placed.");
        }
    }

    @FXML
    private void handlePlaceButtonClick(ActionEvent event) {
        PlacementTarget target = enclosureComboBox.getValue();
        if (target == null) {
            messageLabel.setText("Choose an enclosure first.");
            return;
        }

        if (!shopService.placeInEnclosure(animal.getId(), target.getId())) {
            messageLabel.setText("Could not place " + animal.getName() + " right now.");
            return;
        }

        messageLabel.setText(animal.getName() + " has moved into " + target.getDisplayName() + ".");
        placeButton.setDisable(true);
        closeButton.setText("BACK TO SHOP");
    }

    private String nameOf(String enclosureId) {
        return EnclosureCatalog.all().stream()
                .filter(target -> target.getId().equals(enclosureId))
                .map(PlacementTarget::getDisplayName)
                .findFirst()
                .orElse(enclosureId);
    }

    @FXML
    private void handleCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
