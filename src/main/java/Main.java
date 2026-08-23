import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Load the initial starting view built in Scene Builder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trashslammers/views/main-menu-view.fxml"));
        Parent root = loader.load();

        // Set up the window and show it
        primaryStage.setTitle("Trash Slammers");
            primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}