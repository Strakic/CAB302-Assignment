package com.trashslammers.controller;

import com.trashslammers.database.DatabaseConnection;
import com.trashslammers.model.OwnedAnimal;
import com.trashslammers.model.PlayerSession;
import com.trashslammers.model.Score;
import com.trashslammers.model.TrashItem;
import com.trashslammers.model.gamestates.GameStateDAO;
import com.trashslammers.model.qte.ActiveBoost;
import com.trashslammers.model.qte.GoldenTrashEvent;
import com.trashslammers.model.qte.QteReward;
import com.trashslammers.service.DraggableMaker;
import com.trashslammers.service.QteService;
import com.trashslammers.service.SaveGameService;
import com.trashslammers.service.Session;
import com.trashslammers.service.SpriteService;
import com.trashslammers.util.SceneFactory;
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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controller for the main game screen, where the player sorts falling trash into the right bins to earn points.
 */
public class GameController implements Initializable {

    @FXML private Pane fallZone;
    @FXML private VBox bucketOrganic;
    @FXML private VBox bucketGeneral;
    @FXML private VBox bucketRecycle;
    @FXML private Label scoreLabel;
    @FXML private Label boostLabel;
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

    private final QteService qteService = new QteService(rand);
    private final ActiveBoost boost = new ActiveBoost();
    private GoldenTrashEvent goldenEvent;
    private Node goldenSprite;
    private double goldenStep;

    /**
     * Sets up the play area and score label, then starts spawning and moving trash.
     * @param url The location of the FXML file
     * @param resourceBundle The resources for the FXML file
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buckets = List.of(bucketOrganic, bucketGeneral, bucketRecycle);

        // stop sprites painting outside the play area and over the HUD
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(fallZone.widthProperty());
        clip.heightProperty().bind(fallZone.heightProperty().add(200));
        fallZone.setClip(clip);
        // ADD NEW TRASH HERE!!!!!!!
        trashPool = List.of(
                new TrashItem("TrashSoda.png", TrashItem.WasteType.RECYCLING),
                new TrashItem("AppleCore.png", TrashItem.WasteType.GREEN),
                new TrashItem("ChipBag.png", TrashItem.WasteType.GENERAL)
        );
        scoreLabel.textProperty().bind(score.valueProperty().asString("Score: %d"));

        spawner = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> spawnRandomTrashSprite()));
        spawner.setCycleCount(Timeline.INDEFINITE);
        spawner.play();

        gameLoop = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            moveTrashDown();
            moveGoldenTrash();
            updateBoost();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    /**
     * Adds a random piece of draggable trash at the top of the play area.
     */
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

    /**
     * Moves all trash down, scoring any that land on a bin and removing any that fall off the screen.
     */
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

    private void updateBoost() {
        boost.tick(0.02);

        // blank while nothing is running, so the hud only shows a live boost
        if (boost.currentMultiplier() > 1) {
            boostLabel.setText(String.format("x%d  %.1fs", boost.currentMultiplier(), boost.remainingSeconds()));
        } else {
            boostLabel.setText("");
        }
    }

    private void moveGoldenTrash() {
        // service holds spawn timer
        if (goldenEvent == null) {
            qteService.tick(0.02).ifPresent(this::showGoldenTrash);
            return;
        }

        goldenEvent.tick(0.02);
        goldenSprite.setLayoutX(goldenSprite.getLayoutX() + goldenStep);

        // gone once the player runs out of time or it drifts off the right side
        if (!goldenEvent.isActive() || goldenSprite.getLayoutX() > fallZone.getWidth() + 60) {
            removeGoldenTrash();
        }
    }

    private void showGoldenTrash(GoldenTrashEvent event) {
        double zoneWidth = fallZone.getWidth() > 0 ? fallZone.getWidth() : 600;
        double zoneHeight = fallZone.getHeight() > 0 ? fallZone.getHeight() : 400;

        // pace it to clear the full width
        double ticks = event.windowSeconds() / 0.02;
        goldenStep = (zoneWidth + 60) / ticks;

        Circle golden = new Circle(30, Color.GOLD);
        golden.setStroke(Color.BLACK);
        golden.setLayoutX(-30); // starts just off the left edge
        golden.setLayoutY(60 + rand.nextDouble() * (zoneHeight / 2));
        golden.setOnMouseClicked(e -> onGoldenClicked());

        goldenEvent = event;
        goldenSprite = golden;
        fallZone.getChildren().add(golden);
    }

    private void onGoldenClicked() {
        if (goldenEvent.claim()) {
            giveReward(qteService.rollReward());
        }
        removeGoldenTrash();
    }

    private void giveReward(QteReward reward) {
        switch (reward) {
            case CASH_BONUS -> score.add(qteService.cashBonus());
            case POINT_MULTIPLIER -> boost.activate(qteService.boostMultiplier(), qteService.boostSeconds());
            case TRASH_BURST -> spawnTrashBurst();
        }
    }

    private void spawnTrashBurst() {
        Timeline burst = new Timeline(new KeyFrame(Duration.millis(200), e -> spawnRandomTrashSprite()));
        burst.setCycleCount(qteService.burstSize());
        burst.play();
    }

    private void removeGoldenTrash() {
        fallZone.getChildren().remove(goldenSprite);
        goldenSprite = null;
        goldenEvent = null;
    }

    /**
     * Scores and removes a piece of trash if the player dropped it on a bin.
     * @param trash The piece of trash that was dropped
     */
    private void onDropped(Node trash) {
        Node droppedBucket = bucketUnder(trash);
        if (droppedBucket == null) return;

        processBinCollision(trash, droppedBucket);
        activeTrash.remove(trash);
        fallZone.getChildren().remove(trash);
    }

    /**
     * Adds points if a piece of trash reached the correct bin.
     * @param trash The piece of trash that reached a bin
     * @param bucket The bin it reached
     */
    private void processBinCollision(Node trash, Node bucket) {
        TrashItem.WasteType itemBin = (TrashItem.WasteType) trash.getProperties().get("correctBin");

        boolean isCorrect = (bucket == bucketOrganic && itemBin == TrashItem.WasteType.GREEN)
                || (bucket == bucketGeneral && itemBin == TrashItem.WasteType.GENERAL)
                || (bucket == bucketRecycle && itemBin == TrashItem.WasteType.RECYCLING);

        if (isCorrect) {
            score.addForCorrectSort(Session.getPointMultiplier() * boost.currentMultiplier());
        }
    }

    /**
     * Finds the bin that a piece of trash is over.
     * @param trash The piece of trash to check
     * @return The bin under the trash, or null if it isn't over one
     */
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

    /**
     * Shows or hides the in-game menu when the Menu button is clicked.
     * @param event The button click event
     */
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

    /**
     * Builds the in-game menu with the Animal Shop, Enclosure, Resume and Save options.
     * @return The in-game menu
     */
    private ContextMenu buildGameMenu() {
        MenuItem shop = new MenuItem("Animal Shop");
        shop.setOnAction(event -> openAnimalShop());

        MenuItem enclosure = new MenuItem("Enclosure");
        enclosure.setOnAction(event -> openEnclosure());

        MenuItem resume = new MenuItem("Resume");

        MenuItem save = new MenuItem("Save");
        save.setOnAction(event -> saveGame());

        return new ContextMenu(shop, enclosure, resume, save);
    }

    /**
     * Saves the player's score and animals to a CSV file the player chooses.
     */
    private void saveGame() {
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


    /**
     * Opens the animal shop in a new window.
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
            shop.setScene(SceneFactory.styled(shopRoot));
            shop.setMaximized(true);
            shop.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load animal-shop-view.fxml");
        }
    }

    /**
     * Switches the game window to the enclosure screen.
     */
    private void openEnclosure() {
        try {
            URL fxmlUrl = getClass().getResource("/com/trashslammers/views/enclosure-view.fxml");
            if (fxmlUrl == null) return;
            Parent enclosureRoot = FXMLLoader.load(fxmlUrl);

            SceneFactory.swap(menuButton, enclosureRoot, "Trash Slammers - Enclosure");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load enclosure-view.fxml");
        }
    }
}