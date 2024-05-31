package com.example.algorithmvisualizer.PathAlgorithms;

import com.example.algorithmvisualizer.Controller;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.example.algorithmvisualizer.Utility.*;

public class DijkstraAlgorithm {

    public class QueueItem {
        // coordinates of node
        private String coordinates;
        // distance from start node
        private double distance;

        /**
         * Constructor for QueueItem
         * @param coordinates       coordinates of node
         * @param distance          distance from start node
         */
        public QueueItem(String coordinates, double distance) {
            this.coordinates = coordinates;
            this.distance = distance;
        }

        /**
         * Getter method for coordinates
         * @return coordinates      coordinates of node
         */
        public String getCoordinates() {
            return coordinates;
        }

        /**
         * Getter method for distance
         * @return distance         distance from start node
         */
        public double getDistance() {
            return distance;
        }
    }

    // coordinates of start node
    private int startRow;
    private int startColumn;
    // 2D array to represent grid
    private int [][] grid;
    // array to mark unvisited nodes
    private boolean [][] unvisited;
    // 2D array to record previous shortest path of nodes
    private String [][] prev;
    // priority queue for algorithm
    private PriorityQueue<QueueItem> pQueue;
    // direction vectors
    private int [] rowDir;
    private int [] columnDir;
    // controller for updating GUI
    private Controller controller;

    /**
     * Constructor for DijkstraAlgorithm
     * @param grid                  2D array to represent grid
     * @param startCoordinates      coordinates of start node
     */
    public DijkstraAlgorithm(int [][] grid, String startCoordinates) {
        this.grid = grid;
        unvisited = new boolean[DIM][DIM];
        prev = new String[DIM][DIM];
        // initialize all values in distances and unvisited
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
        // direction vectors
        rowDir = new int[] {-1, 0, 1, 0, -1, 1, 1, -1};
        columnDir = new int[] {0, 1, 0, -1, 1, 1, -1, -1};
        // get start coordinates
        String [] startSplit = startCoordinates.split(", ");
        startRow = Integer.parseInt(startSplit[0]);
        startColumn = Integer.parseInt(startSplit[1]);
        prev[startRow][startColumn] = "START";
        // comparator
        Comparator<QueueItem> comparator = (o1, o2) -> {
            if (o1.getDistance() < o2.getDistance())
                return -1;
            return 1;
        };
        pQueue = new PriorityQueue<>(comparator);
    }

    /**
     * Set controller to update GUI
     * @param controller        controller to update GUI
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Marks the shortest path on the grid by backtracking from target node in prev
     */
    private void markPath(int row, int column) {
        String previous = prev[row][column];
        if (grid[row][column] == VISITED_NODE) {
            controller.addToQueue(row, column, PATH_NODE);
            grid[row][column] = PATH_NODE;
            System.out.println(formatGrid(grid));
        } else if (grid[row][column] == START_NODE) {
            return;
        }
        String [] previousSplit = previous.split(", ");
        int previousRow = Integer.parseInt(previousSplit[0]);
        int previousColumn = Integer.parseInt(previousSplit[1]);
        markPath(previousRow, previousColumn);
    }

    /**
     * Validates if node at row and column is valid
     * @param row           row coordinate of node
     * @param column        column coordinate of node
     * @return boolean      true if valid else false
     */
    public boolean validator(int row, int column) {
        if (row >= 0 && row < DIM && column >= 0 && column < DIM && unvisited[row][column] && grid[row][column] != OBSTACLE_NODE)
            return true;
        return false;
    }

    /**
     * Calculates the distance between the previous item's coordinates and the current coordinates
     * @param row           row coordinate of current node
     * @param column        column coordinate of current node
     * @param item          previous item in queue
     * @return double       distance between the two nodes
     */
    public double calculateDistance(int row, int column, QueueItem item) {
        String [] coordinatesSplit = item.getCoordinates().split(", ");
        int prevRow = Integer.parseInt(coordinatesSplit[0]);
        int prevColumn = Integer.parseInt(coordinatesSplit[1]);
        return Math.sqrt(Math.pow(prevRow - row, 2) + Math.pow(prevColumn - column, 2));
    }

    /**
     * Looks into pQueue and checks if there is a node with a specific coordinate
     * @return ret      QueueItem if node with coordinate is in pQueue else null
     */
    private QueueItem containsNode(int row, int column) {
        for (QueueItem item : pQueue) {
            String [] coordinatesSplit = item.getCoordinates().split(", ");
            int checkRow = Integer.parseInt(coordinatesSplit[0]);
            int checkColumn = Integer.parseInt(coordinatesSplit[1]);
            if (row == checkRow && column == checkColumn)
                return item;
        }
        return null;
    }

    /**
     * Run the dijkstra algorithm on the grid
     * @return boolean      true if path from start node to target node is found else false
     */
    public void Dijkstra() {
        pQueue.add(new QueueItem(startRow + ", " + startColumn, 0));
        // keep calculating node distances while there are unvisited nodes
        while (!pQueue.isEmpty()) {
            System.out.println(formatGrid(grid));
            QueueItem item = pQueue.remove();
            // get the row and column coordinates of the current node in oldQueue
            String [] coordinates = item.getCoordinates().split(", ");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);

            // check if node is outside boundary or should not be visited
            if (!validator(row, column)) continue;

            // if the target is reached, recover steps
            if (grid[row][column] == TARGET_NODE) {
                controller.addToQueue(row, column, FOUND_NODE);
                markPath(row, column);
                return;
            }

            // mark visited if appropriate
            if (grid[row][column] == EMPTY_NODE) {
                grid[row][column] = VISITED_NODE;
                controller.addToQueue(row, column, VISITED_NODE);
            }

            // update unvisited
            unvisited[row][column] = false;

            // look through neighbours
            for (int i = 0; i < 8; i++) {
                int updatedRow = row + rowDir[i];
                int updatedColumn = column + columnDir[i];

                // create new item
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