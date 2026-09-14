package com.trashslammers.controller;

import com.trashslammers.model.Animal;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.PlayerSession;
import com.trashslammers.service.AnimalShopService;
import com.trashslammers.service.OwnershipFilter;
import com.trashslammers.service.SortMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AnimalShopController {

    private static final String SPRITE_PATH = "/com/trashslammers/Sprites/Animal Sprites/";
    private static final String PURCHASE_VIEW = "/com/trashslammers/views/animal-purchase-view.fxml";
    private static final String PLACE_VIEW = "/com/trashslammers/views/animal-place-view.fxml";

    @FXML
    private Label pointsLabel;

    @FXML
    private ComboBox<SortMode> sortComboBox;

    @FXML
    private ComboBox<OwnershipFilter> filterComboBox;

    @FXML
    private TilePane animalGrid;

    @FXML
    private void initialize() {
        sortComboBox.getItems().setAll(SortMode.values());
        sortComboBox.setValue(SortMode.CATALOG_ORDER);

        filterComboBox.getItems().setAll(OwnershipFilter.values());
        filterComboBox.setValue(OwnershipFilter.ALL);

        sortComboBox.valueProperty().addListener((observable, previous, current) -> refresh());
        filterComboBox.valueProperty().addListener((observable, previous, current) -> refresh());

        refresh();
    }

    private AnimalShopService shop() {
        return PlayerSession.getInstance().getShopService();
    }

    // redraw the balance and every card, so a purchase shows up straight away
    private void refresh() {
        pointsLabel.setText("Points: " + shop().getPointBalance());

        List<Node> cards = shop()
                .listCatalog(sortComboBox.getValue(), filterComboBox.getValue())
                .stream()
                .map(this::createCard)
                .map(Node.class::cast)
                .toList();

        animalGrid.getChildren().setAll(cards);
    }

    private VBox createCard(Animal animal) {
        boolean owned = shop().isOwned(animal.getId());
        boolean placed = shop().findOwned(animal.getId())
                .map(OwnedAnimal::isPlaced)
                .orElse(false);

        String textColour = owned ? "#31572C" : "#666666";

        ImageView artwork = createArtwork(animal);
        if (!owned) {
            ColorAdjust drained = new ColorAdjust();
            drained.setSaturation(-1);
            artwork.setEffect(drained);
            artwork.setOpacity(0.75);
        }

        Label name = new Label(animal.getName());
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);
        name.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + textColour + ";");

        Label rarity = new Label(animal.getRarity().getDisplayName());
        rarity.setStyle("-fx-font-size: 11px; -fx-text-fill: " + textColour + ";");

        Label cost = new Label(animal.getCost() + " pts");
        cost.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + textColour + ";");

        Label status = new Label(statusFor(owned, placed));
        status.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 3px 10px;"
                + " -fx-background-radius: 8px; -fx-background-color: " + statusColourFor(owned, placed) + ";");

        VBox card = new VBox(6, artwork, name, rarity, cost, status);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(175, 200);
        card.setStyle(cardStyleFor(owned, placed));
        card.setOnMouseClicked(event -> handleCardClick(animal));

        Tooltip tooltip = new Tooltip(owned
                ? animal.getFact()
                : "Unlock " + animal.getName() + " to reveal a fact about it.");
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(260);
        tooltip.setShowDelay(Duration.millis(150));
        Tooltip.install(card, tooltip);

        return card;
    }

    // stays empty until there is a sprite for the animal in the Animal Sprites folder
    private ImageView createArtwork(Animal animal) {
        ImageView artwork = new ImageView();
        artwork.setFitWidth(80);
        artwork.setFitHeight(80);
        artwork.setPreserveRatio(true);

        if (animal.getSpriteFile() == null) {
            return artwork;
        }

        InputStream stream = getClass().getResourceAsStream(SPRITE_PATH + animal.getSpriteFile());
        if (stream == null) {
            System.err.println("Resource not found: " + SPRITE_PATH + animal.getSpriteFile());
            return artwork;
        }

        artwork.setImage(new Image(stream));
        return artwork;
    }

    private String statusFor(boolean owned, boolean placed) {
        if (placed) {
            return "IN ENCLOSURE";
        }
        return owned ? "OWNED" : "BUY";
    }

    private String statusColourFor(boolean owned, boolean placed) {
        if (placed) {
            return "#527A34";
        }
        return owned ? "#8B5A2B" : "#a0a0a0";
    }

    private String cardStyleFor(boolean owned, boolean placed) {
        String background = owned ? "#E5C07B" : "#c9c9c9";
        String border = placed ? "#527A34" : owned ? "#704214" : "#a0a0a0";

        return "-fx-background-color: " + background + ";"
                + " -fx-border-color: " + border + ";"
                + " -fx-border-width: 4px;"
                + " -fx-background-radius: 10px;"
                + " -fx-border-radius: 10px;"
                + " -fx-padding: 10px;"
                + " -fx-cursor: hand;";
    }

    // an unowned animal opens the buy pop-up, tapping it again once owned opens the placement one
    private void handleCardClick(Animal animal) {
        if (shop().isOwned(animal.getId())) {
            openDialog(PLACE_VIEW, "Place " + animal.getName(), animal);
        } else {
            openDialog(PURCHASE_VIEW, "Buy " + animal.getName(), animal);
        }

        refresh();
    }

    private void openDialog(String fxmlPath, String title, Animal animal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent dialogRoot = loader.load();

            AnimalDialogController controller = loader.getController();
            controller.setAnimal(animal, shop());

            Stage dialog = new Stage();
            dialog.initOwner(animalGrid.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Trash Slammers - " + title);
            dialog.setScene(new Scene(dialogRoot, 420, 480));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load " + fxmlPath);
        }
    }

    @FXML
    private void handleCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
