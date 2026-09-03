package com.trashslammers.controller;

import org.mindrot.jbcrypt.BCrypt;

import com.trashslammers.service.AuthenticationService;
import com.trashslammers.service.IAuthenticationService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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

            String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

            authenticationService.signUp(username, hashedPassword);
            errorLabel.setText("Sign up worked!");

        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        } catch (Exception ex) {
            errorLabel.setText("error");
            ex.printStackTrace();
        }
    }
}