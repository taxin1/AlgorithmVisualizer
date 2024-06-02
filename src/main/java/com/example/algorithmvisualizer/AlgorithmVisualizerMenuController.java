package com.example.algorithmvisualizer;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class AlgorithmVisualizerMenuController {
    @FXML
    private AnchorPane mainAnchorPane;
    private Scene primaryScene;

    static CanvasController cref;

    public void setPrimaryScene(Scene scene) {
        this.primaryScene = scene;
        System.out.println("Primary Scene set: " + scene);
    }

    @FXML
    private void initialize() {
        String imageFilePath = Objects.requireNonNull(getClass().getResource("/images/mainMenuBackground.jpg")).toString();
        Image image = new Image(imageFilePath);
        ImageView imageView = new ImageView(image);

        mainAnchorPane.getChildren().add(imageView);
        imageView.toBack();
    }

    @FXML
    private void handleSortingButtonClick() {
        if (mainAnchorPane == null) {
            System.err.println("Main anchor pane is null.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            Scene currentScene = mainAnchorPane.getScene();
            System.out.println("Passing Primary Scene to MainWindow: " + primaryScene);
            String stylesheetPath = Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm();
            MainWindow mainWindow = new MainWindow(primaryScene, loader, stylesheetPath);
            currentScene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("styles.css")).toExternalForm());
            currentScene.setRoot(mainWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchingButtonClick() {
        if (mainAnchorPane == null) {
            System.err.println("Main anchor pane is null.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PathfindingVisualizer.fxml"));
            Parent pathfindingRoot = loader.load();
            Scene currentScene = mainAnchorPane.getScene();
            currentScene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("Pathfinding.css")).toExternalForm());
            currentScene.setRoot(pathfindingRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGraphButtonClick() {
        if (mainAnchorPane == null) {
            System.err.println("Main anchor pane is null.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Canvas.fxml"));
            Parent pathfindingRoot = loader.load();
            Scene currentScene = mainAnchorPane.getScene();
            currentScene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("Styling.css")).toExternalForm());
            currentScene.setRoot(pathfindingRoot);
            cref = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.close();
    }
}
