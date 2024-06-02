package com.example.algorithmvisualizer.SortAlgorithms;

public class ShellSortAnimation extends AlgorithmAnimation {
    @Override
    public void startSort(int[] arr) {
        sort(arr);
    }

    private void sort(int[] arr) {
        int j, newElement;
        int gap = arr.length / 2;

        while (gap > 0) {
            for (int i = gap; i < arr.length; i++) {
                newElement = arr[i];
                for (j = i - gap; j >= 0; j = j - gap) {
                    if (newElement < arr[j]) {
                        arr[j + gap] = arr[j];
                        arr[j] = newElement;
                        addTransition(j + gap, j);
                    }
                }
            }
            gap = gap / 2;
        }
    }

    @Override
    public void playAnimation() {
        initializeSPOrder();
        for (int i = 0; i < transitions.size(); i++) {
            animateIterative(true, i);
        }
        playSequentialTransition();
    }
}
