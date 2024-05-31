package com.example.algorithmvisualizer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static com.example.algorithmvisualizer.Utility.*;

public class Controller {

    public class UpdateQueueItem {
        private int row;
        private int column;
        private int status;

        public UpdateQueueItem(int row, int column, int status) {
            this.row = row;
            this.column = column;
            this.status = status;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public int getStatus() {
            return status;
        }
    }

    private boolean startDrag;
    private boolean targetDrag;
    private boolean dragLock;
    private boolean lock;
    private boolean isReset;
    private String algorithm;
    private Button algoBtn;
    private Grid grid;
    private Rectangle startRect;
    private Rectangle targetRect;
    private HashMap<Integer, HashMap<Integer, Rectangle>> rows;
    private Queue<UpdateQueueItem> updateQueue;

    @FXML
    private GridPane gridPane;
    @FXML
    private ListView<String> algorithmListView;
    @FXML
    private Button resetPath, resetObstacles, resetEverything, run, backButton;

    private void setEmpty(Rectangle rect, int row, int column) {
        rect.setFill(EMPTY_COLOR);
        grid.changeNode(row, column, EMPTY_NODE);
    }

    private void setObstacle(Rectangle rect, int row, int column) {
        rect.setFill(OBSTACLE_COLOR);
        grid.changeNode(row, column, OBSTACLE_NODE);
    }

    private void setStart(Rectangle rect, Rectangle prevRect, int row, int column) {
        if (grid.setStart(row, column)) {
            prevRect.setFill(EMPTY_COLOR);
            rect.setFill(START_COLOR);
            startRect = rect;
        }
    }

    private void setTarget(Rectangle rect, Rectangle prevRect, int row, int column) {
        if (grid.setTarget(row, column)) {
            prevRect.setFill(EMPTY_COLOR);
            rect.setFill(TARGET_COLOR);
            targetRect = rect;
        }
    }

    private void clearObstacles() {
        grid.clearObstacles();
        updateGrid();
    }

    private void clearPath() {
        grid.clearPath();
        updateGrid();
    }

    private void clearEverything() {
        grid.clearEverything();
        updateGrid();
    }

    private void runAlgorithm(String algorithm) {
        if (!lock && isReset) {
            lock = true;
            isReset = false;
            switch (algorithm) {
                case "BFS":
                    grid.performBFS();
                    break;
                case "DFS":
                    grid.performDFS();
                    break;
                case "Dijkstra":
                    grid.performDijkstra();
                    break;
                case "AStar":
                    grid.performAStar();
                    break;
            }
        }
    }

    public void updateGrid() {
        for (Node node : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer column = GridPane.getColumnIndex(node);
            if (row == null || column == null || !(node instanceof Rectangle)) continue;
            Rectangle rect = (Rectangle)node;
            int status = grid.getNode(row, column);
            switch (status) {
                case EMPTY_NODE:
                    rect.setFill(EMPTY_COLOR);
                    break;
                case START_NODE:
                    startRect = rect;
                    rect.setFill(START_COLOR);
                    break;
                case TARGET_NODE:
                    targetRect = rect;
                    rect.setFill(TARGET_COLOR);
                    break;
                case OBSTACLE_NODE:
                    rect.setFill(OBSTACLE_COLOR);
                    break;
                case VISITED_NODE:
                    rect.setFill(VISITED_COLOR);
                    break;
                case PATH_NODE:
                    rect.setFill(PATH_COLOR);
                    break;
                case FOUND_NODE:
                    rect.setFill(FOUND_COLOR);
            }
        }
    }

    public void updateNode(UpdateQueueItem item) {
        int row = item.getRow();
        int column = item.getColumn();
        Rectangle rect = rows.get(row).get(column);
        switch (item.getStatus()) {
            case EMPTY_NODE:
                rect.setFill(EMPTY_COLOR);
                break;
            case START_NODE:
                rect.setFill(START_COLOR);
                break;
            case TARGET_NODE:
                rect.setFill(TARGET_COLOR);
                break;
            case VISITED_NODE:
                rect.setFill(VISITED_COLOR);
                break;
            case PATH_NODE:
                rect.setFill(PATH_COLOR);
                break;
            case FOUND_NODE:
                rect.setFill(FOUND_COLOR);
                break;
        }
    }

    public void updateFromQueue() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), event -> {
            if (!updateQueue.isEmpty()) {
                lock = true;
                updateNode(updateQueue.remove());
            } else {
                lock = false;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void addToQueue(int row, int column, int status) {
        updateQueue.add(new UpdateQueueItem(row, column, status));
    }

    public void initialize() {
        System.out.println("START");
        startDrag = false;
        targetDrag = false;
        dragLock = false;
        isReset = true;
        grid = new Grid();
        updateQueue = new LinkedList<>();

        EventHandler<MouseEvent> mouseClickHandler = event -> {
            if (!lock && isReset) {
                Rectangle sourceRect = (Rectangle) event.getSource();
                Integer row = GridPane.getRowIndex(sourceRect);
                Integer column = GridPane.getColumnIndex(sourceRect);
                int gridNode = grid.getNode(row, column);
                if (gridNode == EMPTY_NODE) {
                    setObstacle(sourceRect, row, column);
                } else if (gridNode == OBSTACLE_NODE) {
                    setEmpty(sourceRect, row, column);
                }
            }
        };

        EventHandler<MouseEvent> mouseDragHandler = event -> {
            if (!lock && isReset) {
                Rectangle sourceRect = (Rectangle) event.getSource();
                Integer row = GridPane.getRowIndex(sourceRect);
                Integer column = GridPane.getColumnIndex(sourceRect);
                int gridNode = grid.getNode(row, column);
                if ((gridNode == START_NODE || startDrag) && !dragLock) {
                    startDrag = true;
                    setStart(sourceRect, startRect, row, column);
                } else if ((gridNode == TARGET_NODE || targetDrag) && !dragLock) {
                    targetDrag = true;
                    setTarget(sourceRect, targetRect, row, column);
                } else {
                    dragLock = true;
                    if (gridNode == EMPTY_NODE) {
                        setObstacle(sourceRect, row, column);
                    } else if (gridNode == OBSTACLE_NODE) {
                        setEmpty(sourceRect, row, column);
                    }
                }
            }
        };

        EventHandler<MouseEvent> mouseReleaseHandler = event -> {
            startDrag = false;
            targetDrag = false;
            dragLock = false;
        };

        EventHandler<MouseEvent> algorithmClickHandler = event -> {
            if (algoBtn != null)
                algoBtn.setStyle("-fx-background-color: white; -fx-text-fill: black");
            algoBtn = (Button)event.getSource();
            algoBtn.setStyle("-fx-background-color: black; -fx-text-fill: red");
            algorithm = algoBtn.getId();
            System.out.println(algorithm);
        };

        EventHandler<MouseEvent> resetClickHandler = event -> {
            if (!lock) {
                Button btn = (Button) event.getSource();
                String reset = btn.getId();
                switch (reset) {
                    case "resetObstacles":
                        clearObstacles();
                        break;
                    case "resetPath":
                        clearPath();
                        isReset = true;
                        break;
                    case "resetEverything":
                        clearEverything();
                        isReset = true;
                        break;
                }
            }
        };

        EventHandler<MouseEvent> runClickHandler = event -> {
            if (algorithm != null)
                runAlgorithm(algorithm);
        };

        EventHandler<MouseEvent> backClickHandler = event -> {
            handleBackButtonClick();
        };

        // Set up ListView for algorithm selection
        algorithmListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        algorithmListView.getItems().addAll("BFS", "DFS", "Dijkstra", "A*");
        algorithmListView.setOnMouseClicked(event -> {
            String selectedAlgorithm = algorithmListView.getSelectionModel().getSelectedItem();
            if (selectedAlgorithm != null) {
                algorithm = selectedAlgorithm;
                System.out.println(algorithm);
            }
        });

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle rect = (Rectangle)node;
                rect.setOnMouseClicked(mouseClickHandler);
                rect.setOnDragDetected(event -> rect.startFullDrag());
                rect.setOnMouseDragEntered(mouseDragHandler);
                rect.setOnMouseReleased(mouseReleaseHandler);
            }
        }

        resetObstacles.setOnMouseClicked(resetClickHandler);
        resetPath.setOnMouseClicked(resetClickHandler);
        resetEverything.setOnMouseClicked(resetClickHandler);
        run.setOnMouseClicked(runClickHandler);
        backButton.setOnMouseClicked(backClickHandler);

        rows = new HashMap<>();
        for (int row = 0; row < DIM; row++) {
            HashMap<Integer, Rectangle> columns = new HashMap<>();
            rows.put(row, columns);
        }

        for (Node node : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer column = GridPane.getColumnIndex(node);
            if (row == null || column == null || !(node instanceof Rectangle)) continue;
            Rectangle rect = (Rectangle)node;
            rows.get(row).put(column, rect);
        }

        grid.setController(this);
        updateGrid();
        updateFromQueue();
    }

    @FXML
    private void handleBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/algorithmvisualizer/Menu.fxml"));
            Parent menuRoot = loader.load();
            Scene scene = backButton.getScene();
            if (scene == null) {
                System.out.println("Scene is null. Cannot set root.");
            } else {
                scene.setRoot(menuRoot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

