package com.example.algorithmvisualizer.PathAlgorithms;

import com.example.algorithmvisualizer.Controller;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.example.algorithmvisualizer.Utility.*;

public class AStarAlgorithm {

    public class QueueItem {
        private final String coordinates;
        private final double distanceToTarget;
        private final double fCost;
        private final double distance;

        public QueueItem(String coordinates, double distance, double distanceToTarget) {
            this.coordinates = coordinates;
            this.distance = distance;
            this.distanceToTarget = distanceToTarget;
            fCost = distanceToTarget + distance;
        }

        public String getCoordinates() {
            return coordinates;
        }

        public double getDistance() {
            return distance;
        }

        public double getDistanceToTarget() {
            return distanceToTarget;
        }

        public double getFCost() {
            return fCost;
        }
    }

    private final int[][] grid;
    private final String startCoordinates;
    private final int startRow;
    private final int startColumn;
    private final String targetCoordinates;
    private final int targetRow;
    private final int targetColumn;
    private final double[][] distancesToTarget;
    private final PriorityQueue<QueueItem> pQueue;
    private final boolean[][] processed;
    private final String[][] prev;
    private final int[] rowDir;
    private final int[] columnDir;
    private Controller controller;

    public AStarAlgorithm(int[][] grid, String startCoordinates, String targetCoordinates) {
        this.grid = grid;
        this.startCoordinates = startCoordinates;
        String[] startSplit = startCoordinates.split(", ");
        this.startRow = Integer.parseInt(startSplit[0]);
        this.startColumn = Integer.parseInt(startSplit[1]);
        this.targetCoordinates = targetCoordinates;
        String[] targetSplit = targetCoordinates.split(", ");
        this.targetRow = Integer.parseInt(targetSplit[0]);
        this.targetColumn = Integer.parseInt(targetSplit[1]);
        distancesToTarget = new double[DIM][DIM];
        processed = new boolean[DIM][DIM];
        prev = new String[DIM][DIM];
        for (int row = 0; row < DIM; row++) {
            for (int column = 0; column < DIM; column++) {
                if (grid[row][column] == OBSTACLE_NODE) {
                    processed[row][column] = true;
                    prev[row][column] = "X, X";
                } else {
                    processed[row][column] = false;
                    prev[row][column] = "O, O";
                }
            }
        }
        rowDir = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
        columnDir = new int[]{0, 1, 0, -1, 1, 1, -1, -1};
        calculateDistances();
        Comparator<QueueItem> comparator = (o1, o2) -> {
            if (o1.getFCost() <= o2.getFCost())
                return -1;
            return 1;
        };
        pQueue = new PriorityQueue<>(comparator);
    }

    public void setController(Controller controller) {
        this.controller = controller;
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

    private void markProcessed(QueueItem item) {
        String[] coordinatesSplit = item.getCoordinates().split(", ");
        int row = Integer.parseInt(coordinatesSplit[0]);
        int column = Integer.parseInt(coordinatesSplit[1]);

        processed[row][column] = true;
        if (grid[row][column] != TARGET_NODE && grid[row][column] != START_NODE) {
            grid[row][column] = VISITED_NODE;
            controller.addToQueue(row, column, VISITED_NODE);
            System.out.println(formatGrid(grid));
        }
    }

    private double calculateDistance(int row, int column, QueueItem item) {
        String[] coordinatesSplit = item.getCoordinates().split(", ");
        int prevRow = Integer.parseInt(coordinatesSplit[0]);
        int prevColumn = Integer.parseInt(coordinatesSplit[1]);

        return Math.sqrt(Math.pow(prevRow - row, 2) + Math.pow(prevColumn - column, 2));
    }

    private void lookAtNeighbours(QueueItem item) {
        String[] coordinatesSplit = item.getCoordinates().split(", ");
        int row = Integer.parseInt(coordinatesSplit[0]);
        int column = Integer.parseInt(coordinatesSplit[1]);
        for (int i = 0; i < 8; i++) {
            int updatedRow = row + rowDir[i];
            int updatedColumn = column + columnDir[i];
            if (updatedRow >= 0 && updatedRow < DIM && updatedColumn >= 0 && updatedColumn < DIM
                    && !processed[updatedRow][updatedColumn]
                    && distancesToTarget[updatedRow][updatedColumn] != -1) {
                QueueItem pendingItem = new QueueItem(
                        updatedRow + ", " + updatedColumn,
                        calculateDistance(updatedRow, updatedColumn, item) + item.getDistance(),
                        distancesToTarget[updatedRow][updatedColumn]
                );
                QueueItem compareItem = containsNode(updatedRow, updatedColumn);
                if (compareItem == null) {
                    prev[updatedRow][updatedColumn] = item.getCoordinates();
                    pQueue.add(pendingItem);
                } else if (pendingItem.getDistance() < compareItem.getDistance()) {
                    prev[updatedRow][updatedColumn] = item.getCoordinates();
                    pQueue.remove(compareItem);
                    pQueue.add(pendingItem);
                }
            }
        }
    }

    private void calculateDistances() {
        for (int row = 0; row < DIM; row++) {
            for (int column = 0; column < DIM; column++) {
                if (grid[row][column] != OBSTACLE_NODE) {
                    distancesToTarget[row][column] = Math.sqrt(Math.pow(targetRow - row, 2) + Math.pow(targetColumn - column, 2));
                } else {
                    distancesToTarget[row][column] = -1;
                }
            }
        }
    }

    private void markPath(int row, int column) {
        String previous = prev[row][column];
        if (grid[row][column] == VISITED_NODE) {
            grid[row][column] = PATH_NODE;
            controller.addToQueue(row, column, PATH_NODE);
            System.out.println(formatGrid(grid));
        } else if (grid[row][column] == START_NODE) {
            return;
        }
        String[] previousSplit = previous.split(", ");
        int previousRow = Integer.parseInt(previousSplit[0]);
        int previousColumn = Integer.parseInt(previousSplit[1]);
        markPath(previousRow, previousColumn);
    }

    public void AStar() {
        prev[startRow][startColumn] = "START";
        pQueue.add(new QueueItem(startCoordinates, 0, distancesToTarget[startRow][startColumn]));
        while (!pQueue.isEmpty()) {
            System.out.println();
            QueueItem removed = pQueue.remove();
            markProcessed(removed);
            if (removed.getCoordinates().equals(targetCoordinates)) {
                String[] targetCoordinatesSplit = targetCoordinates.split(", ");
                int targetRow = Integer.parseInt(targetCoordinatesSplit[0]);
                int targetColumn = Integer.parseInt(targetCoordinatesSplit[1]);
                controller.addToQueue(targetRow, targetColumn, FOUND_NODE);
                markPath(targetRow, targetColumn);
                return;
            }
            lookAtNeighbours(removed);
        }
    }

}
