package com.example.algorithmvisualizer;

import com.example.algorithmvisualizer.PathAlgorithms.AStarAlgorithm;
import com.example.algorithmvisualizer.PathAlgorithms.BFSAlgorithm;
import com.example.algorithmvisualizer.PathAlgorithms.DFSAlgorithm;
import com.example.algorithmvisualizer.PathAlgorithms.DijkstraAlgorithm;

import java.util.Arrays;

import static com.example.algorithmvisualizer.Utility.*;

public class Grid {
    private static int[][] grid;
    private String startCoordinates;
    private String targetCoordinates;
    private Controller controller;

    public Grid() {
        grid = new int[DIM][DIM];
        for (int[] row : grid)
            Arrays.fill(row, EMPTY_NODE);
        startCoordinates = "4, 4";
        grid[4][4] = START_NODE;
        targetCoordinates = (DIM - 5) + ", " + (DIM - 5);
        grid[DIM - 5][DIM - 5] = TARGET_NODE;
    }

    public void setPreset(int number) {
        switch (number) {
            case 1:
                grid = new int[][]
                        {
                                {3, 3, 0, 0, 3, 0, 3, 0, 0, 3, 0, 0, 3, 0, 3, 0, 0, 0, 0, 3},
                                {3, 3, 1, 3, 0, 0, 0, 0, 3, 3, 0, 3, 3, 0, 0, 3, 3, 3, 0, 3},
                                {0, 3, 0, 0, 3, 0, 0, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 3},
                                {0, 0, 0, 3, 0, 3, 0, 0, 3, 0, 0, 3, 3, 0, 3, 0, 3, 0, 0, 0},
                                {3, 3, 0, 0, 0, 0, 3, 0, 0, 3, 0, 3, 0, 0, 3, 0, 3, 0, 3, 3},
                                {0, 3, 0, 3, 3, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0},
                                {0, 0, 0, 0, 3, 3, 3, 3, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 3, 0},
                                {3, 3, 0, 0, 3, 0, 3, 0, 0, 3, 0, 0, 3, 0, 3, 0, 0, 0, 0, 3},
                                {3, 0, 0, 3, 0, 0, 0, 0, 3, 3, 0, 3, 3, 0, 0, 3, 3, 3, 0, 3},
                                {0, 3, 0, 0, 3, 0, 0, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 3},
                                {0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 3, 3, 0, 3, 0, 3, 0, 0, 0},
                                {3, 3, 0, 0, 0, 3, 0, 0, 3, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 3},
                                {3, 3, 0, 0, 3, 3, 3, 0, 3, 3, 3, 3, 3, 0, 3, 3, 3, 0, 3, 3},
                                {3, 3, 0, 0, 3, 3, 3, 0, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 3, 3},
                                {3, 3, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 3},
                                {3, 0, 0, 3, 0, 0, 0, 0, 3, 3, 0, 3, 3, 0, 0, 3, 3, 3, 0, 3},
                                {0, 3, 0, 0, 3, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 2, 0, 3, 0, 3},
                                {0, 0, 0, 3, 0, 3, 0, 0, 3, 0, 0, 3, 3, 0, 3, 0, 3, 0, 0, 0},
                                {3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3},
                                {3, 0, 0, 3, 0, 0, 0, 0, 3, 3, 0, 3, 3, 0, 0, 3, 3, 3, 0, 3}
                        };
                startCoordinates = "1, 2";
                targetCoordinates = "16, 15";
                break;
            case 2:
                grid = new int[][]
                        {
                                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                                {3, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 1, 3},
                                {3, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 3},
                                {3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3},
                                {3, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 3},
                                {3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 3},
                                {3, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3},
                                {3, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 3},
                                {3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 3},
                                {3, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 3},
                                {3, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 3},
                                {3, 2, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 3, 3},
                                {3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3},
                                {3, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 3},
                                {3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 3},
                                {3, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3},
                                {3, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 3},
                                {3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 3, 3},
                                {3, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 3},
                                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
                        };
                startCoordinates = "1, 18";
                targetCoordinates = "11, 1";
                break;
            case 3:
                grid = new int[][]
                        {
                                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                                {3, 1, 3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 0, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3},
                                {3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 3, 2, 3},
                                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
                        };
                startCoordinates = "1, 1";
                targetCoordinates = "18, 18";
                break;
            case 4:
                grid = new int[][]
                        {
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        };
                startCoordinates = "10, 0";
                targetCoordinates = "10, 19";
                break;
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void changeNode(int row, int column, int status) {
        grid[row][column] = status;
    }

    public boolean setStart(int row, int column) {
        if (grid[row][column] != TARGET_NODE) {
            String[] startSplit = startCoordinates.split(", ");
            int prevRow = Integer.parseInt(startSplit[0]);
            int prevColumn = Integer.parseInt(startSplit[1]);
            grid[prevRow][prevColumn] = EMPTY_NODE;
            grid[row][column] = START_NODE;
            startCoordinates = row + ", " + column;
            return true;
        }
        return false;
    }

    public boolean setTarget(int row, int column) {
        if (grid[row][column] != START_NODE) {
            String[] targetSplit = targetCoordinates.split(", ");
            int prevRow = Integer.parseInt(targetSplit[0]);
            int prevColumn = Integer.parseInt(targetSplit[1]);
            grid[prevRow][prevColumn] = EMPTY_NODE;
            grid[row][column] = TARGET_NODE;
            targetCoordinates = row + ", " + column;
            return true;
        }
        return false;
    }

    public void clearObstacles() {
        for (int row = 0; row < DIM; row++) {
            for (int column = 0; column < DIM; column++) {
                if (grid[row][column] == OBSTACLE_NODE)
                    grid[row][column] = EMPTY_NODE;
            }
        }
    }

    public void clearPath() {
        for (int row = 0; row < DIM; row++) {
            for (int column = 0; column < DIM; column++) {
                if (grid[row][column] == VISITED_NODE || grid[row][column] == PATH_NODE)
                    grid[row][column] = EMPTY_NODE;
            }
        }
    }

    public void clearEverything() {
        clearObstacles();
        clearPath();
    }

    public int getNode(int row, int column) {
        return grid[row][column];
    }

    public int[][] getGrid() {
        return grid;
    }

    public void performBFS() {
        BFSAlgorithm BFSObj = new BFSAlgorithm(grid, startCoordinates);
        if (controller != null) {
            BFSObj.setController(controller);
            BFSObj.BFS();
        }
    }

    public void performDFS() {
        DFSAlgorithm DFSObj = new DFSAlgorithm(grid);
        if (controller != null) {
            DFSObj.setController(controller);
            String[] coordinates = startCoordinates.split(", ");
            DFSObj.DFS(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        }
    }

    public void performDijkstra() {
        DijkstraAlgorithm DijkstraObj = new DijkstraAlgorithm(grid, startCoordinates);
        if (controller != null) {
            DijkstraObj.setController(controller);
            DijkstraObj.Dijkstra();
        }
    }

    public void performAStar() {
        AStarAlgorithm AStarObj = new AStarAlgorithm(grid, startCoordinates, targetCoordinates);
        if (controller != null) {
            AStarObj.setController(controller);
            AStarObj.AStar();
        }
    }

}
