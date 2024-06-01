package com.example.algorithmvisualizer.SortAlgorithms;

import com.example.algorithmvisualizer.view.MainWindow;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class RadixSortAnimation extends AlgorithmAnimation {
    private int[][] stackPanes;

    @Override
    public void startSort(int[] arr) {
        stackPanes = new int[arr.length][2];
        for (int i = 0; i < arr.length; i++) {
            stackPanes[i] = new int[]{arr[i], i};
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
            StackPane bar = (StackPane) MainWindow.chartPane.getChildren().get(index);
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
        int fromLoc = -1; // Initialize fromLoc to an invalid value
        Integer[] currentTransition = transitions.get(transitionIdx);
        toLoc = currentTransition[0];

        // Find the correct fromLoc considering duplicates
        int occurrence = 0;
        for (int i = 0; i < stackPanes.length; i++) {
            if (stackPanes[i][0] == currentTransition[1]) {
                if (stackPanes[i][1] == toLoc) {
                    occurrence++;
                } else {
                    fromLoc = stackPanes[i][1];
                    stackPanes[i][1] = toLoc;
                    break;
                }
            }
        }

        if (fromLoc == -1) {
            System.err.println("Error: fromLoc not found for value " + currentTransition[1] + " with occurrence " + occurrence);
            return;
        }

        variance = toLoc - fromLoc;

        // Correctly find and move the bar to the new location
        int barIndex = getBarIndex(currentTransition[1], occurrence);
        if (barIndex == -1) {
            System.err.println("Error: Bar not found for value " + currentTransition[1] + " with occurrence " + occurrence);
            return;
        }
        StackPane movedBar = (StackPane) MainWindow.chartPane.getChildren().get(barIndex);
        addTranslateTransition(variance, movedBar, 1);
    }
}
