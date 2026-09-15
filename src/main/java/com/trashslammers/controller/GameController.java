package com.trashslammers.controller;

import com.trashslammers.model.PlayerSession;
import com.trashslammers.model.Score;
import com.trashslammers.model.TrashItem;
import com.trashslammers.service.DraggableMaker;
import com.trashslammers.service.SpriteService;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Pane fallZone;
    @FXML private VBox bucketOrganic;
    @FXML private VBox bucketGeneral;
    @FXML private VBox bucketRecycle;
    @FXML private Label scoreLabel;
    @FXML private Button menuButton;

    private final Score score = PlayerSession.getInstance().getScore();
    private final SpriteService spriteService = new SpriteService();

    private List<Node> buckets;
    private List<TrashItem> trashPool;
    private ContextMenu gameMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buckets = List.of(bucketOrganic, bucketGeneral, bucketRecycle);

        // Define pool mapping items to WasteType enum
        trashPool = List.of(
                new TrashItem("Soda Can", TrashItem.WasteType.RECYCLING),
                new TrashItem("Apple Core", TrashItem.WasteType.GREEN),
                new TrashItem("Wrapper", TrashItem.WasteType.GENERAL)
        );

        spawnRandomTrashSprite(100, 100);
        updateScoreLabel();
    }

    private void spawnRandomTrashSprite(double x, double y) {
        TrashItem randomItem = spriteService.getRandomTrashItem(trashPool);

        ImageView sprite = spriteService.createSprite(
                randomItem.getCorrectBin().getFileName(),
                x,
                y,
                120,
                120
        );

        sprite.getProperties().put("startX", x);
        sprite.getProperties().put("startY", y);
        sprite.getProperties().put("targetWasteType", randomItem.getCorrectBin());

        DraggableMaker.makeDraggable(sprite, this::onDropped);
        fallZone.getChildren().add(sprite);
    }

    private void onDropped(Node trash) {
        Node droppedBucket = bucketUnder(trash);

        if (droppedBucket == null) {
            resetTrashPosition(trash);
            return;
        }

        TrashItem.WasteType targetType = (TrashItem.WasteType) trash.getProperties().get("targetWasteType");

        if (isCorrectBucket(droppedBucket, targetType)) {
            fallZone.getChildren().remove(trash);
            score.addForCorrectSort();
            updateScoreLabel();
            spawnRandomTrashSprite(100, 100);
        } else {
            resetTrashPosition(trash);
        }
    }

    private boolean isCorrectBucket(Node bucketNode, TrashItem.WasteType type) {
        if (bucketNode == bucketOrganic && type == TrashItem.WasteType.GREEN) return true;
        if (bucketNode == bucketRecycle && type == TrashItem.WasteType.RECYCLING) return true;
        if (bucketNode == bucketGeneral && type == TrashItem.WasteType.GENERAL) return true;
        return false;
    }

    private void resetTrashPosition(Node trash) {
        Double startX = (Double) trash.getProperties().get("startX");
        Double startY = (Double) trash.getProperties().get("startY");
        if (startX != null && startY != null) {
            trash.setLayoutX(startX);
            trash.setLayoutY(startY);
        }
    }

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