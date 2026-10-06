package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.function.Consumer;

public class EnclosureCardController {

    @FXML private Label titleLabel;
    @FXML private Label statsLabel;
    @FXML private Button viewButton;
    @FXML private ImageView animalIcon;

    void setCardData(Animal animal, OwnedAnimal ownedAnimal, Consumer<OwnedAnimal> onViewAction) {
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