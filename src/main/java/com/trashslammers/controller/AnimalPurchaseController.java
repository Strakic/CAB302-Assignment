package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.service.AnimalShopService;
import com.trashslammers.service.PurchaseResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the pop-up that lets the player buy an animal with their points.
 */
public class AnimalPurchaseController implements AnimalDialogController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label speciesLabel;

    @FXML
    private Label rarityLabel;

    @FXML
    private Label habitatLabel;

    @FXML
    private Label costLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button buyButton;

    @FXML
    private Button closeButton;

    private Animal animal;
    private AnimalShopService shopService;

    /**
     * Shows the animal's details and cost, along with the player's points.
     * @param animal The animal to buy
     * @param shopService The shop service used to buy the animal
     */
    @Override
    public void setAnimal(Animal animal, AnimalShopService shopService) {
        this.animal = animal;
        this.shopService = shopService;

        nameLabel.setText(animal.getName());
        speciesLabel.setText(animal.getSpecies());
        rarityLabel.setText(animal.getRarity().getDisplayName());
        habitatLabel.setText("Habitat: " + animal.getHabitat());
        costLabel.setText("Cost: " + animal.getCost() + " pts");

        showBalance();
    }

    /**
     * Shows the player's points and disables the Buy button if they can't afford the animal.
     */
    private void showBalance() {
        int balance = shopService.getPointBalance();
        balanceLabel.setText("You have: " + balance + " pts");

        int shortfall = animal.getCost() - balance;
        if (shortfall > 0) {
            buyButton.setDisable(true);
            messageLabel.setText("You need " + shortfall + " more points. Sort more trash and come back.");
        }
    }

    /**
     * Tries to buy the animal when the Buy button is clicked and shows the result.
     * @param event The button click event
     */
    @FXML
    private void handleBuyButtonClick(ActionEvent event) {
        PurchaseResult result = shopService.purchase(animal);

        switch (result) {
            case SUCCESS -> showUnlocked();
            case INSUFFICIENT_POINTS -> messageLabel.setText("Not enough points for " + animal.getName() + " yet.");
            case ALREADY_OWNED -> messageLabel.setText("You already own " + animal.getName() + ".");
        }
    }

    /**
     * Updates the pop-up after the animal is bought, showing the new balance and the animal's fact.
     */
    private void showUnlocked() {
        balanceLabel.setText("You have: " + shopService.getPointBalance() + " pts");
        messageLabel.setText("Unlocked! " + animal.getFact());

        buyButton.setText("OWNED");
        buyButton.setDisable(true);
        closeButton.setText("BACK TO SHOP");
    }

    /**
     * Closes the pop-up when the Close button is clicked.
     * @param event The button click event
     */
    @FXML
    private void handleCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
