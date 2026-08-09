import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Define the root node and set the alignment and spacing properties
        VBox root = new VBox();
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setSpacing(15.0);
        root.setFillWidth(false);

        // Create a TextField, a Label, and an HBox
        TextField textField = new TextField();
        textField.setText("TextField");
        Label label = new Label("Label");

        // The HBox is used to hold the buttons
        HBox hbox = new HBox();
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        hbox.setSpacing(15.0);
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");
        Button button3 = new Button("Button 3");

        // Add the buttons to the HBox
        hbox.getChildren().addAll(button1, button2, button3);

        // Add the children to the root VBox
        root.getChildren().addAll(textField, label, hbox);

        // Define the scene, add to the stage (window) and show the stage
        Scene scene = new Scene(root, 320, 180);
        stage.setScene(scene);
        stage.setTitle("JavaFX Example Scene");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}