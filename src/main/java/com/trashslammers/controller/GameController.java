package com.trashslammers.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController {
    @FXML
            private AnchorPane anchorPane;
    @FXML
            private Circle circle;

    DraggableMaker draggableMaker = new DraggableMaker();

    public void App(URL url, ResourceBundle resourceBundle) {
        draggableMaker.makeDraggable(circle);
        draggableMaker.makeDraggable(anchorPane);
    }

}
