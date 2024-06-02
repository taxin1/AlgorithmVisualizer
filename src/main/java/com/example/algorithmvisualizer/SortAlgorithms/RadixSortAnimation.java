package com.example.algorithmvisualizer.SortAlgorithms;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RadixSortAnimation extends AlgorithmAnimation {
    private int[][] VBoxs;

    @Override
    public void startSort(int[] arr) {
        VBoxs = new int[arr.length][2];
        for (int i = 0; i < arr.length; i++) {
            VBoxs[i] = new int[]{arr[i], i};
        }

        int n = arr.length;
        int maxDigit = getMaxDigit(arr);

        for (int digitIndex = 1; digitIndex <= maxDigit; digitIndex++) {
            int[] countingArray = new int[10];
            for (int value : arr) {
                countingArray[getDigit(value, digitIndex)]++;
            }

            for (int i = 1; i < countingArray.length; i++) {
                countingArray[i] += countingArray[i - 1];
            }

            int[] tmp = new int[arr.length];
            for (int k = n - 1; k >= 0; k--) {
                int digit = getDigit(arr[k], digitIndex);
                tmp[--countingArray[digit]] = arr[k];
                addTransition(countingArray[digit], arr[k]);
            }
            System.arraycopy(tmp, 0, arr, 0, arr.length);
        }
    }

    private int getMaxDigit(int[] arr) {
        int max = 0;
        for (int value : arr) {
            int digits = (int) Math.log10(value) + 1;
            if (digits > max) {
                max = digits;
            }
        }
        return max;
    }

    private int getDigit(int number, int index) {
        return (number / (int) Math.pow(10, index - 1)) % 10;
    }

    @Override
    public void playAnimation() {
        initializeSPOrder();
        for (int i = 0; i < transitions.size(); i++) {
            animateRecursive(i);
        }
        playSequentialTransition();
    }

    private int getBarIndex(int value, int occurrence) {
        int occurrenceCount = 0;
        for (int index = 0; index < spOrder.length; index++) {
            VBox bar = (VBox) MainWindow.chartPane.getChildren().get(index);
            if (Integer.parseInt(((Text) bar.getChildren().get(1)).getText()) == value) {
                if (occurrenceCount == occurrence) {
                    return index;
                }
                occurrenceCount++;
            }
        }
        return -1;
    }

    public void animateRecursive(int transitionIdx) {
        int toLoc, variance;
        int fromLoc = -1;
        Integer[] currentTransition = transitions.get(transitionIdx);
        toLoc = currentTransition[0];
        int occurrence = 0;
        for (int i = 0; i < VBoxs.length; i++) {
            if (VBoxs[i][0] == currentTransition[1]) {
                if (VBoxs[i][1] == toLoc) {
                    occurrence++;
                } else {
                    fromLoc = VBoxs[i][1];
                    VBoxs[i][1] = toLoc;
                    break;
                }
            }
        }

        if (fromLoc == -1) {
            System.err.println("Error: fromLoc not found for value " + currentTransition[1] + " with occurrence " + occurrence);
            return;
        }

        variance = toLoc - fromLoc;
        int barIndex = getBarIndex(currentTransition[1], occurrence);
        if (barIndex == -1) {
            System.err.println("Error: Bar not found for value " + currentTransition[1] + " with occurrence " + occurrence);
            return;
        }
        VBox movedBar = (VBox) MainWindow.chartPane.getChildren().get(barIndex);
        addTranslateTransition(variance, movedBar, 1);
    }
}
