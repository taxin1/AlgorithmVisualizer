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
                // No values entered, show an alert
                return;
            }

            MainWindow.chartPane.clearChart(); // Clear the chart pane

            // Update the chart pane with bars according to the values in the text fields
            for (int i = 0; i < arr.length; i++) {
                int height = arr[i];
                MainWindow.chartPane.updateBar(height, i);
            }

            this.setDisable(true); // Disable the reset button
            this.sortButton.setDisable(false); // Enable the sort button
            this.playPause.setDisable(true);
        });
    }

    public void setSortButton(SortButton sortButton) {
        this.sortButton = sortButton;
    }
    public void setPlayPause(ToggleButton playPause) { this.playPause = playPause; }
}
