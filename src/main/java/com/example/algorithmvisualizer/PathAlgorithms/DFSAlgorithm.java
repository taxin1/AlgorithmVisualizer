package com.example.algorithmvisualizer.PathAlgorithms;

import com.example.algorithmvisualizer.Controller;

import java.util.Arrays;

import static com.example.algorithmvisualizer.Utility.*;

public class DFSAlgorithm {

    // visited array for each row in grid
    private boolean [][] visited;
    // 2D array to represent grid
    private int [][] grid;
    // direction vectors
    private int rowDir[];
    private int columnDir[];
    // controller for updating gui
    private Controller controller;
    // boolean for whether or not the target node was found
    private boolean found;

    /**
     * Constructor for DFSAlgorithm
     */
    public DFSAlgorithm(int [][] grid) {
        visited = new boolean[DIM][DIM];
        for (int row = 0; row < DIM; row++)
            Arrays.fill(visited[row], false);
        this.grid = grid;
        rowDir = new int[] {-1, 0, 1, 0, -1, 1, 1, -1};
        columnDir = new int[] {0, 1, 0, -1, 1, 1, -1, -1};
        found = false;
    }

    /**
     * Set controller to update GUI and access controller methods
     * @param controller        controller to update GUI
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Validates that the node is within grid bounds or is an empty node
     * @param row               row coordinate of node
     * @param column            column coordinate of node
     * @return boolean          true if within bounds or epty node else false
     */
    private boolean validator(int row, int column) {
        if (row >= 0 && row < DIM && column >=0 && column < DIM && !visited[row][column] && grid[row][column] != OBSTACLE_NODE)
            return true;
        return false;
    }

    /**
     * Perform DFS on grid
     */
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
