package com.example.algorithmvisualizer.view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class DescriptionBox extends VBox {
    private final TextArea descriptionTextArea;


    public DescriptionBox() {
        this.getStyleClass().add("vbox3");
        descriptionTextArea = new TextArea();
//        descriptionTextArea.setStyle("-fx-font-family: \"MesloLGL Nerd Font\";");
//        descriptionTextArea.setStyle("-fx-font-weight: bold;");
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setPrefWidth(250);
        descriptionTextArea.setPrefHeight(250);
        descriptionTextArea.setStyle("-fx-alignment: center;");
        this.getChildren().add(descriptionTextArea);

        this.setPrefWidth(250); // Adjust width as needed
        this.setPrefHeight(250);
    }

    public void setDescription(String description) {
        descriptionTextArea.setText(description);
    }

    public void updateDescription(String algorithmName) {
        String description = getDescriptionForAlgorithm(algorithmName);
        setDescription(description);
        System.out.println("Description: " + description + " " + algorithmName);
    }

    private String getDescriptionForAlgorithm(String algorithmName) {
        return switch (algorithmName) {
            case "Bubble Sort" -> "Best Time Complexity: O(n)\n" +
                    "Worst Time Complexity: O(n^2)\n" +
                    "Average Time Complexity: O(n^2)\n" +
                    "Space Complexity: O(1)";
            case "Selection Sort" -> "Best Time Complexity: O(n^2)\n" +
                    "Worst Time Complexity: O(n^2)\n" +
                    "Average Time Complexity: O(n^2)\n" +
                    "Space Complexity: O(1)";
            case "Insertion Sort" -> "Best Time Complexity: O(n)\n" +
                    "Worst Time Complexity: O(n^2)\n" +
                    "Average Time Complexity: O(n^2)\n" +
                    "Space Complexity: O(1)";
            case "Shell Sort" -> "Best Time Complexity: O(n log n)\n" +
                    "Worst Time Complexity: O(n^2)\n" +
                    "Average Time Complexity: Depends on gap sequence\n" +
                    "Space Complexity: O(1)";
            case "Merge Sort" -> "Best Time Complexity: O(n log n)\n" +
                    "Worst Time Complexity: O(n log n)\n" +
                    "Average Time Complexity: O(n log n)\n" +
                    "Space Complexity: O(n)";
            case "Quick Sort" -> "Best Time Complexity: O(n log n)\n" +
                    "Worst Time Complexity: O(n^2)\n" +
                    "Average Time Complexity: O(n log n)\n" +
                    "Space Complexity: O(log n) to O(n)";
            case "Counting Sort" -> "Best Time Complexity: O(n + k)\n" +
                    "Worst Time Complexity: O(n + k)\n" +
                    "Average Time Complexity: O(n + k)\n" +
                    "Space Complexity: O(n + k)";
            case "Radix Sort" -> "Best Time Complexity: O(nk)\n" +
                    "Worst Time Complexity: O(nk)\n" +
                    "Average Time Complexity: O(nk)\n" +
                    "Space Complexity: O(n + k)";
            // Add descriptions for other algorithms
            default -> "";
        };
    }
}
