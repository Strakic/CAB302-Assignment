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

public class Main extends Application {

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

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance();
        launch(args);
    }
}