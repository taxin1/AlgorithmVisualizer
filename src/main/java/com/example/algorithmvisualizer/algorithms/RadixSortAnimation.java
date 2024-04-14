package com.example.algorithmvisualizer.algorithms;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class RadixSortAnimation extends AlgorithmAnimation{
    private int[][] stackPanes;
    @Override
    public void startSort(int[] arr) {
        stackPanes = new int[arr.length][2];
        for(int i = 0; i < arr.length; i++){
            stackPanes[i] = new int[]{arr[i], i};
        }

        int n = arr.length;
        for (int j = 1; j <= 2; j++) {
            int[] countingArray = new int[10];
            for (int value : arr) {
                countingArray[getDigit(value, j)]++;
            }

            for (int i = 1; i < countingArray.length; i++) {
                countingArray[i] += countingArray[i-1];
            }

            int[] tmp = new int[arr.length];
            for (int k = n - 1; k >= 0; k--) {
                tmp[--countingArray[getDigit(arr[k], j)]] = arr[k];
                addTransition(countingArray[getDigit(arr[k], j)], arr[k]);
            }
            System.arraycopy(tmp, 0, arr, 0, arr.length);
        }
    }

    private int getDigit(int number, int index){
        return (int) (number / Math.pow(10, index-1)) % 10;
    }

    @Override
    public void playAnimation() {
        initializeSPOrder();
        for(int i = 0; i < transitions.size(); i++) {
            animateRecursive(i);
        }
        playSequentialTransition();
    }

    private int getBarIndex(int value){
        for(int index = 0; index < spOrder.length ; index++){
            StackPane bar = (StackPane) MainWindow.chartPane.getChildren().get(index);
            if (Integer.parseInt(((Text) bar.getChildren().get(1)).getText()) == value) {
                return index;
            }

        }
        return -1;
    }
    public void animateRecursive(int transitionIdx){
        int toLoc, variance;
        int fromLoc = 0;
        Integer[] currentTransition = transitions.get(transitionIdx);
        toLoc = currentTransition[0];
        for(int[] i : stackPanes){
            if(i[0] == currentTransition[1]){
                fromLoc = i[1];
                i[1] = toLoc;
                break;
            }
        }
        variance = toLoc - fromLoc;

        StackPane movedBar = (StackPane) MainWindow.chartPane.getChildren().get(getBarIndex(currentTransition[1]));
        addTranslateTransition(variance, movedBar, 1);
    }


}
