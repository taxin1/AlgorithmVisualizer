package com.example.algorithmvisualizer.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ButtonBox extends HBox {
    ResetButton resetButton;
    SortButton sortButton;
    Button backButton;
    Button removeBarsButton;

    public ButtonBox(FieldBox fieldBox, FXMLLoader loader, Scene primaryScene) {
        this.getStyleClass().add("buttonbox");

        sortButton = new SortButton(fieldBox);
        resetButton = new ResetButton(fieldBox);

        removeBarsButton = new Button("Remove Bars");
        removeBarsButton.setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            MainWindow newMainWindow = new MainWindow(primaryScene, loader);
            newMainWindow.getScene().getStylesheets().add("styles.css");
            stage.setScene(new Scene(newMainWindow));
        });

        // Create and add back button
        backButton = new Button("Back");
        backButton.setOnAction(event -> {
            clearContent();
            reloadAlgorithmVisualizerMenu(loader);
        });

        this.sortButton.getStyleClass().add("button");
        this.resetButton.getStyleClass().add("button");
        this.backButton.getStyleClass().add("button");
        this.removeBarsButton.getStyleClass().add("button");

        resetButton.setSortButton(sortButton);
        sortButton.setResetButton(resetButton);

        this.getChildren().add(sortButton);
        this.getChildren().add(resetButton);
        this.getChildren().add(removeBarsButton);
        this.getChildren().add(backButton);
    }

    private void clearContent() {
        this.getChildren().clear();
    }

    private void reloadAlgorithmVisualizerMenu(FXMLLoader loader) {
        try {
            AnchorPane algorithmVisualizerMenu = loader.load();
            Scene newScene = new Scene(algorithmVisualizerMenu);
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
