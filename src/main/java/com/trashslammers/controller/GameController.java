package com.trashslammers.controller;

import com.trashslammers.model.PlayerSession;
import com.trashslammers.model.Score;
import com.trashslammers.model.TrashItem;
import com.trashslammers.service.DraggableMaker;
import com.trashslammers.service.SpriteService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private final Random rand = new Random();

    private List<Node> buckets;
    private List<TrashItem> trashPool;
    private final List<Node> activeTrash = new ArrayList<>();

    private ContextMenu gameMenu;
    private Timeline gameLoop;
    private Timeline spawner;
    private final double fallSpeed = 2.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buckets = List.of(bucketOrganic, bucketGeneral, bucketRecycle);

        trashPool = List.of(
                new TrashItem("TrashSoda.png", TrashItem.WasteType.RECYCLING)
        );

        updateScoreLabel();

        spawner = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> spawnRandomTrashSprite()));
        spawner.setCycleCount(Timeline.INDEFINITE);
        spawner.play();

        gameLoop = new Timeline(new KeyFrame(Duration.millis(20), e -> moveTrashDown()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void spawnRandomTrashSprite() {
        TrashItem randomItem = spriteService.getRandomTrashItem(trashPool);

        double zoneWidth = fallZone.getWidth() > 0 ? fallZone.getWidth() : 600;
        double startX = 20 + rand.nextDouble() * (zoneWidth - 140);
        double startY = -100; // Spawns above visible area

        ImageView sprite = spriteService.createSprite(randomItem.getName(), startX, startY, 120, 120);
        sprite.getProperties().put("correctBin", randomItem.getCorrectBin());

        // Pass direct drop callback without extra event filters
        DraggableMaker.makeDraggable(sprite, this::onDropped);

        activeTrash.add(sprite);
        fallZone.getChildren().add(sprite);
    }

    private void moveTrashDown() {
        List<Node> toRemove = new ArrayList<>();

        for (Node trash : activeTrash) {
            // Move item downward
            trash.setLayoutY(trash.getLayoutY() + fallSpeed);

            // Check if item hit a bin during movement
            Node collidedBucket = bucketUnder(trash);
            if (collidedBucket != null) {
                processBinCollision(trash, collidedBucket);
                toRemove.add(trash);
                continue;
            }

            // Remove only when sprite completely passes the bottom of the screen
            if (trash.getLayoutY() > fallZone.getHeight() + 120) {
                toRemove.add(trash);
            }
        }

        for (Node trash : toRemove) {
            activeTrash.remove(trash);
            fallZone.getChildren().remove(trash);
        }
    }

    private void onDropped(Node trash) {
        Node droppedBucket = bucketUnder(trash);
        if (droppedBucket == null) return;

        processBinCollision(trash, droppedBucket);
        activeTrash.remove(trash);
        fallZone.getChildren().remove(trash);
    }

    private void processBinCollision(Node trash, Node bucket) {
        TrashItem.WasteType itemBin = (TrashItem.WasteType) trash.getProperties().get("correctBin");

        boolean isCorrect = (bucket == bucketOrganic && itemBin == TrashItem.WasteType.GREEN)
                || (bucket == bucketGeneral && itemBin == TrashItem.WasteType.GENERAL)
                || (bucket == bucketRecycle && itemBin == TrashItem.WasteType.RECYCLING);

        if (isCorrect) {
            score.addForCorrectSort();
            updateScoreLabel();
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