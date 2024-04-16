package com.example.algorithmvisualizer.view;

import javafx.scene.layout.BorderPane;

public class MainWindow extends BorderPane {
    public static ChartPane chartPane = new ChartPane();

    public MainWindow() {
        this.getStyleClass().add("pane");
        FieldBox fieldBox = new FieldBox();

        this.setTop(fieldBox);
        this.setCenter(chartPane);
        this.setBottom(new ButtonBox(fieldBox));
        this.setLeft(new AlgorithmBox(fieldBox));
    }
}
