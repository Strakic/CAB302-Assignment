package com.trashslammers.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController {
    @FXML
            private AnchorPane anchorPane;
    @FXML
            private Rectangle rectangle;

    DraggableMaker draggableMaker = new DraggableMaker();

    @Override
    public void App(URL url, ResourceBundle resourceBundle) {
        draggableMaker.makeDraggable(rectangle);
        draggableMaker.makeDraggable(anchorPane);
    }

}
