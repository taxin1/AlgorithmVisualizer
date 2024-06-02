package com.example.algorithmvisualizer.SortAlgorithms;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class QuickSortAnimation extends AlgorithmAnimation {
    List<Boolean> isPivot = new ArrayList<>();
    @Override
    public void startSort(int[] arr) {
        initializeSPOrder();
        sort(arr, 0, arr.length);
    }
    public void sort(int[] arr, int start, int end) {
        if (end - start < 2) {
            return;
        }
        int partitionIdx = partition(arr, start, end);

        sort(arr, start, partitionIdx);
        sort(arr, partitionIdx + 1, end);
    }

    private int partition(int[] arr, int start, int end) {

        int pivot = arr[start]; int startIdx = spOrder[start];
        int i = start;
        int j = end;
        while (i < j) {
            while (i < j && arr[--j] >= pivot ) { continue; }
            if (i < j) {
                arr[i] = arr[j];
                addTransition(i, j, start, false);
                isPivot.add(false);
            }

            while (i < j && arr[++i] <= pivot) { continue; }
            if (i < j) {
                arr[j] = arr[i];
                addTransition(j, i, start, false);
                isPivot.add(false);
            }
        }

        arr[i] = pivot;
        addTransition(i, startIdx, start, true);
        isPivot.add(true);

        return i;
    }

    private void addTransition(int to, int from, int start, boolean pivot){
        int variance;
        VBox movedBar;

        if(pivot){
            variance = start - to;
            movedBar = (VBox) MainWindow.chartPane.getChildren().get(from);
            spOrder[to] = from;
        }else{
            variance = from - to;
            movedBar = (VBox) MainWindow.chartPane.getChildren().get(spOrder[from]);
            spOrder[to] = spOrder[from];
        }

        addTranslateTransition(-variance, movedBar, 1);
    }

    public void playAnimation() {
        playSequentialTransition();
    }
}
