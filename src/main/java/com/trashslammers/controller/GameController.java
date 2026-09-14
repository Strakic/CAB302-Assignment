package com.trashslammers.controller;

import com.trashslammers.model.PlayerSession;
import com.trashslammers.model.Score;
import com.trashslammers.service.DraggableMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Pane fallZone;

    @FXML private VBox bucketOrganic;
    @FXML private VBox bucketGeneral;
    @FXML private VBox bucketRecycle;
    @FXML private Label scoreLabel;
    @FXML private Button menuButton;

    // the shop spends the same score, so it lives on the session instead of here
    private final Score score = PlayerSession.getInstance().getScore();

    private List<Node> buckets;
    private ContextMenu gameMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // collect the buckets once so collision checks can loop over them
        buckets = List.of(bucketOrganic, bucketGeneral, bucketRecycle);

        // make sprite and immediately attach drag using spawn trash sprite method
        spawnTrashSprite("/com/trashslammers/Sprites/TrashSoda.png", 100, 100);

        updateScoreLabel();
    }

    private void spawnTrashSprite(String path, double x, double y) {
        // use create sprite method to make a sprite.
        ImageView sprite = createSprite(path, x, y, 120, 120);
        // connect DraggableMaker directly to the new sprite, and tell us when it's dropped
        DraggableMaker.makeDraggable(sprite, this::onDropped);
        // add new sprite as child to fallzone
        fallZone.getChildren().add(sprite);
    }

    // find the image at image path and build up sprite
    private ImageView createSprite(String path, double x, double y, double width, double height) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Resource not found: " + path);
            return new ImageView();
        }

        ImageView imageView = new ImageView(new Image(stream));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        return imageView;
    }

    // called by DraggableMaker when the user lets go of a sprite
    private void onDropped(Node trash) {
        if (bucketUnder(trash) == null) return;

        fallZone.getChildren().remove(trash);
        score.addForCorrectSort();
        updateScoreLabel();
    }

    // returns the bucket the sprite's centre is sitting in, or null if none
    private Node bucketUnder(Node trash) {
        Bounds t = trash.localToScene(trash.getBoundsInLocal());
        double cx = t.getCenterX();
        double cy = t.getCenterY();

        for (Node bucket : buckets) {
            Bounds b = bucket.localToScene(bucket.getBoundsInLocal());
            if (b.contains(cx, cy)) return bucket;
        }
        return null;
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score.getValue());
    }

    @FXML
    private void handleMenuButtonClick(ActionEvent event) {
        if (gameMenu == null) {
            gameMenu = buildGameMenu();
        }

        if (gameMenu.isShowing()) {
            gameMenu.hide();
        } else {
            gameMenu.show(menuButton, Side.BOTTOM, 0, 0);
        }
    }

    private ContextMenu buildGameMenu() {
        MenuItem shop = new MenuItem("Animal Shop");
        shop.setOnAction(event -> openAnimalShop());

        MenuItem enclosure = new MenuItem("Enclosure");
        enclosure.setOnAction(event -> openEnclosure());

        MenuItem resume = new MenuItem("Resume");

        return new ContextMenu(shop, enclosure, resume);
    }

    // open the shop as a modal so the game screen underneath stays as it was
    private void openAnimalShop() {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/animal-shop-view.fxml");
            Parent shopRoot = FXMLLoader.load(fxmlUrl);

            Stage shop = new Stage();
            shop.initOwner(menuButton.getScene().getWindow());
            shop.initModality(Modality.APPLICATION_MODAL);
            shop.setTitle("Trash Slammers - Animal Shop");
            shop.setScene(new Scene(shopRoot, 800, 600));
            shop.setResizable(false);
            shop.showAndWait();

            updateScoreLabel();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load animal-shop-view.fxml");
        }
    }

    private void openEnclosure() {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/enclosure-view.fxml");
            Parent enclosureRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) menuButton.getScene().getWindow();
            stage.setScene(new Scene(enclosureRoot, 800, 600));
            stage.setWidth(800);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.setTitle("Trash Slammers - Enclosure");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load enclosure-view.fxml");
        }
    }
}