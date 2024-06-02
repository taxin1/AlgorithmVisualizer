package com.example.algorithmvisualizer.view;

import com.example.algorithmvisualizer.AlgorithmVisualizerMenuController;
import com.example.algorithmvisualizer.SortAlgorithms.AlgorithmAnimation;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ButtonBox extends HBox {
    ResetButton resetButton;
    SortButton sortButton;
    Button backButton;
    Button removeBarsButton;
    Slider durationSlider;
    static ToggleButton playPause;
    AlgorithmAnimation current;
    static Duration animationDuration = Duration.millis(400);
    public static boolean isPaused = false;

    public ButtonBox(FieldBox fieldBox, FXMLLoader loader, Scene primaryScene, String stylesheetPath) {
        this.getStyleClass().add("buttonbox");

        sortButton = new SortButton(fieldBox, this);
        resetButton = new ResetButton(fieldBox);

        removeBarsButton = new Button("Reset Everything");
        removeBarsButton.setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            MainWindow newMainWindow = new MainWindow(primaryScene, loader, stylesheetPath);
            Scene scene = new Scene(newMainWindow, 900, 600);
            scene.getStylesheets().add(stylesheetPath);
            stage.setScene(scene);
        });

        durationSlider = new Slider(100, 1500, 400);
        durationSlider.setShowTickLabels(false);
        durationSlider.setShowTickMarks(false);
        durationSlider.setMajorTickUnit(100);
        durationSlider.setBlockIncrement(50);
        durationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            AlgorithmAnimation currentAlgorithm = AlgorithmBox.getAlgorithm();
            if (currentAlgorithm != null) {
                animationDuration = Duration.millis(newVal.doubleValue());
            }
        });
        durationSlider.setPrefWidth(250);

        playPause = new ToggleButton("Pause");
        playPause.setDisable(true);
        playPause.setPrefWidth(70);
        playPause.selectedProperty().addListener((obs, oldVal, newVal) -> {
            isPaused = newVal;
            playPause.setText(newVal ? "Play" : "Pause");
            if (!newVal) {
                if (current != null) {
                    current.continueTransition();
                }
            }
        });
        backButton = new Button("Back");
        backButton.setOnAction(event -> {
            clearContent();
            reloadAlgorithmVisualizerMenu(loader);
        });

        this.sortButton.getStyleClass().add("button");
        this.sortButton.setStyle("-fx-background-color: lightgreen;");
        this.resetButton.getStyleClass().add("button");
        this.resetButton.setStyle("-fx-background-color: yellow;");
        this.backButton.getStyleClass().add("button");
        this.removeBarsButton.getStyleClass().add("button");
        this.removeBarsButton.setStyle("-fx-background-color: lightcoral;");
        playPause.getStyleClass().add("button");
        this.durationSlider.getStyleClass().add("slider");

        resetButton.setSortButton(sortButton);
        sortButton.setResetButton(resetButton);
        sortButton.setPlayPauseButton(playPause);
        resetButton.setPlayPause(playPause);

        this.getChildren().add(sortButton);
        this.getChildren().add(resetButton);
        this.getChildren().add(removeBarsButton);
        this.getChildren().add(playPause);
        HBox.setMargin(durationSlider, new Insets(14, 0, 0, 0));
        HBox.setHgrow(durationSlider, Priority.ALWAYS);
        this.getChildren().add(durationSlider);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);

        this.getChildren().add(backButton);
        primaryScene.getStylesheets().add(stylesheetPath);
    }

    private void clearContent() {
        this.getChildren().clear();
    }

    public void setCurrent(AlgorithmAnimation current) {
        this.current = current;
    }

    private void reloadAlgorithmVisualizerMenu(FXMLLoader loader) {
        try {
            AnchorPane algorithmVisualizerMenu = loader.load();
            Scene newScene = new Scene(algorithmVisualizerMenu);
            Stage stage = (Stage) this.getScene().getWindow();
            AlgorithmVisualizerMenuController controller = loader.getController();
            controller.setPrimaryScene(newScene);
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPlayPauseDisabled() {
        playPause.setDisable(true);
    }
}
