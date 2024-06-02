package com.example.algorithmvisualizer;

import javafx.scene.paint.Color;

public class Utility {
    public final static int DIM = 20;
    public final static int EMPTY_NODE = 0;
    public final static Color EMPTY_COLOR = Color.WHITE;
    public final static int START_NODE = 1;
    public final static Color START_COLOR = Color.RED;
    public final static int TARGET_NODE = 2;
    public final static Color TARGET_COLOR = Color.BLUE;
    public final static int OBSTACLE_NODE = 3;
    public final static Color OBSTACLE_COLOR = Color.BLACK;
    public final static int VISITED_NODE = 4;
    public final static Color VISITED_COLOR = Color.GREY;
    public final static int PATH_NODE = 5;
    public final static Color PATH_COLOR = Color.LIGHTGREEN;
    public final static int FOUND_NODE = 6;
    public final static Color FOUND_COLOR = Color.YELLOW;

    public static String formatGrid(int[][] grid) {
        String ret = "";
        for (int row = 0; row < DIM; row++) {
            for (int column = 0; column < DIM; column++) {
                ret = ret + grid[row][column] + " ";
                if (column == DIM - 1) ret += "\n";
            }
        }
        return ret;
    }

}