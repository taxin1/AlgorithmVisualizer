package com.example.algorithmvisualizer.algorithms;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AlgorithmAnimation {
    List<Integer[]> transitions = new ArrayList<>();

    SequentialTransition sq = new SequentialTransition();

    int[] spOrder;

    int spSize;

    final void swap(int[] arr, int i, int j){
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

    final void addTransition(int i, int j) {
        transitions.add(new Integer[]{i, j});
    }

    void initializeSPOrder(){
        spOrder = new int[spSize];
        for(int i = 0; i < spOrder.length; i++){
            spOrder[i] = i;
        }
    }

    final void animateIterative(boolean isVariance, int transitionIdx){
        int toLoc, fromLoc, variance;
        Integer[] currentTransition = transitions.get(transitionIdx);

        toLoc = currentTransition[0];
        fromLoc = currentTransition[1];

        if(isVariance) variance = fromLoc - toLoc;
        else variance = 1;

        StackPane firstBar = (StackPane) MainWindow.chartPane.getChildren().get(spOrder[toLoc]);
        StackPane secondBar = (StackPane) MainWindow.chartPane.getChildren().get(spOrder[fromLoc]);

        addTranslateTransition(variance, firstBar, 1);
        addTranslateTransition(variance, secondBar, -1);

        swap(spOrder, toLoc, fromLoc);
    }

    final void addTranslateTransition(int variance, StackPane bar, int direction){
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), bar);
        tt.setByX(25 * variance * direction);
        tt.setAutoReverse(false);
        sq.getChildren().add(tt);
    }

    public final void setSPSize(int spSize){
        this.spSize = spSize;
    }

    void playSequentialTransition(){
        sq.play();
    }

    public void resetBars(int[] arr){
        int currentIndex, variance;
        SequentialTransition resetTransition = new SequentialTransition();

        int[] sortedArray = Arrays.stream(arr).sorted().toArray();
        List<Integer> sortedArrList = new ArrayList<>();
        for(int i : sortedArray) sortedArrList.add(i);

        for(int i = 0; i < arr.length; i++){
            StackPane currentPane = (StackPane) MainWindow.chartPane.getChildren().get(i);

            currentIndex = sortedArrList.indexOf(arr[i]);

            variance = i - currentIndex;

            sortedArrList.set(currentIndex, -1);

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), currentPane);
            tt.setByX(25 * variance);
            tt.setAutoReverse(false);
            resetTransition.getChildren().add(tt);
        }
        resetTransition.play();
    }

    public abstract void startSort(int[] arr);
    public abstract void playAnimation();
}
