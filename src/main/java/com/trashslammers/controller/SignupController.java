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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.trashslammers.model.User;
import com.trashslammers.service.Session;
import java.io.IOException;
import java.net.URL;


public class SignupController {

    private static final int MIN_PASSWORD_LENGTH = 8;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmpasswordField;
    @FXML private Text errorLabel;

    private final IAuthenticationService authenticationService;

    public SignupController() {
        this.authenticationService = new AuthenticationService();
    }

    @FXML
    public void initialize() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    @FXML
    private void goToMainMenu(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/main-menu-view.fxml");
            Parent mainMenuRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot, 800, 600));
            stage.setTitle("TrashSlammers");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load main menu");
        }
    }

    @FXML
    private void signup(ActionEvent event) {
        String username = usernameField.getText().trim();
        String rawPassword = passwordField.getText();
        String confirmPassword = confirmpasswordField.getText();

        if (username.isEmpty() || rawPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (rawPassword.length() < MIN_PASSWORD_LENGTH) {
            errorLabel.setText("Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
            return;
        }

        if (!rawPassword.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        try {


            User newUser = authenticationService.signUp(username, rawPassword);
            Session.setCurrentUser(newUser);
            goToMainMenu(event);

        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        } catch (Exception ex) {
            errorLabel.setText("error");
            ex.printStackTrace();
        }
    }
}