package com.example.algorithmvisualizer;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

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
            // Instantiate the MainWindow
            MainWindow mainWindow = new MainWindow();

            // Get the current scene from the mainStackPane
            Scene currentScene = mainStackPane.getScene();

            // Replace the root node of the current scene with the MainWindow
            currentScene.setRoot(mainWindow);
        } catch (Exception e) {
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
