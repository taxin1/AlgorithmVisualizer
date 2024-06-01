package com.example.algorithmvisualizer.view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class DescriptionBox extends VBox {
    private final TextArea descriptionTextArea;

    public DescriptionBox() {
        descriptionTextArea = new TextArea();
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setWrapText(true);
        this.getChildren().add(descriptionTextArea);
        this.getStyleClass().add("desc");
    }

    public void setDescription(String description) {
        descriptionTextArea.setText(description);
    }

    public void updateDescription(String algorithmName) {
        String description = getDescriptionForAlgorithm(algorithmName);
        setDescription(description);
    }

    private String getDescriptionForAlgorithm(String algorithmName) {
        return switch (algorithmName) {
            case "Bubble Sort" -> "Bubble Sort is a simple sorting algorithm...";
            case "Selection Sort" -> "Selection Sort is another simple sorting algorithm...";
            case "Insertion Sort" -> "Insertion Sort is yet another simple sorting algorithm...";
            // Add descriptions for other algorithms
            default -> "";
        };
    }
}
