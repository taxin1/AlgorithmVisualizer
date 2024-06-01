package com.example.algorithmvisualizer.SortAlgorithms;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public abstract class AlgorithmAnimation {
    List<Integer[]> transitions = new ArrayList<>();
    SequentialTransition sq = new SequentialTransition();
    int[] spOrder;
    int spSize;

    final void swap(int[] arr, int i, int j) {
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

    final void addTransition(int i, int j) {
        transitions.add(new Integer[]{i, j});
    }

    void initializeSPOrder() {
        spOrder = new int[spSize];
        for (int i = 0; i < spOrder.length; i++) {
            spOrder[i] = i;
        }
    }

    final void animateIterative(boolean isVariance, int transitionIdx) {
        int toLoc, fromLoc, variance;
        Integer[] currentTransition = transitions.get(transitionIdx);

        toLoc = currentTransition[0];
        fromLoc = currentTransition[1];

        if (isVariance) variance = fromLoc - toLoc;
        else variance = 1;

        StackPane firstBar = (StackPane) MainWindow.chartPane.getChildren().get(spOrder[toLoc]);
        StackPane secondBar = (StackPane) MainWindow.chartPane.getChildren().get(spOrder[fromLoc]);

        addTranslateTransition(variance, firstBar, 1);
        addTranslateTransition(variance, secondBar, -1);

        swap(spOrder, toLoc, fromLoc);
    }

    final void addTranslateTransition(int variance, StackPane bar, int direction) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), bar);
        tt.setByX(25 * variance * direction);
        tt.setAutoReverse(false);
        sq.getChildren().add(tt);
    }

    public final void setSPSize(int spSize) {
        this.spSize = spSize;
    }

    void playSequentialTransition() {
        sq.play();
    }

    public void pauseSequentialTransition() {
        sq.pause();
    }

    public void resumeSequentialTransition() {
        sq.play();
    }

    public abstract void startSort(int[] arr);
    public abstract void playAnimation();
}
