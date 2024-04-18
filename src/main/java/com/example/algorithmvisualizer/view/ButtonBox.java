package com.example.algorithmvisualizer.view;

import javafx.scene.layout.HBox;
public class ButtonBox extends HBox {
    ResetButton resetButton;
    SortButton sortButton;
    BackButton backButton;


    public ButtonBox(FieldBox fieldBox) {
        this.getStyleClass().add("buttonbox");

        sortButton = new SortButton(fieldBox);
        resetButton = new ResetButton(fieldBox);
        backButton = new BackButton();

        this.sortButton.getStyleClass().add("button");
        this.resetButton.getStyleClass().add("button");
        this.backButton.getStyleClass().add("button");

        resetButton.setSortButton(sortButton);
        sortButton.setResetButton(resetButton);

        this.getChildren().add(sortButton);
        this.getChildren().add(resetButton);
        this.getChildren().add(backButton);
    }

}
