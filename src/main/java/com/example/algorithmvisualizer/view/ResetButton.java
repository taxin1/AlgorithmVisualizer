package com.example.algorithmvisualizer.view;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public class ResetButton extends Button {

    FieldBox fieldBox;
    SortButton sortButton;
    ToggleButton playPause;

    public ResetButton(FieldBox fieldBox) {
        this.fieldBox = fieldBox;
        this.setText("Reset Bars");
        this.getStyleClass().add("button");
        this.setDisable(true);

        this.setOnAction(actionEvent -> {
            int[] arr = this.fieldBox.getEnterFieldValues();
            if (arr.length == 0) {
                return;
            }

            MainWindow.chartPane.clearChart();
            for (int i = 0; i < arr.length; i++) {
                int height = arr[i];
                MainWindow.chartPane.updateBar(height, i);
            }

            this.setDisable(true);
            this.sortButton.setDisable(false);
            this.playPause.setDisable(true);
        });
    }

    public void setSortButton(SortButton sortButton) {
        this.sortButton = sortButton;
    }

    public void setPlayPause(ToggleButton playPause) {
        this.playPause = playPause;
    }
}
