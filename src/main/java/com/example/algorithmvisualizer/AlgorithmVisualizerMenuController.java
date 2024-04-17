package com.example.algorithmvisualizer;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class AlgorithmVisualizerMenuController {
    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private void initialize() {
        String imageFilePath = getClass().getResource("/images/mainMenuBackground.jpg").toString();
        Image image = new Image(imageFilePath);
        ImageView imageView = new ImageView(image);

        mainAnchorPane.getChildren().add(imageView);
        imageView.toBack();
    }

    @FXML
    private void handleSortingButtonClick(ActionEvent event) {
        if (mainAnchorPane == null) {
            System.err.println("Main stack pane is null.");
            return;
        }

        try {
            MainWindow mainWindow = new MainWindow();
            Scene currentScene = mainAnchorPane.getScene();
            currentScene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("styles.css")).toExternalForm());
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

    @FXML
    private void handleCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.close();
    }
}
