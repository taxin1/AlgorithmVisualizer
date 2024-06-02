package com.example.algorithmvisualizer.PathAlgorithms;

import com.example.algorithmvisualizer.Controller;

import java.util.LinkedList;
import java.util.Queue;

import static com.example.algorithmvisualizer.Utility.*;

public class BFSAlgorithm {
    private Controller controller;
    private final int[][] grid;
    private final String startCoordinates;
    private boolean[][] visited;
    private final int[] rowDir;
    private final int[] columnDir;

    public BFSAlgorithm(int[][] grid, String startCoordinates) {
        this.grid = grid;
        this.startCoordinates = startCoordinates;
        visited = new boolean[DIM][DIM];
        rowDir = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
        columnDir = new int[]{0, 1, 0, -1, 1, 1, -1, -1};
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private boolean validator(int row, int column) {
        return row >= 0 && row < DIM && column >= 0 && column < DIM && grid[row][column] != OBSTACLE_NODE && !visited[row][column];
    }

    public void BFS() {
        visited = new boolean[DIM][DIM];
        Queue<String> queue = new LinkedList<>();
        queue.add(startCoordinates);
        while (!queue.isEmpty()) {
            System.out.println(formatGrid(grid));
            String[] coordinates = queue.remove().split(", ");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            if (!validator(row, column)) continue;
            visited[row][column] = true;

            for (int i = 0; i < 8; i++) {
                int d_row = row + rowDir[i];
                int d_column = column + columnDir[i];
                if (validator(d_row, d_column)) {
                    queue.add(d_row + ", " + d_column);
                    if (grid[d_row][d_column] == EMPTY_NODE) {
                        grid[d_row][d_column] = VISITED_NODE;
                        controller.addToQueue(d_row, d_column, VISITED_NODE);
                    } else if (grid[d_row][d_column] == TARGET_NODE) {
                        controller.addToQueue(d_row, d_column, FOUND_NODE);
                        return;
                    }
                }
            }
        }
    }

}
