package com.example.algorithmvisualizer.SortAlgorithms;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class MergeSortAnimation extends AlgorithmAnimation {
    private int[][] VBoxs;
    @Override
    public void startSort(int[] arr) {
        sort(arr, 0, arr.length);
    }

    private void sort(int[] arr, int start, int end){

        VBoxs = new int[arr.length][2];
        if(end - start < 2){
            return;
        }
        int mid = (end + start) / 2;
        sort(arr, start, mid);
        sort(arr, mid, end);
        merge(arr, start, mid, end);
    }

    private void merge(int[] arr, int start, int mid, int end){
        if(arr[mid - 1] < arr[mid]){
            return;
        }
        int i = start, toIdx = start, j = mid, tempIndex = 0;
        int[] tempArray = new int[end - start];
        while(i < mid && j < end){
            if(arr[i] < arr[j]){
                addTransition(toIdx++, i);
                tempArray[tempIndex++] = arr[i++];
            }else{
                addTransition(toIdx++, j);
                tempArray[tempIndex++] = arr[j++];
            }
        }
        while(i < mid){
            addTransition(toIdx++, i);
            tempArray[tempIndex++] = arr[i++];
        }
        while(j < end){
            addTransition(toIdx++, j);
            tempArray[tempIndex++] = arr[j++];
        }
        System.arraycopy(tempArray, 0, arr, start, tempArray.length);
    }
    @Override
    public void playAnimation() {
        initializeSPOrder();
        for(int i = 0; i < transitions.size(); i++) {
            animateRecursive(i);
        }
        playSequentialTransition();
    }
    @Override
    void initializeSPOrder(){
        for(int i = 0; i < VBoxs.length; i++){
            //initialize bar locations - {barIdx, barLoc}
            VBoxs[i] = new int[]{i, i};
        }
    }
    private int getBarLoc(int loc){
        List<int[]> matches = new ArrayList<>();
        int[] max;
        for(int[] arr : VBoxs){
            if(arr[1] == loc){
                matches.add(arr);
            }
        }
        if(matches.size() == 1){
            return matches.get(0)[0];
        }else{
            max = matches.get(0);
            for(int[] arr : matches){
                if(getBarValue(arr[0]) > getBarValue(max[0]))
                    max = arr;
            }
        }
        return max[0];
    }
    private int getBarValue(int idx){
        VBox sp = (VBox) MainWindow.chartPane.getChildren().get(idx);
        Text txt = (Text) sp.getChildren().get(1);
        return Integer.parseInt(txt.getText());
    }
    private void animateRecursive(int transitionIdx){

        int toLoc, fromLoc, variance;
        Integer[] currentTransition = transitions.get(transitionIdx);

        toLoc = currentTransition[0];
        fromLoc = currentTransition[1];
        variance = fromLoc - toLoc;
        if(variance != 0) {
            int idx = getBarLoc(fromLoc);
            VBox movedBar = (VBox) MainWindow.chartPane.getChildren().get(idx);

            addTranslateTransition(-variance, movedBar, 1);
            VBoxs[idx][1] = toLoc;
        }
    }

}
