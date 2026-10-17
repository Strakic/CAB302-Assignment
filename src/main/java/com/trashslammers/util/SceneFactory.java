package com.trashslammers.util;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Creates scenes with the game's stylesheet and switches the window to new screens.
 */
public final class SceneFactory {

    private static final String CSS = "/com/trashslammers/css/styles.css";

    private SceneFactory() {}

    /**
     * Creates a scene of the given size with the game's stylesheet.
     * @param root The root node of the scene
     * @param w The width of the scene
     * @param h The height of the scene
     * @return The styled scene
     */
    public static Scene styled(Parent root, double w, double h) {
        Scene scene = new Scene(root, w, h);
        URL url = SceneFactory.class.getResource(CSS);
        scene.getStylesheets().add(url.toExternalForm());
        return scene;
    }

    /**
     * Creates a scene with the game's stylesheet.
     * @param root The root node of the scene
     * @return The styled scene
     */
    public static Scene styled(Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                SceneFactory.class.getResource(CSS).toExternalForm());
        return scene;
    }

    /**
     * Shows a new screen in the window that an event came from.
     * @param eventSource The node that triggered the change
     * @param root The root node of the new screen
     * @param title The new window title
     */
    public static void swap(Object eventSource, Parent root, String title) {
        Stage stage = (Stage) ((Node) eventSource).getScene().getWindow();
        stage.setMaximized(false);
        stage.setScene(styled(root));
        stage.setTitle(title);
        stage.setMaximized(true);
    }

}