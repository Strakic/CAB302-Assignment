package com.trashslammers.service;

import javafx.scene.Node;

import java.util.function.Consumer;

public class DraggableMaker {

    public static void makeDraggable(Node node) {
        makeDraggable(node, n -> {});
    }

    public static void makeDraggable(Node node, Consumer<Node> onRelease) {
        node.setOnMousePressed(event -> {
            node.getProperties().put("anchorX", event.getSceneX() - node.getLayoutX());
            node.getProperties().put("anchorY", event.getSceneY() - node.getLayoutY());
            node.toFront();
        });

        node.setOnMouseDragged(event -> {
            Double anchorX = (Double) node.getProperties().get("anchorX");
            Double anchorY = (Double) node.getProperties().get("anchorY");

            if (anchorX != null && anchorY != null) {
                node.setLayoutX(event.getSceneX() - anchorX);
                node.setLayoutY(event.getSceneY() - anchorY);
            }
        });

        node.setOnMouseReleased(event -> onRelease.accept(node));
    }
}