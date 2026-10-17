package com.trashslammers.service;

import javafx.scene.Node;

import java.util.function.Consumer;

/**
 * Makes nodes draggable with the mouse.
 */
public class DraggableMaker {

    public static void makeDraggable(Node node) {
        makeDraggable(node, n -> {});
    }

    /**
     * Makes a node draggable with the mouse.
     * @param node The node to make draggable
     */
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