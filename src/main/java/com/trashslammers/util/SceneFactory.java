package com.trashslammers.util;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public final class SceneFactory {

    private static final String CSS = "/com/trashslammers/css/styles.css";

    private SceneFactory() {}

    public static Scene styled(Parent root, double w, double h) {
        Scene scene = new Scene(root, w, h);
        URL url = SceneFactory.class.getResource(CSS);
        scene.getStylesheets().add(url.toExternalForm());
        return scene;
    }

    public static Scene styled(Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                SceneFactory.class.getResource(CSS).toExternalForm());
        return scene;
    }
    public static void swap(Object eventSource, Parent root, String title) {
        Stage stage = (Stage) ((Node) eventSource).getScene().getWindow();
        stage.setMaximized(false);
        stage.setScene(styled(root));
        stage.setTitle(title);
        stage.setMaximized(true);
    }

}