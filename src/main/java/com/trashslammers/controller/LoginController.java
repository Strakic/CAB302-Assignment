package com.trashslammers.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void logIn() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Pass credentials to AuthenticationService here
    }
}