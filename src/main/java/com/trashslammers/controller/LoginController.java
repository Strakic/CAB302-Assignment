package com.trashslammers.controller;

import com.trashslammers.service.AuthenticationService;
import com.trashslammers.service.IAuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final IAuthenticationService authenticationService = new AuthenticationService();

    @FXML
    private void logIn(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            showError("Please enter a username and password.");
            return;
        }

        boolean success = authenticationService.logIn(username, password);

        if (!success) {
            showError("Incorrect username or password.");
            return;
        }

        clearError();
        goToMainMenu(event);
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        }

    }

    private void clearError() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    private void goToMainMenu(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/main-menu-view.fxml");
            Parent mainMenuRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot, 400, 300));
            stage.setTitle("TrashSlammers");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load main menu");
        }
    }
}
