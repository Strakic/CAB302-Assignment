package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.function.Consumer;

public class EnclosureCardController {

    @FXML private Label titleLabel;
    @FXML private Label statsLabel;
    @FXML private Button viewButton;
    @FXML private ImageView animalIcon;

    private String titleText;
    private String statsText;
    private OwnedAnimal currentOwnedAnimal;
    private Consumer<OwnedAnimal> onViewAction;

    void setCardData(Animal animal, OwnedAnimal ownedAnimal, Consumer<OwnedAnimal> onViewAction) {
        if (animal == null || ownedAnimal == null) {
            return;
        }

        this.currentOwnedAnimal = ownedAnimal;
        this.onViewAction = onViewAction;

        this.titleText = animal.getName();
        if (ownedAnimal.isPlaced()) {
            this.statsText = "Enclosure: " + ownedAnimal.getEnclosureId();
        } else {
            String habitat = animal.getHabitat() != null ? animal.getHabitat() : "Unassigned";
            this.statsText = "Habitat: " + habitat;
        }

        if (titleLabel != null) {
            titleLabel.setText(this.titleText);
        }
        if (statsLabel != null) {
            statsLabel.setText(this.statsText);
        }

        if (viewButton != null) {
            viewButton.setOnAction(e -> handleViewAction());
        }

        if (animalIcon != null && animal.getSpriteFile() != null && !animal.getSpriteFile().isBlank()) {
            try {
                animalIcon.setImage(new Image(getClass().getResourceAsStream(animal.getSpriteFile())));
            } catch (Exception ignored) {

            }
        }
    }

    String getTitleText() {
        return null;
    }

    String getStatsText() {
        return null;
    }

    void handleViewAction() {
    }
}