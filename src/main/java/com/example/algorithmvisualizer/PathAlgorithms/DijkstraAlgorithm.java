package com.example.algorithmvisualizer.PathAlgorithms;

import com.example.algorithmvisualizer.Controller;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.example.algorithmvisualizer.Utility.*;

public class DijkstraAlgorithm {

    public class QueueItem {
        private final String coordinates;
        private final double distance;

        public QueueItem(String coordinates, double distance) {
            this.coordinates = coordinates;
            this.distance = distance;
        }

        public String getCoordinates() {
            return coordinates;
        }

        public double getDistance() {
            return distance;
        }
    }

    private final int startRow;
    private final int startColumn;
    private final int[][] grid;
    private final boolean[][] unvisited;
    private final String[][] prev;
    private final PriorityQueue<QueueItem> pQueue;
    private final int[] rowDir;
    private final int[] columnDir;
    private Controller controller;

    public DijkstraAlgorithm(int[][] grid, String startCoordinates) {
        this.grid = grid;
        unvisited = new boolean[DIM][DIM];
        prev = new String[DIM][DIM];
        for (int row = 0; row < DIM; row++) {
            for (int column = 0; column < DIM; column++) {
                if (grid[row][column] == OBSTACLE_NODE) {
                    unvisited[row][column] = false;
                    prev[row][column] = "X, X";
                } else {
                    unvisited[row][column] = true;
                    prev[row][column] = "O, O";
                }
            }
        }
        rowDir = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
        columnDir = new int[]{0, 1, 0, -1, 1, 1, -1, -1};
        String[] startSplit = startCoordinates.split(", ");
        startRow = Integer.parseInt(startSplit[0]);
        startColumn = Integer.parseInt(startSplit[1]);
        prev[startRow][startColumn] = "START";
        Comparator<QueueItem> comparator = (o1, o2) -> {
            if (o1.getDistance() < o2.getDistance())
                return -1;
            return 1;
        };
        pQueue = new PriorityQueue<>(comparator);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void markPath(int row, int column) {
        String previous = prev[row][column];
        if (grid[row][column] == VISITED_NODE) {
            controller.addToQueue(row, column, PATH_NODE);
            grid[row][column] = PATH_NODE;
            System.out.println(formatGrid(grid));
        } else if (grid[row][column] == START_NODE) {
            return;
        }
        String[] previousSplit = previous.split(", ");
        int previousRow = Integer.parseInt(previousSplit[0]);
        int previousColumn = Integer.parseInt(previousSplit[1]);
        markPath(previousRow, previousColumn);
    }

    public boolean validator(int row, int column) {
        return row >= 0 && row < DIM && column >= 0 && column < DIM && unvisited[row][column] && grid[row][column] != OBSTACLE_NODE;
    }

    public double calculateDistance(int row, int column, QueueItem item) {
        String[] coordinatesSplit = item.getCoordinates().split(", ");
        int prevRow = Integer.parseInt(coordinatesSplit[0]);
        int prevColumn = Integer.parseInt(coordinatesSplit[1]);
        return Math.sqrt(Math.pow(prevRow - row, 2) + Math.pow(prevColumn - column, 2));
    }

    private QueueItem containsNode(int row, int column) {
        for (QueueItem item : pQueue) {
            String[] coordinatesSplit = item.getCoordinates().split(", ");
            int checkRow = Integer.parseInt(coordinatesSplit[0]);
            int checkColumn = Integer.parseInt(coordinatesSplit[1]);
            if (row == checkRow && column == checkColumn)
                return item;
        }
        return null;
    }

    public void Dijkstra() {
        pQueue.add(new QueueItem(startRow + ", " + startColumn, 0));
        while (!pQueue.isEmpty()) {
            System.out.println(formatGrid(grid));
            QueueItem item = pQueue.remove();
            String[] coordinates = item.getCoordinates().split(", ");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            if (!validator(row, column)) continue;
            if (grid[row][column] == TARGET_NODE) {
                controller.addToQueue(row, column, FOUND_NODE);
                markPath(row, column);
                return;
            }
            if (grid[row][column] == EMPTY_NODE) {
                grid[row][column] = VISITED_NODE;
                controller.addToQueue(row, column, VISITED_NODE);
            }
            unvisited[row][column] = false;
            for (int i = 0; i < 8; i++) {
                int updatedRow = row + rowDir[i];
                int updatedColumn = column + columnDir[i];
                QueueItem pendingItem = new QueueItem(
                        updatedRow + ", " + updatedColumn,
                        calculateDistance(updatedRow, updatedColumn, item) + item.getDistance()
                );

                QueueItem compareItem = containsNode(updatedRow, updatedColumn);
                if (compareItem == null && validator(updatedRow, updatedColumn)) {
                    pQueue.add(pendingItem);
                    prev[updatedRow][updatedColumn] = item.getCoordinates();
                } else if (pendingItem.getDistance() < item.getDistance()) {
                    prev[updatedRow][updatedColumn] = item.getCoordinates();
                    pQueue.remove(compareItem);
                    pQueue.add(pendingItem);
                }
            }
        }
    }

}