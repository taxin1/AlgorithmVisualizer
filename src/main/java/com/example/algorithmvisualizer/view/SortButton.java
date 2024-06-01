package com.example.algorithmvisualizer.view;

import com.example.algorithmvisualizer.SortAlgorithms.AlgorithmAnimation;
import com.example.algorithmvisualizer.SortAlgorithms.RadixSortAnimation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public class SortButton extends Button {

    AlgorithmAnimation algorithm;

    int[] arr;
    FieldBox fieldBox;
    ResetButton resetButton;
    ToggleButton playPauseButton;

    public SortButton(FieldBox fieldBox, ButtonBox buttonBox) {
        this.fieldBox = fieldBox;
        this.setDisable(false);

        this.setText("Sort values");
        this.getStyleClass().add("Button");

        this.setOnAction(actionEvent -> {
            arr = this.fieldBox.getEnterFieldValues();

            // Check if the array is empty
            if (arr.length == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Insert values first");
                alert.show();
                return;
            }

            boolean invalidValues = false;
            algorithm = AlgorithmBox.getAlgorithm();
            for (int i : arr) {
                if (algorithm instanceof RadixSortAnimation) {
                    if (i < 10 || i > 50) {
                        invalidValues = true;
                    }
                } else {
                    if (i <= 0 || i > 50) {
                        invalidValues = true;
                    }
                }
            }
            if (invalidValues) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Invalid values entered");
                a.show();
            } else {
                try {
                    assert algorithm != null;
                    buttonBox.setCurrent(algorithm);
                    algorithm.setTransitionDuration(ButtonBox.animationDuration);
                    algorithm.setSPSize(arr.length);
                    this.setDisable(true);
                    this.playPauseButton.setDisable(false);
                    algorithm.startSort(arr);
                    algorithm.playAnimation();
                    this.resetButton.setDisable(false);
                } catch (NullPointerException e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("You must pick an algorithm to sort");
                    a.show();
                }
            }
        });
    }

    public void setResetButton(ResetButton resetButton) {
        this.resetButton = resetButton;
    }

    public void setPlayPauseButton(ToggleButton playPauseButton) { this.playPauseButton = playPauseButton;}
}
