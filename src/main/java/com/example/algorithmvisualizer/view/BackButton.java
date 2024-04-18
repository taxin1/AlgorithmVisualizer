package com.example.algorithmvisualizer.view;

import com.example.algorithmvisualizer.algorithms.AlgorithmAnimation ;
import com.example.algorithmvisualizer.algorithms.RadixSortAnimation ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class BackButton extends Button {
    @FXML
    private AnchorPane mainAnchorPane;
    public BackButton() {
        this.setText("Back");
        this.getStyleClass().add("Button");

        this.setOnAction(actionEvent -> {
            if (mainAnchorPane == null) {
                System.err.println("Main anchor pane is null.");
                return;
            }

            try {
                //MainWindow mainWindow = new MainWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Menu.fxml")));
                Scene currentScene = mainAnchorPane.getScene();
                mainAnchorPane.getChildren().add(root);
                Scene newScene = new Scene(root, currentScene.getWidth(), currentScene.getHeight());
                //currentScene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("styles.css")).toExternalForm());
                currentScene.setRoot(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
