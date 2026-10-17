import com.trashslammers.database.DatabaseConnection;
import com.trashslammers.util.SceneFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.io.IOException;
import java.net.URL;

/**
 * The main JavaFX application class, which starts Trash Slammers and opens the main menu.
 */
public class Main extends Application {

    /**
     * Loads the game's font and shows the main menu in a maximised window.
     * @param primaryStage The main window of the application
     * @throws Exception If the main menu view can't be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("/com/trashslammers/fonts/PIXY.ttf"), 16);
        //Load the initial starting view built in Scene Builder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trashslammers/views/main-menu-view.fxml"));
        Parent root = loader.load();

        Scene scene = SceneFactory.styled(root, 800, 600);

        // Set up the window and show it
        primaryStage.setTitle("Trash Slammers");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Connects to the database and then launches the application.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance();
        launch(args);
    }
}