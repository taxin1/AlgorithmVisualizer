package com.example.algorithmvisualizer.view;

import com.example.algorithmvisualizer.SortAlgorithms.AlgorithmAnimation ;
import javafx.scene.control.Button;

public class ResetButton extends Button {

    AlgorithmAnimation algorithm;
    FieldBox fieldBox;
    SortButton sortButton;
    int[] arr;
    public ResetButton(FieldBox fieldBox){
        this.fieldBox = fieldBox;
        this.setText("Reset Values");
        this.getStyleClass().add("button");
        this.setDisable(true);
        this.setOnAction(actionEvent -> {
            arr = this.fieldBox.getEnterFieldValues();
            this.algorithm = AlgorithmBox.getAlgorithm();
            algorithm.resetBars(arr);
            this.setDisable(true);
            this.sortButton.setDisable(false);
        });
    }
    public void setSortButton(SortButton sortButton){
        this.sortButton = sortButton;
    }
}
