package com.example.algorithmvisualizer.SortAlgorithms;

import com.example.algorithmvisualizer.view.ButtonBox;
import com.example.algorithmvisualizer.view.MainWindow;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public abstract class AlgorithmAnimation {
    List<Integer[]> transitions = new ArrayList<>();
    List<TranslateTransition> transitionList = new ArrayList<>();
    int[] spOrder;
    int spSize;
    int currentTransitionIndex = 0;
    Duration transitionDuration = Duration.millis(400);
    AlgorithmAnimation current = this;

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

        VBox firstBar = (VBox) MainWindow.chartPane.getChildren().get(spOrder[toLoc]);
        VBox secondBar = (VBox) MainWindow.chartPane.getChildren().get(spOrder[fromLoc]);

        addTranslateTransition(variance, firstBar, 1);
        addTranslateTransition(variance, secondBar, -1);

        swap(spOrder, toLoc, fromLoc);
    }

    final void addTranslateTransition(int variance, VBox bar, int direction) {
        TranslateTransition tt = new TranslateTransition(transitionDuration, bar);
        tt.setByX(25 * variance * direction);
        tt.setAutoReverse(false);
        transitionList.add(tt);
    }

    public AlgorithmAnimation getCurrent () { return current; }

    public final void setSPSize(int spSize) {
        this.spSize = spSize;
    }

    void playSequentialTransition() {
        if (transitionList.isEmpty()) return;
        playNextTransition();
    }

    private void playNextTransition() {
        if (currentTransitionIndex < transitionList.size()) {
            TranslateTransition currentTransition = transitionList.get(currentTransitionIndex);
            currentTransition.setOnFinished(event -> {
                if (!ButtonBox.isPaused) {
                    currentTransitionIndex++;
                    playNextTransition();
                    System.out.println(1);
                }
            });
            currentTransition.play();
        }
        if (currentTransitionIndex >= transitionList.size()) {
            ButtonBox.setPlayPauseDisabled();
            setRectanglesToGreen();
            ButtonBox.setPlayPauseDisabled();
        }
    }

    private void setRectanglesToGreen() {
        for (int i = 0; i < spSize; i++) {
            VBox bar = (VBox) MainWindow.chartPane.getChildren().get(i);
            Rectangle rect = (Rectangle) bar.getChildren().get(0); // Assuming rectangle is the first child
            rect.setFill(Color.LIGHTGREEN);
        }
    }

    public void continueTransition () {
        currentTransitionIndex++;
        playNextTransition();
        System.out.println(1);
    }

    public void setTransitionDuration(Duration duration) {
        this.transitionDuration = duration;
    }

    public abstract void startSort(int[] arr);
    public abstract void playAnimation();
}
