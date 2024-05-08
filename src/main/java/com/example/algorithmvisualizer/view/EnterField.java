package com.example.algorithmvisualizer.view;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class EnterField extends TextField {
    private FieldBox fieldBox;

    public EnterField(FieldBox fieldBox){
        this.fieldBox = fieldBox;

        this.setEditable(false);
        this.getStyleClass().add("text-field");

        addChangeListener();
        addFocusedListener();
    }

    private void addFocusedListener(){
        this.focusedProperty().addListener((observableValue, s, t1) -> {
            if(!t1) {
                updateChartBar();
            }
        });
    }

    private void addChangeListener(){
        this.textProperty().addListener((observableValue, oldValue, newValue) -> {
            setEditableField(!newValue.isEmpty());
        });
    }
    private void setEditableField(boolean isEditable){
        int index = fieldBox.getChildren().indexOf(this);
        if(index < 15) {
            EnterField nextField = fieldBox.getTextField(index + 1);
            nextField.setEditable(isEditable);
        }
    }
    private void updateChartBar(){
        String text = this.getText();
        try {
            if (!text.equals("")) {
                int textValue = Integer.parseInt(text);
                if (AlgorithmBox.getSelectedIndices().equals("Radix Sort")) {
                    if (textValue >= 10 && textValue <= 50) {
                        this.setStyle("-fx-text-fill: black");
                        this.setTooltip(null);
                        MainWindow.chartPane.updateBar(Integer.parseInt(this.getText()), fieldBox.getChildren().indexOf(this) - 1);
                    } else {
                        Tooltip tooltip = new Tooltip("Value must be\nbetween 10-50");
                        this.setStyle("-fx-text-fill: red");
                        this.setTooltip(tooltip);
                    }
                } else {
                    if (textValue >= 0 && textValue <= 50) {
                        this.setStyle("-fx-text-fill: black");
                        this.setTooltip(null);
                        MainWindow.chartPane.updateBar(Integer.parseInt(this.getText()), fieldBox.getChildren().indexOf(this) - 1);
                    } else {
                        Tooltip tooltip = new Tooltip("Value must be\nbetween 0-50");
                        this.setStyle("-fx-text-fill: red");
                        this.setTooltip(tooltip);
                    }
                }
            }
        }catch(NumberFormatException e){
            Tooltip tooltip = new Tooltip("Value must be\nan integer");
            this.setStyle("-fx-text-fill: red");
            this.setTooltip(tooltip);
        }
    }

}
