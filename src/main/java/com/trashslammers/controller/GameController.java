package com.trashslammers.controller;

import com.trashslammers.database.DatabaseConnection;
import com.trashslammers.model.*;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.PlayerSession;
import com.trashslammers.model.Score;
import com.trashslammers.model.TrashItem;
import com.trashslammers.service.DraggableMaker;
//import com.trashslammers.service.SaveGameService;
import com.trashslammers.service.SpriteService;
import com.trashslammers.model.gamestates.*;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.io.File;
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
    @FXML private Button pauseButton;
    @FXML private StackPane countdownOverlay;
    @FXML private Label countdownLabel;
    @FXML private StackPane pauseOverlay;

    private final Score score = PlayerSession.getInstance().getScore();
    private final SpriteService spriteService = new SpriteService();
    private final Random rand = new Random();

    private List<Node> buckets;
    private List<TrashItem> trashPool;
    private final List<Node> activeTrash = new ArrayList<>();

    private ContextMenu gameMenu;
    private AnimationTimer gameLoop;
    private long lastFrameTime = -1;
    private Timeline spawner;
    private final double fallSpeedPxPerSecond = 100.0; // tune to taste; was fallSpeed=2.0 per 20ms tick
    private boolean isPaused = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buckets = List.of(bucketOrganic, bucketGeneral, bucketRecycle);

        // stop sprites painting outside the play area and over the HUD
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(fallZone.widthProperty());
        clip.heightProperty().bind(fallZone.heightProperty().add(200));
        fallZone.setClip(clip);

        trashPool = List.of(
                new TrashItem("TrashSoda.png", TrashItem.WasteType.RECYCLING)
        );

        updateScoreLabel();

        // Build the spawner and game loop now, but don't start them until the countdown finishes
        spawner = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> spawnRandomTrashSprite()));
        spawner.setCycleCount(Timeline.INDEFINITE);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime < 0) {
                    lastFrameTime = now;
                    return;
                }
                double deltaSeconds = (now - lastFrameTime) / 1_000_000_000.0;
                lastFrameTime = now;
                moveTrashDown(deltaSeconds);
            }
        };

        pauseButton.setDisable(true); // nothing to pause during the countdown
        startCountdown();
    }

    private void startCountdown() {
        countdownOverlay.setVisible(true);
        final int[] count = {3};
        animateCountdownPop(String.valueOf(count[0]));

        Timeline countdownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    count[0]--;
                    if (count[0] > 0) {
                        animateCountdownPop(String.valueOf(count[0]));
                    } else {
                        animateCountdownPop("GO!");
                    }
                })
        );
        countdownTimeline.setCycleCount(4); // fires at 3->2, 2->1, 1->0(GO!), then the GO! hold
        countdownTimeline.setOnFinished(e -> {
            countdownOverlay.setVisible(false);
            startGame();
        });
        countdownTimeline.play();
    }

    /**
     * Simple pop-and-fade for each countdown number.
     */
    private void animateCountdownPop(String text) {
        countdownLabel.setText(text);
        countdownLabel.setScaleX(1.5);
        countdownLabel.setScaleY(1.5);
        countdownLabel.setOpacity(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(300), countdownLabel);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(Duration.millis(300), countdownLabel);
        fade.setToValue(1.0);

        new ParallelTransition(scale, fade).play();
    }

    private void startGame() {
        lastFrameTime = -1; // reset so the first frame after countdown doesn't jump
        spawner.play();
        gameLoop.start();
        pauseButton.setDisable(false);
    }

    private void spawnRandomTrashSprite() {
        TrashItem randomItem = spriteService.getRandomTrashItem(trashPool);

        double zoneWidth = fallZone.getWidth() > 0 ? fallZone.getWidth() : 600;
        double startX = 20 + rand.nextDouble() * (zoneWidth - 140);
        double startY = -50; // Spawns above visible area

        ImageView sprite = spriteService.createSprite(randomItem.getName(), startX, startY, 120, 120);
        sprite.getProperties().put("correctBin", randomItem.getCorrectBin());

        // Pass direct drop callback without extra event filters
        DraggableMaker.makeDraggable(sprite, this::onDropped);

        activeTrash.add(sprite);
        fallZone.getChildren().add(sprite);
    }

    private void moveTrashDown(double deltaSeconds) {
        List<Node> toRemove = new ArrayList<>();

        for (Node trash : activeTrash) {
            // Move item downward, scaled by elapsed time for smooth frame-rate-independent motion
            trash.setLayoutY(trash.getLayoutY() + fallSpeedPxPerSecond * deltaSeconds);

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
    private void handlePauseButtonClick(ActionEvent event) {
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    @FXML
    private void handleResumeButtonClick(ActionEvent event) {
        resumeGame();
    }

    private void pauseGame() {
        isPaused = true;
        spawner.pause();
        gameLoop.stop();
        pauseButton.setText("Resume");
        pauseOverlay.setVisible(true);
    }

    private void resumeGame() {
        isPaused = false;
        lastFrameTime = -1; // reset so the first frame after resuming doesn't jump
        spawner.play();
        gameLoop.start();
        pauseButton.setText("Pause");
        pauseOverlay.setVisible(false);
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

        MenuItem save = new MenuItem("Save");
        //save.setOnAction(event -> saveGame());

        MenuItem back = new MenuItem("Back to Main Menu");
        back.setOnAction(event -> goToMainMenu());

        return new ContextMenu(shop, enclosure, resume, save, back);
    }

    private void goToMainMenu() {
        try {
            isPaused = false;
            spawner.stop();
            gameLoop.stop();
            pauseOverlay.setVisible(false);

            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/main-menu-view.fxml");
            Parent mainMenuRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) menuButton.getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot, 800, 600));
            stage.setTitle("TrashSlammers");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load main menu");
        }
    }

    /*   private void saveGame() {
           try {
               // Open JavaFX FileChooser so user can select location
               FileChooser fileChooser = new FileChooser();
               fileChooser.setTitle("Save Game Progress");
               fileChooser.setInitialFileName("trash_slammers_save.csv");
               fileChooser.getExtensionFilters().add(
                       new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv")
               );

               Window window = menuButton.getScene().getWindow();
               File selectedFile = fileChooser.showSaveDialog(window);

               // Exit cleanly if user cancels file picker
               if (selectedFile == null) {
                   return;
               }

               GameStateDAO gameStateDAO = new GameStateDAO();
               SaveGameService saveService = new SaveGameService(DatabaseConnection.getInstance(), gameStateDAO);

               int userId = 1;
               int currentScore = PlayerSession.getInstance().getScore().getValue();
               List<OwnedAnimal> animals = PlayerSession.getInstance().getShopService().ownedAnimals();

               saveService.saveGame(userId, selectedFile.toPath(), currentScore, animals);

               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Game Saved");
               alert.setHeaderText(null);
               alert.setContentText("Game saved successfully to " + selectedFile.getName() + "!");
               alert.showAndWait();

           } catch (Exception e) {
               e.printStackTrace();
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Save Failed");
               alert.setHeaderText(null);
               alert.setContentText("Could not save the game: " + e.getMessage());
               alert.showAndWait();
           }
       }
   */
    private void openAnimalShop() {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/animal-shop-view.fxml");
            if (fxmlUrl == null) return;
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
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/enclosure-list-view.fxml");
            if (fxmlUrl == null) return;
            Parent enclosureRoot = FXMLLoader.load(fxmlUrl);

            Stage stage = (Stage) menuButton.getScene().getWindow();
            stage.setScene(new Scene(enclosureRoot, 800, 600));
            stage.setWidth(800);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.setTitle("Trash Slammers - Enclosure");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load enclosure-list-view.fxml");
        }
    }
}