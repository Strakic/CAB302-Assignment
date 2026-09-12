package com.trashslammers.controller;

import com.trashslammers.model.User;
import com.trashslammers.model.usertype.Role;
import com.trashslammers.service.AuthenticationService;
import com.trashslammers.service.IAuthenticationService;
import com.trashslammers.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * options screen
 * Shows the ADD ANIMAL button only to admins, and lets a standard user
 * upgrade themselves to premium
 */
public class OptionsController {

    @FXML private Button addAnimalButton;
    @FXML private CheckBox premiumCheckbox;
    @FXML private Button backButton;

    private final IAuthenticationService authenticationService = new AuthenticationService();

    @FXML
    public void initialize() {
        User user = Session.getCurrentUser();

        boolean canManageAnimals = user != null && user.canManageAnimals();

        addAnimalButton.setVisible(canManageAnimals);
        addAnimalButton.setManaged(canManageAnimals);

        boolean isStandard = user != null && user.getRole() == Role.STANDARD;
        premiumCheckbox.setSelected(user != null && user.getRole() == Role.PREMIUM);

        premiumCheckbox.setDisable(!isStandard);
    }

    @FXML
    private void handleActivatePremium(ActionEvent event) {
        if (!premiumCheckbox.isSelected()) {
            return;
        }

        try {
            User upgraded = authenticationService.upgradeToPremium(Session.getCurrentUser());
            Session.setCurrentUser(upgraded);
            premiumCheckbox.setDisable(true);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            premiumCheckbox.setSelected(false);
        }
    }

    @FXML
    private void handleAddAnimal(ActionEvent event) {
        User user = Session.getCurrentUser();

        // ensures that only an admin can add animals

        if (user == null || !user.canManageAnimals()) {
            System.err.println("Blocked: only an admin can add animals.");
            return;
        }

        try {
            //Locate the login FXML view
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/admin-view.fxml");
            Parent loginRoot = FXMLLoader.load(fxmlUrl);

            //Get the current Stage (window) from the clicked button
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Swap the scene on the current stage
            stage.setScene(new Scene(loginRoot, 800, 600));
            stage.setTitle("TrashSlammers - add animal");

        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load");
        }
    }



    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, "/com/trashslammers/views/main-menu-view.fxml", "Trash Slammers");
    }

    private void navigateTo(ActionEvent event, String fxmlPath, String title) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("View not found: " + fxmlPath);
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setWidth(800);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.setTitle(title);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load " + fxmlPath);
        }
    }
}