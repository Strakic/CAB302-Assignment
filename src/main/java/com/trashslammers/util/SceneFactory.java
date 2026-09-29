package com.trashslammers.util;

import javafx.scene.Parent;
import javafx.scene.Scene;

public final class SceneFactory {

    private static final String CSS = "/com/trashslammers/css/styles.css";

    private SceneFactory() {}

    public static Scene styled(Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(
                SceneFactory.class.getResource(CSS).toExternalForm());
        return scene;
    }
}
