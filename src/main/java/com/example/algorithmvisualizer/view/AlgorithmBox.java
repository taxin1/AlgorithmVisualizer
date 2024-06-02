package com.example.algorithmvisualizer.view;

import com.example.algorithmvisualizer.SortAlgorithms.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class AlgorithmBox extends VBox {
    private static final double PREFERRED_WIDTH = 600;
    // Buttons for each sorting algorithm
    private final Label titleLabel = new Label("Selected\nAlgorithm");
    private final Button bubbleSortButton = new Button("Bubble");
    private final Button selectionSortButton = new Button("Selection");
    private final Button insertionSortButton = new Button("Insertion");
    private final Button shellSortButton = new Button("Shell");
    private final Button mergeSortButton = new Button("Merge");
    private final Button quickSortButton = new Button("Quick");
    private final Button countingSortButton = new Button("Counting");
    private final Button radixSortButton = new Button("Radix");

    private static String selectedAlgorithm = "Bubble Sort";
    private Button selectedButton;

    public AlgorithmBox(FieldBox fieldBox, DescriptionBox descriptionBox){
        setPrefWidth(PREFERRED_WIDTH);
        this.getStyleClass().add("vbox2");

        setSpacing(5);

        // Initialize buttons
        initializeButtons(fieldBox, descriptionBox);

        // Add buttons to the VBox
        this.getChildren().addAll(
                bubbleSortButton,
                selectionSortButton,
                insertionSortButton,
                shellSortButton,
                mergeSortButton,
                quickSortButton,
                countingSortButton,
                radixSortButton
        );

        selectAlgorithm(bubbleSortButton,"Bubble Sort", fieldBox, descriptionBox);
    }

    private void initializeButtons(FieldBox fieldBox, DescriptionBox descriptionBox) {
        getChildren().add(titleLabel);

        double buttonWidth = 120;
        double buttonHeight = 30;
        double maxButtonWidth = Double.MAX_VALUE;

        bubbleSortButton.setPrefSize(buttonWidth, buttonHeight);
        selectionSortButton.setPrefSize(buttonWidth, buttonHeight);
        insertionSortButton.setPrefSize(buttonWidth, buttonHeight);
        shellSortButton.setPrefSize(buttonWidth, buttonHeight);
        mergeSortButton.setPrefSize(buttonWidth, buttonHeight);
        quickSortButton.setPrefSize(buttonWidth, buttonHeight);
        countingSortButton.setPrefSize(buttonWidth, buttonHeight);
        radixSortButton.setPrefSize(buttonWidth, buttonHeight);

//        bubbleSortButton.setMaxWidth(Double.MAX_VALUE);
//        selectionSortButton.setMaxWidth(Double.MAX_VALUE);
//        insertionSortButton.setMaxWidth(Double.MAX_VALUE);
//        shellSortButton.setMaxWidth(Double.MAX_VALUE);
//        mergeSortButton.setMaxWidth(Double.MAX_VALUE);
//        quickSortButton.setMaxWidth(Double.MAX_VALUE);
//        countingSortButton.setMaxWidth(Double.MAX_VALUE);
//        radixSortButton.setMaxWidth(Double.MAX_VALUE);

        bubbleSortButton.setOnAction(event -> selectAlgorithm(bubbleSortButton, "Bubble Sort", fieldBox, descriptionBox));
        selectionSortButton.setOnAction(event -> selectAlgorithm(selectionSortButton, "Selection Sort", fieldBox, descriptionBox));
        insertionSortButton.setOnAction(event -> selectAlgorithm(insertionSortButton,"Insertion Sort", fieldBox, descriptionBox));
        shellSortButton.setOnAction(event -> selectAlgorithm(shellSortButton,"Shell Sort", fieldBox, descriptionBox));
        mergeSortButton.setOnAction(event -> selectAlgorithm(mergeSortButton,"Merge Sort", fieldBox, descriptionBox));
        quickSortButton.setOnAction(event -> selectAlgorithm(quickSortButton,"Quick Sort", fieldBox, descriptionBox));
        countingSortButton.setOnAction(event -> selectAlgorithm(countingSortButton,"Counting Sort", fieldBox, descriptionBox));
        radixSortButton.setOnAction(event -> selectAlgorithm(radixSortButton,"Radix Sort", fieldBox, descriptionBox));
    }

    private void selectAlgorithm(Button selectedButton, String algorithmName, FieldBox fieldBox, DescriptionBox descriptionBox) {
        if (selectedButton == this.selectedButton) {
            return;
        }

        // Deselect the previously selected button (if any)
        if (this.selectedButton != null) {
            this.selectedButton.setStyle("-fx-background-color: lightgrey;");
        }

        // Select the newly selected button
        this.selectedButton = selectedButton;
        this.selectedButton.setStyle("-fx-background-color: #87CEFA;");


        selectedAlgorithm = algorithmName;
        System.out.println("Selected algorithm: " + selectedAlgorithm);
        descriptionBox.updateDescription(selectedAlgorithm);

//        if (MainWindow.descriptionBox != null) {
//            MainWindow.descriptionBox.updateDescription(getDescriptionForAlgorithm(selectedAlgorithm));
//        }
        if (selectedAlgorithm.equals("Radix Sort")) {
            for (Node node : fieldBox.getChildren()) {
                if (node instanceof EnterField currentField) {
                    if (!currentField.getText().isEmpty()) {
                        int value = Integer.parseInt(currentField.getText());
                        if (value < 10) {
                            Tooltip tooltip = new Tooltip("Value must be\nbetween 10-50");
                            currentField.setStyle("-fx-text-fill: red");
                            currentField.setTooltip(tooltip);
                        }
                    }
                }
            }
            fieldBox.setLabel("Enter Values 10-50: ");
        } else {
            for (Node node : fieldBox.getChildren()) {
                if (node instanceof EnterField) {
                    node.setStyle("-fx-text-fill: black");
                }
            }
            fieldBox.setLabel("Enter Values 1-50: ");
        }
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

    public static AlgorithmAnimation getAlgorithm() {
        switch(selectedAlgorithm) {
            case "Bubble Sort":
                return new BubbleSortAnimation();
            case "Selection Sort":
                return new SelectionSortAnimation();
            case "Insertion Sort":
                return new InsertionSortAnimation();
            case "Shell Sort":
                return new ShellSortAnimation();
            case "Merge Sort":
                return new MergeSortAnimation();
            case "Quick Sort":
                return new QuickSortAnimation();
            case "Counting Sort":
                return new CountingSortAnimation();
            case "Radix Sort":
                return new RadixSortAnimation();
            default:
                System.out.println("No algorithm selected or unknown algorithm: " + selectedAlgorithm);
                return null;
        }
    }

    public static String getSelectedIndices() {
        return selectedAlgorithm;
    }
}
