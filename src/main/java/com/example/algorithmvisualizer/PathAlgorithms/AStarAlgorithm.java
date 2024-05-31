package com.example.algorithmvisualizer.PathAlgorithms;

import com.example.algorithmvisualizer.Controller;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.example.algorithmvisualizer.Utility.*;

public class AStarAlgorithm {

    public class QueueItem {
        // coordinates of the node
        private String coordinates;
        // distance left to target node in a straight line
        private double distanceToTarget;
        // cost to influence search path
        private double fCost;
        // total distance so far
        private double distance;

        /**
         * Constructor for QueueItem for a comparator to be used in pQueue
         * @param coordinates           coordinates of the node
         * @param distance              current distance from start node
         * @param distanceToTarget      distance left to target node in a straight line
         */
        public QueueItem(String coordinates, double distance, double distanceToTarget) {
            this.coordinates = coordinates;
            this.distance = distance;
            this.distanceToTarget = distanceToTarget;
            fCost = distanceToTarget + distance;
        }

        /**
         * Getter method for coordinates
         * @return coordinates          coordinates of the node
         */
        public String getCoordinates() {
            return coordinates;
        }

        /**
         * Getter method for distance
         * @return distance            distanceFromStart from start node
         */
        public double getDistance() {
            return distance;
        }

        /**
         * Getter method for distanceToTarget
         * @return distanceToTarget     distance to target node in a straight line
         */
        public double getDistanceToTarget() {
            return distanceToTarget;
        }

        /**
         * Get the heuristic distance to estimate best path in a* algorithm
         * @return fCost    distance + distanceToTarget
         */
        public double getFCost() {
            return fCost;
        }
    }

    // 2D array to represent grid
    private int [][] grid;
    // coordinates of start node
    private String startCoordinates;
    private int startRow;
    private int startColumn;
    // coordinates of target node
    private String targetCoordinates;
    private int targetRow;
    private int targetColumn;
    // 2D array to store all nodes' distances to target node
    private double [][] distancesToTarget;
    // priority queue to determine which nodes get processed first
    private PriorityQueue<QueueItem> pQueue;
    // 2D array to store booleans on whether or not a node has processed
    private boolean [][] processed;
    // 2D array to store previous node coordinates
    private String [][] prev;
    // direction vectors
    private int [] rowDir;
    private int [] columnDir;
    // controller for updating GUI
    private Controller controller;

    /**
     * Constructor for AStarAlgorithm
     * @param grid                  2D array to represent grid
     * @param startCoordinates      coordinates of start node
     * @param targetCoordinates     coordinates of target node
     */
    public AStarAlgorithm(int [][] grid, String startCoordinates, String targetCoordinates) {
        this.grid = grid;
        // convert start coordinates
        this.startCoordinates = startCoordinates;
        String [] startSplit = startCoordinates.split(", ");
        this.startRow = Integer.parseInt(startSplit[0]);
        this.startColumn = Integer.parseInt(startSplit[1]);
        // convert target coordinates
        this.targetCoordinates = targetCoordinates;
        String [] targetSplit = targetCoordinates.split(", ");
        this.targetRow = Integer.parseInt(targetSplit[0]);
        this.targetColumn = Integer.parseInt(targetSplit[1]);
        // initialize values
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
        // direction vectors
        rowDir = new int[] {-1, 0, 1, 0, -1, 1, 1, -1};
        columnDir = new int[] {0, 1, 0, -1, 1, 1, -1, -1};
        // calculate all nodes' distances to target node
        calculateDistances();
        // comparator for priority queue (pQueue)
        Comparator<QueueItem> comparator = (o1, o2) -> {
            if (o1.getFCost() <= o2.getFCost())
                return -1;
            return 1;
        };
        // priority queue to queue items from least heuristic distance to most heuristic distance
        pQueue = new PriorityQueue<>(comparator);
    }

    /**
     * Set controller for updating GUI
     * @param controller        controller for updating GUI
     */
    public void setController(Controller controller) {
        this.controller = controller;
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
     * set the coordinates in processed to true
     * @param item      QueueItem that holds the coordinates
     */
    private void markProcessed(QueueItem item) {
        String [] coordinatesSplit = item.getCoordinates().split(", ");
        int row = Integer.parseInt(coordinatesSplit[0]);
        int column = Integer.parseInt(coordinatesSplit[1]);

        processed[row][column] = true;
        // mark visited
        if (grid[row][column] != TARGET_NODE && grid[row][column] != START_NODE) {
            grid[row][column] = VISITED_NODE;
            controller.addToQueue(row, column, VISITED_NODE);
            System.out.println(formatGrid(grid));
        }
    }

    /**
     * Calculates the distance between two nodes
     * @param row           row coordinate of new node
     * @param column        column coordinate of new node
     * @param item          old coordinate
     * @return double       distance
     */
    private double calculateDistance(int row, int column, QueueItem item) {
        String [] coordinatesSplit = item.getCoordinates().split(", ");
        int prevRow = Integer.parseInt(coordinatesSplit[0]);
        int prevColumn = Integer.parseInt(coordinatesSplit[1]);

        return Math.sqrt(Math.pow(prevRow - row, 2) + Math.pow(prevColumn - column, 2));
    }

    /**
     * Looks at all the neighbouring nodes and adds them into pQueue to be processed later
     * @param item      current item in pQueue
     */
    private void lookAtNeighbours(QueueItem item) {
        // convert coordinates
        String [] coordinatesSplit = item.getCoordinates().split(", ");
        int row = Integer.parseInt(coordinatesSplit[0]);
        int column = Integer.parseInt(coordinatesSplit[1]);
        // look at the top, left, bottom, and right nodes and check if they're processed or obstacle nodes and add them
        // to pQueue accordingly
        for (int i = 0; i < 8; i++) {
            int updatedRow = row + rowDir[i];
            int updatedColumn = column + columnDir[i];
            // check boundaries
            if (updatedRow >= 0 && updatedRow < DIM && updatedColumn >= 0 && updatedColumn < DIM
                && !processed[updatedRow][updatedColumn]
                && distancesToTarget[updatedRow][updatedColumn] != -1) {

                // first create the QueueItem to compare later or add
                QueueItem pendingItem = new QueueItem(
                    updatedRow + ", " + updatedColumn,
                    calculateDistance(updatedRow, updatedColumn, item) + item.getDistance(),
                    distancesToTarget[updatedRow][updatedColumn]
                );

                // check if node is already in the pQueue and compare heuristic distances if so
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

    /**
     * Calculates the distances from each node to the target node and start node
     */
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

    /**
     * Marks the shortest path on the grid by backtracking from target node in prev
     */
    private void markPath(int row, int column) {
        String previous = prev[row][column];
        if (grid[row][column] == VISITED_NODE) {
            grid[row][column] = PATH_NODE;
            controller.addToQueue(row, column, PATH_NODE);
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
     * Perform A* algorithm on grid
     * @return boolean      true if a path to target node exists else false
     */
    public void AStar() {
        // mark start node's previous node in prev
        prev[startRow][startColumn] = "START";
        // add start node to begin with
        pQueue.add(new QueueItem(startCoordinates, 0, distancesToTarget[startRow][startColumn]));
        while (!pQueue.isEmpty()) {
            System.out.println();
            QueueItem removed = pQueue.remove();
            // mark removed as processed
            markProcessed(removed);
            // the moment the removed element is the target node, we finish the algorithm
            if (removed.getCoordinates().equals(targetCoordinates)) {
                String [] targetCoordinatesSplit = targetCoordinates.split(", ");
                int targetRow = Integer.parseInt(targetCoordinatesSplit[0]);
                int targetColumn = Integer.parseInt(targetCoordinatesSplit[1]);
                controller.addToQueue(targetRow, targetColumn, FOUND_NODE);
                markPath(targetRow, targetColumn);
                return;
            }
            // look at the neighbours of the removed item and add them to pQueue
            lookAtNeighbours(removed);
        }
    }

}
