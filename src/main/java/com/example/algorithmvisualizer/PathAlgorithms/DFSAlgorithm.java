package com.example.algorithmvisualizer.PathAlgorithms;

import com.example.algorithmvisualizer.Controller;

import java.util.Arrays;

import static com.example.algorithmvisualizer.Utility.*;

public class DFSAlgorithm {

    private final boolean[][] visited;
    private final int[][] grid;
    private final int[] rowDir;
    private final int[] columnDir;
    private Controller controller;
    private boolean found;

    public DFSAlgorithm(int[][] grid) {
        visited = new boolean[DIM][DIM];
        for (int row = 0; row < DIM; row++)
            Arrays.fill(visited[row], false);
        this.grid = grid;
        rowDir = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
        columnDir = new int[]{0, 1, 0, -1, 1, 1, -1, -1};
        found = false;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private boolean validator(int row, int column) {
        return row >= 0 && row < DIM && column >= 0 && column < DIM && !visited[row][column] && grid[row][column] != OBSTACLE_NODE;
    }

    public void DFS(int row, int column) {
        if (found) return;

        int d_row, d_column;
        visited[row][column] = true;
        if (grid[row][column] == EMPTY_NODE) {
            grid[row][column] = VISITED_NODE;
            System.out.println(formatGrid(grid));
            controller.addToQueue(row, column, VISITED_NODE);
        } else if (grid[row][column] == TARGET_NODE) {
            found = true;
            controller.addToQueue(row, column, FOUND_NODE);
            return;
        }

        for (int i = 0; i < 8; i++) {
            d_row = row + rowDir[i];
            d_column = column + columnDir[i];

            if (validator(d_row, d_column)) {
                DFS(d_row, d_column);
            }
        }
    }

}
