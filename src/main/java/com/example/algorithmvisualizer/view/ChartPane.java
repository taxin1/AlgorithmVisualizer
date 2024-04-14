package com.example.algorithmvisualizer.view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ChartPane extends HBox {

    public ChartPane(){
        this.getStyleClass().add("hbox");
    }
    public void updateBar(int height, int index){
        if(this.getChildren().size() <= index){
            Text text = new Text(Integer.toString(height));
            Rectangle rect = new Rectangle(20, height * 10);
            rect.setFill(Color.CORNFLOWERBLUE);

            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.BOTTOM_CENTER);
            stackPane.getChildren().addAll(rect, text);

            this.getChildren().add(stackPane);
        }else {
            StackPane currentPane = (StackPane) this.getChildren().get(index);
            ((Rectangle) currentPane.getChildren().get(0)).setHeight(height * 10);
            ((Text) currentPane.getChildren().get(1)).setText(Integer.toString(height));
        }
    }

}
