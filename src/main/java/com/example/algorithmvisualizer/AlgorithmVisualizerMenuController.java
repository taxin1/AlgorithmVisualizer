package com.example.algorithmvisualizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class AlgorithmVisualizerMenuController {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private void handleSortingButtonClick(ActionEvent event) {
        if (mainStackPane == null) {
            System.err.println("Main stack pane is null.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SortingAlgorithmsMenu.fxml"));
            Parent sortingAlgorithmsMenu = loader.load();
            SortingAlgorithmsMenuController controller = loader.getController();

            mainStackPane.getChildren().clear();
            mainStackPane.getChildren().add(sortingAlgorithmsMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchingButtonClick(ActionEvent event) {
        System.out.println("Searching Algorithms button clicked!");
    }

    @FXML
    private void handleGraphButtonClick(ActionEvent event) {
        System.out.println("Graph Algorithms button clicked!");
    }
}
