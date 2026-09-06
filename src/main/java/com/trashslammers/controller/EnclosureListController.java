package com.trashslammers.controller;

import com.trashslammers.model.Enclosure;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class EnclosureListController {

    @FXML
    private FlowPane enclosureGrid;

    // Add new enclosures here as you build them — everything else updates automatically.
    private final List<Enclosure> enclosures = Arrays.asList(
            new Enclosure("Kai & Willow", "🐨", "Koala Habitat", "/com/trashslammers/views/enclosure-view.fxml")
    );

    @FXML
    private void initialize() {
        for (Enclosure enclosure : enclosures) {
            enclosureGrid.getChildren().add(buildCard(enclosure));
        }
    }

    private VBox buildCard(Enclosure enclosure) {
        VBox card = new VBox(6);
        card.setPrefSize(180, 160);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setStyle(
                "-fx-background-color: #A8D86E;" +
                        "-fx-border-color: #704214;" +
                        "-fx-border-width: 4px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
        );

        Label emojiLabel = new Label(enclosure.getEmoji());
        emojiLabel.setStyle("-fx-font-size: 48px;");

        Label nameLabel = new Label(enclosure.getName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #31572C;");

        Label subtitleLabel = new Label(enclosure.getSubtitle());
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #527A34;");

        card.getChildren().addAll(emojiLabel, nameLabel, subtitleLabel);

        // Hover feedback
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #B9E38A;" +
                        "-fx-border-color: #704214;" +
                        "-fx-border-width: 4px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #A8D86E;" +
                        "-fx-border-color: #704214;" +
                        "-fx-border-width: 4px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
        ));

        card.setOnMouseClicked((MouseEvent e) -> openEnclosure(card, enclosure));

        return card;
    }

    private void openEnclosure(Node source, Enclosure enclosure) {
        try {
            URL fxmlUrl = getClass().getResource(enclosure.getFxmlPath());
            Parent root = javafx.fxml.FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setWidth(800);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.setTitle("Trash Slammers - " + enclosure.getName());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load " + enclosure.getFxmlPath());
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/main-menu-view.fxml");
            Parent root = javafx.fxml.FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Trash Slammers");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load main-menu-view.fxml");
        }
    }
}