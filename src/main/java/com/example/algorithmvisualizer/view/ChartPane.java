package com.example.algorithmvisualizer.view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ChartPane extends HBox {

    public ChartPane() {
        this.getStyleClass().add("chartpane");
    }

    public void updateBar(int height, int index) {
        if (this.getChildren().size() <= index) {
            Text text = new Text(Integer.toString(height));
            Rectangle rect = new Rectangle(20, height * 9);
            rect.setFill(Color.CORNFLOWERBLUE);
            rect.setArcWidth(10);
            rect.setArcHeight(10);

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BOTTOM_CENTER);
            vbox.getChildren().addAll(rect, text);

            this.getChildren().add(vbox);
        } else {
            VBox currentPane = (VBox) this.getChildren().get(index);
            ((Rectangle) currentPane.getChildren().get(0)).setHeight(height * 10);
            ((Text) currentPane.getChildren().get(1)).setText(Integer.toString(height));
        }
    }

    public void clearChart() {
        this.getChildren().clear();
    }
}
