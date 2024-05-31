package com.example.algorithmvisualizer.SortAlgorithms;

public class InsertionSortAnimation extends AlgorithmAnimation {
    @Override
    public void startSort(int[] arr) {
        sort(arr);
    }

    private void sort(int[] arr) {
        int newElement;
        for(int i = 1; i < arr.length; i++){
            newElement = arr[i];
            for(int j = i-1; j >= 0; j--){
                if(arr[j] > newElement){
                    arr[j+1] = arr[j];
                    arr[j] = newElement;
                    addTransition(j, j+1);
                }
            }
        }
    }
    @Override
    public void playAnimation() {
        initializeSPOrder();
        for(int i = 0; i < transitions.size(); i++) {
            animateIterative(false, i);
        }
        playSequentialTransition();

    }

}
