package com.example.algorithmvisualizer.view;

import javafx.scene.layout.BorderPane;

public class MainWindow extends BorderPane {
    public static ChartPane chartPane = new ChartPane();

    public MainWindow() {
        this.getStyleClass().add("pane");
        this.setStyle("-fx-hgap: 5; -fx-padding: 10; -fx-spacing: 5;");

        FieldBox fieldBox = new FieldBox();
        fieldBox.getStyleClass().add("hbox");
        fieldBox.setStyle("-fx-spacing: 5; -fx-alignment: bottom-center; -fx-min-height: 30;");

        ButtonBox buttonBox = new ButtonBox(fieldBox);
        buttonBox.getStyleClass().add("buttonbox");
        buttonBox.setStyle("-fx-spacing: 5; -fx-min-height: 30;");

        AlgorithmBox algorithmBox = new AlgorithmBox(fieldBox);
        algorithmBox.getStyleClass().add("vbox");
        algorithmBox.setStyle("-fx-border-width: 0; -fx-alignment: center; -fx-max-width: 80;");

        this.setTop(fieldBox);
        this.setCenter(chartPane);
        this.setBottom(buttonBox);
        this.setLeft(algorithmBox);
    }
}
