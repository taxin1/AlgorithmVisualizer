package com.example.algorithmvisualizer.view;

import com.example.algorithmvisualizer.SortAlgorithms.AlgorithmAnimation ;
import com.example.algorithmvisualizer.SortAlgorithms.RadixSortAnimation ;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
public class SortButton extends Button {

    AlgorithmAnimation algorithm;

    int[] arr;
    FieldBox fieldBox;
    ResetButton resetButton;

    public SortButton(FieldBox fieldBox){
        this.fieldBox = fieldBox;
        this.setDisable(false);

        this.setText("Sort values");
        this.getStyleClass().add("Button");

        this.setOnAction(actionEvent -> {
            arr = this.fieldBox.getEnterFieldValues();
            boolean invalidValues = false;
            algorithm = AlgorithmBox.getAlgorithm();
            for(int i : arr){
                if(algorithm instanceof RadixSortAnimation){
                    if(i < 10 || i > 50){
                        invalidValues = true;
                    }
                }else {
                    if (i <= 0 || i > 50) {
                        invalidValues = true;
                    }
                }
            }
            if(invalidValues){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Invalid values entered");
                a.show();
            }else {
                try {
                    algorithm.setSPSize(arr.length);
                    this.setDisable(true);
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

    public void setResetButton(ResetButton resetButton){
        this.resetButton = resetButton;
    }
}
