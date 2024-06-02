package com.example.algorithmvisualizer;

import com.jfoenix.controls.*;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CanvasController implements Initializable, ChangeListener {

    @FXML
    private HiddenSidesPane hiddenPane;
    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private StackPane stackRoot;
    @FXML
    private Button canvasBackButton, clearButton, resetButton, playPauseButton;
    @FXML
    private ToggleButton bfsButton, dfsButton, dijkstraButton, mstButton;
    @FXML
    private ToggleButton addNodeButton, addEdgeButton;
    @FXML
    private ToggleGroup algoToggleGroup;
    @FXML
    private RadioButton dButton, udButton, wButton, uwButton;
    @FXML
    private Pane viewer;
    @FXML
    private Group canvasGroup;
    @FXML
    private Line edgeLine;
    @FXML
    private Label sourceText = new Label("Source"), weight;
    @FXML
    private Pane border;
    @FXML
    private Arrow arrow;
    @FXML
    private JFXNodesList nodeList;
    @FXML
    private Slider slider;
    @FXML
    private ImageView playPauseImage, openHidden;

    boolean menuBool = false;
    ContextMenu globalMenu;

    int nNode = 0, time = 500;
    NodeFX selectedNode = null;
    List<NodeFX> circles = new ArrayList<>();
    List<Edge> mstEdges = new ArrayList<>(), realEdges = new ArrayList<>();
    List<Shape> edges = new ArrayList<>();
    boolean addNode = true, addEdge = false, calculate = false,
            calculated = false, playing = false, paused = false, pinned = false;
    List<Label> distances = new ArrayList<Label>(), visitTime = new ArrayList<>(), lowTime = new ArrayList<Label>();
    public boolean weighted = false, unweighted = true, directed = false, undirected = true,
            bfs = true, dfs = true, dijkstra = true, articulationPoint = true, mst = true, topSortBool = true;
    Algorithm algo = new Algorithm();

    public SequentialTransition st;

    public AnchorPane hiddenRoot = new AnchorPane();

    public static TextArea textFlow = new TextArea();
    public ScrollPane textContainer = new ScrollPane();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openHidden.setVisible(false);

        dButton.setSelected(directed);
        udButton.setSelected(undirected);
        wButton.setSelected(weighted);
        uwButton.setSelected(unweighted);

        dButton.setOnAction(e -> {
            directed = true;
            undirected = false;
            udButton.setSelected(false);
        });
        udButton.setOnAction(e -> {
            directed = false;
            undirected = true;
            dButton.setSelected(false);
        });
        wButton.setOnAction(e -> {
            weighted = true;
            unweighted = false;
            uwButton.setSelected(false);
        });
        uwButton.setOnAction(e -> {
            weighted = false;
            unweighted = true;
            wButton.setSelected(false);
        });

        System.out.println("In intit");
        hiddenPane.setContent(canvasGroup);

        ResetHandle(null);
        viewer.prefHeightProperty().bind(border.heightProperty());
        viewer.prefWidthProperty().bind(border.widthProperty());
        addEdgeButton.setDisable(true);
        clearButton.setDisable(true);

        if (weighted) {
            bfsButton.setDisable(true);
            dfsButton.setDisable(true);
        }

        if (unweighted) {
            dijkstraButton.setDisable(true);
        }
        if (directed) {
        }
        canvasBackButton.setOnAction(e -> {
            try {
                ResetHandle(null);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/algorithmvisualizer/Menu.fxml"));
                Parent menuRoot = loader.load();
                Scene scene = canvasBackButton.getScene();
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menuButtonStyle.css")).toExternalForm());
                scene.setRoot(menuRoot);
                AlgorithmVisualizerMenuController controller = loader.getController();
                controller.setPrimaryScene(scene);
            } catch (IOException ex) {
                Logger.getLogger(CanvasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        slider.valueProperty().addListener(this);

        hiddenRoot.setPrefWidth(220);
        hiddenRoot.setPrefHeight(581);

        hiddenRoot.setCursor(Cursor.DEFAULT);
        Label detailLabel = new Label("Detail");
        detailLabel.setPrefSize(hiddenRoot.getPrefWidth() - 20, 38);
        detailLabel.setAlignment(Pos.CENTER);
        detailLabel.setFont(new Font("Roboto", 20));
        detailLabel.setPadding(new Insets(7, 40, 3, -10));
        detailLabel.setStyle("-fx-background-color: #dcdde1;");
        detailLabel.setLayoutX(35);
        textFlow.setPrefSize(hiddenRoot.getPrefWidth(), hiddenRoot.getPrefHeight() - 2);
        textFlow.setStyle("-fx-background-color: #dfe6e9;");
        textFlow.setLayoutY(39);
        textContainer.setLayoutY(textFlow.getLayoutY());
        textFlow.setPadding(new Insets(5, 0, 0, 5));
        textFlow.setEditable(false);
        textContainer.setContent(textFlow);
        JFXButton pinUnpin = new JFXButton();
        pinUnpin.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        ImageView imgPin = new ImageView(new Image(getClass().getResourceAsStream("/res/pinned.png")));
        imgPin.setFitHeight(20);
        imgPin.setFitWidth(20);
        ImageView imgUnpin = new ImageView(new Image(getClass().getResourceAsStream("/res/unpinned.png")));
        imgUnpin.setFitHeight(20);
        imgUnpin.setFitWidth(20);
        pinUnpin.setGraphic(imgPin);

        pinUnpin.setPrefSize(20, 39);
        pinUnpin.setButtonType(JFXButton.ButtonType.FLAT);
        pinUnpin.setStyle("-fx-background-color: #dcdde1;");
        pinUnpin.setOnMouseClicked(e -> {
            if (pinned) {
                pinUnpin.setGraphic(imgPin);
                hiddenPane.setPinnedSide(null);
                pinned = false;
            } else {
                pinUnpin.setGraphic(imgUnpin);
                hiddenPane.setPinnedSide(Side.RIGHT);
                pinned = true;
            }
        });

        hiddenRoot.getChildren().addAll(pinUnpin, detailLabel, textContainer);
        hiddenPane.setRight(hiddenRoot);
        hiddenRoot.addEventFilter(MouseEvent.MOUSE_ENTERED, Event::consume);
        hiddenRoot.addEventFilter(MouseEvent.MOUSE_EXITED, Event::consume);
        hiddenPane.setTriggerDistance(Double.MAX_VALUE);
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        int temp = (int) slider.getValue();

        if (temp > 500) {
            int diff = temp - 500;
            temp = 500;
            temp -= diff;
            temp += 10;
        } else if (temp < 500) {
            int diff = 500 - temp;
            temp = 500;
            temp += diff;
            temp -= 10;
        }
        time = temp;
        System.out.println(time);
    }

    @FXML
    public void handle(MouseEvent ev) {
        if (addNode) {
            if (nNode == 1) {
                addNodeButton.setDisable(false);
                wButton.setDisable(true);
                uwButton.setDisable(true);
                dButton.setDisable(true);
                udButton.setDisable(true);
            }
            if (nNode == 2) {
                addEdgeButton.setDisable(false);
                AddNodeHandle(null);
            }

            if (!ev.getSource().equals(canvasGroup)) {
                if (ev.getEventType() == MouseEvent.MOUSE_RELEASED && ev.getButton() == MouseButton.PRIMARY) {
                    if (menuBool == true) {
                        System.out.println("here" + ev.getEventType());
                        menuBool = false;
                        return;
                    }
                    nNode++;
                    NodeFX circle = new NodeFX(ev.getX(), ev.getY(), 1.2, String.valueOf(nNode));
                    canvasGroup.getChildren().add(circle);

                    circle.setOnMousePressed(mouseHandler);
                    circle.setOnMouseReleased(mouseHandler);
                    circle.setOnMouseDragged(mouseHandler);
                    circle.setOnMouseExited(mouseHandler);
                    circle.setOnMouseEntered(mouseHandler);

                    ScaleTransition tr = new ScaleTransition(Duration.millis(100), circle);
                    tr.setByX(10f);
                    tr.setByY(10f);
                    tr.setInterpolator(Interpolator.EASE_OUT);
                    tr.play();

                }
            }
        }
    }

    boolean edgeExists(NodeFX u, NodeFX v) {
        for (Edge e : realEdges) {
            if (e.source == u.node && e.target == v.node) {
                return true;
            }
        }
        return false;
    }

    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {
            NodeFX circle = (NodeFX) mouseEvent.getSource();
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED && mouseEvent.getButton() == MouseButton.PRIMARY) {

                if (!circle.isSelected) {
                    if (selectedNode != null) {
                        if (addEdge && !edgeExists(selectedNode, circle)) {
                            weight = new Label();
                            System.out.println("Adding Edge");
                            if (undirected) {
                                edgeLine = new Line(selectedNode.point.x, selectedNode.point.y, circle.point.x, circle.point.y);
                                canvasGroup.getChildren().add(edgeLine);
                                edgeLine.setId("line");
                            } else if (directed) {
                                arrow = new Arrow(selectedNode.point.x, selectedNode.point.y, circle.point.x, circle.point.y);
                                canvasGroup.getChildren().add(arrow);
                                arrow.setId("arrow");
                            }
                            if (weighted) {
                                weight.setLayoutX(((selectedNode.point.x) + (circle.point.x)) / 2);
                                weight.setLayoutY(((selectedNode.point.y) + (circle.point.y)) / 2);

                                TextInputDialog dialog = new TextInputDialog("0");
                                dialog.setTitle(null);
                                dialog.setHeaderText("Enter Weight of the Edge :");
                                dialog.setContentText(null);

                                Optional<String> result = dialog.showAndWait();
                                if (result.isPresent()) {
                                    weight.setText(result.get());
                                } else {
                                    weight.setText("0");
                                }
                                canvasGroup.getChildren().add(weight);
                            } else if (unweighted) {
                                weight.setText("1");
                            }
                            Shape line_arrow = null;
                            Edge temp = null;
                            if (undirected) {
                                temp = new Edge(selectedNode.node, circle.node, Integer.valueOf(weight.getText()), edgeLine, weight);
                                if (weighted) {
                                    mstEdges.add(temp);
                                }

                                selectedNode.node.adjacents.add(new Edge(selectedNode.node, circle.node, Double.valueOf(weight.getText()), edgeLine, weight));
                                circle.node.adjacents.add(new Edge(circle.node, selectedNode.node, Double.valueOf(weight.getText()), edgeLine, weight));
                                edges.add(edgeLine);
                                realEdges.add(selectedNode.node.adjacents.get(selectedNode.node.adjacents.size() - 1));
                                realEdges.add(circle.node.adjacents.get(circle.node.adjacents.size() - 1));
                                line_arrow = edgeLine;

                            } else if (directed) {
                                temp = new Edge(selectedNode.node, circle.node, Double.valueOf(weight.getText()), arrow, weight);
                                selectedNode.node.adjacents.add(temp);
                                edges.add(arrow);
                                line_arrow = arrow;
                                realEdges.add(temp);
                            }

                            RightClickMenu rt = new RightClickMenu(temp);
                            ContextMenu menu = rt.getMenu();
                            if (weighted) {
                                rt.changeId.setText("Change Weight");
                            } else if (unweighted) {
                                rt.changeId.setDisable(true);
                            }
                            final Shape la = line_arrow;
                            line_arrow.setOnContextMenuRequested(e -> {
                                System.out.println("In Edge Menu :" + menuBool);

                                if (menuBool == true) {
                                    globalMenu.hide();
                                    menuBool = false;
                                }
                                if (addEdge || addNode) {
                                    globalMenu = menu;
                                    menu.show(la, e.getScreenX(), e.getScreenY());
                                    menuBool = true;
                                }
                            });
                            menu.setOnAction(e -> {
                                menuBool = false;
                            });
                        }
                        if (addNode || (calculate && !calculated) || addEdge) {
                            selectedNode.isSelected = false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300), selectedNode, Color.RED, Color.BLACK);
                            ft1.play();
                        }
                        selectedNode = null;
                        return;
                    }

                    FillTransition ft = new FillTransition(Duration.millis(300), circle, Color.BLACK, Color.RED);
                    ft.play();
                    circle.isSelected = true;
                    selectedNode = circle;
                    if (calculate && !calculated) {
                        if (bfs) {
                            algo.newBFS(circle.node);
                        } else if (dfs) {
                            algo.newDFS(circle.node);
                        } else if (dijkstra) {
                            algo.newDijkstra(circle.node);
                        }

                        calculated = true;
                    } else if (calculate && calculated && !articulationPoint & !mst && !topSortBool) {

                        for (NodeFX n : circles) {
                            n.isSelected = false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300), n);
                            ft1.setToValue(Color.BLACK);
                            ft1.play();
                        }
                        List<Node> path = algo.getShortestPathTo(circle.node);
                        for (Node n : path) {
                            FillTransition ft1 = new FillTransition(Duration.millis(300), n.circle);
                            ft1.setToValue(Color.BLUE);
                            ft1.play();
                        }
                    }
                } else {
                    circle.isSelected = false;
                    FillTransition ft1 = new FillTransition(Duration.millis(300), circle, Color.RED, Color.BLACK);
                    ft1.play();
                    selectedNode = null;
                }

            }
        }

    };

    private Node getRandomStart() {
        return circles.get(0).node;
    }

    @FXML
    public void PlayPauseHandle(ActionEvent event) {
        System.out.println("IN PLAYPAUSE");
        System.out.println(playing + " " + paused);

        try {
            if (playing && st != null && st.getStatus() == Animation.Status.RUNNING) {
                playPauseButton.setText("Play");
                System.out.println("Pausing");
                st.pause();
                paused = true;
                playing = false;
                return;
            } else if (paused && st != null) {
                playPauseButton.setText("Pause");
                if (st.getStatus() == Animation.Status.PAUSED)
                    st.play();
                else if (st.getStatus() == Animation.Status.STOPPED)
                    st.playFromStart();
                playing = true;
                paused = false;
                return;
            }
        } catch (Exception e) {
            System.out.println("Error while play/pause: " + e);
            ClearHandle();
        }
    }

    @FXML
    public void ResetHandle(ActionEvent event) {
        ClearHandle();
        nNode = 0;
        canvasGroup.getChildren().clear();
        canvasGroup.getChildren().addAll(viewer);
        selectedNode = null;
        circles = new ArrayList<NodeFX>();
        distances = new ArrayList<Label>();
        visitTime = new ArrayList<Label>();
        lowTime = new ArrayList<Label>();
        addNode = true;
        addEdge = false;
        calculate = false;
        calculated = false;
        addNodeButton.setSelected(true);
        addEdgeButton.setSelected(false);
        addEdgeButton.setDisable(true);
        addNodeButton.setDisable(false);
        clearButton.setDisable(true);
        algo = new Algorithm();
        playPauseButton.setText("Pause");
        hiddenPane.setPinnedSide(null);

        bfsButton.setDisable(true);
        dfsButton.setDisable(true);
        dijkstraButton.setDisable(true);
        mstButton.setDisable(true);
        playing = false;
        paused = false;

        wButton.setDisable(false);
        uwButton.setDisable(false);
        dButton.setDisable(false);
        udButton.setDisable(false);
    }

    @FXML
    public void ClearHandle() {
        if (st != null && st.getStatus() != Animation.Status.STOPPED)
            st.stop();
        if (st != null) st.getChildren().clear();
        menuBool = false;
        selectedNode = null;
        calculated = false;
        System.out.println("IN CLEAR:" + circles.size());
        for (NodeFX n : circles) {
            n.isSelected = false;
            n.node.visited = false;
            n.node.previous = null;
            n.node.minDistance = Double.POSITIVE_INFINITY;
            n.node.DAGColor = 0;

            FillTransition ft1 = new FillTransition(Duration.millis(300), n);
            ft1.setToValue(Color.BLACK);
            ft1.play();
        }
        for (Shape x : edges) {
            if (undirected) {
                StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), x);
                ftEdge.setToValue(Color.BLACK);
                ftEdge.play();
            } else if (directed) {
                FillTransition ftEdge = new FillTransition(Duration.millis(time), x);
                ftEdge.setToValue(Color.BLACK);
                ftEdge.play();
            }
        }
        canvasGroup.getChildren().remove(sourceText);
        for (Label x : distances) {
            x.setText("Distance : INFINITY");
            canvasGroup.getChildren().remove(x);
        }
        for (Label x : visitTime) {
            x.setText("Visit : 0");
            canvasGroup.getChildren().remove(x);
        }
        for (Label x : lowTime) {
            x.setText("Low Value : NULL");
            canvasGroup.getChildren().remove(x);
        }
        textFlow.clear();
        playPauseButton.setText("Pause");

        distances = new ArrayList<>();
        visitTime = new ArrayList<>();
        lowTime = new ArrayList<>();
        addNodeButton.setDisable(false);
        addEdgeButton.setDisable(false);
        AddNodeHandle(null);
        bfs = false;
        dfs = false;
        articulationPoint = false;
        dijkstra = false;
        mst = false;
        topSortBool = false;
        playing = false;
        paused = false;
    }

    @FXML
    public void AddEdgeHandle(ActionEvent event) {
        addNode = false;
        addEdge = true;
        calculate = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(true);

        if (unweighted) {
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);
        }
        if (weighted) {
            dijkstraButton.setDisable(false);
            dijkstraButton.setSelected(false);
            if (undirected) {
                mstButton.setDisable(false);
                mstButton.setSelected(false);
            }
        }
    }

    @FXML
    public void AddNodeHandle(ActionEvent event) {
        addNode = true;
        addEdge = false;
        calculate = false;
        addNodeButton.setSelected(true);
        addEdgeButton.setSelected(false);
        selectedNode = null;

        if (unweighted) {
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);
        }
        if (weighted) {
            dijkstraButton.setDisable(false);
            dijkstraButton.setSelected(false);
            if (undirected) {
                mstButton.setDisable(false);
                mstButton.setSelected(false);
            }
        }
    }

    @FXML
    public void BFSHandle(ActionEvent event) {
        if(!clearButton.isDisable())
            ClearHandle();

        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        bfs = true;
        dfs = false;
        dijkstra = false;
        mst = false;
        articulationPoint = false;

        dfsButton.setSelected(false);
        bfsButton.setSelected(true);
        dijkstraButton.setSelected(false);
        mstButton.setSelected(false);
    }

    @FXML
    public void DFSHandle(ActionEvent event) {
        if(!clearButton.isDisable())
            ClearHandle();

        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        dfs = true;
        bfs = false;
        dijkstra = false;
        mst = false;
        articulationPoint = false;

        dfsButton.setSelected(true);
        bfsButton.setSelected(false);
        dijkstraButton.setSelected(false);
        mstButton.setSelected(false);
    }

    @FXML
    public void DijkstraHandle(ActionEvent event) {
        if(!clearButton.isDisable())
            ClearHandle();

        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        bfs = false;
        dfs = false;
        dijkstra = true;
        mst = false;
        articulationPoint = false;

        dfsButton.setSelected(false);
        bfsButton.setSelected(false);
        dijkstraButton.setSelected(true);
        mstButton.setSelected(false);
    }

    @FXML
    public void MSTHandle(ActionEvent event) {
        if(!clearButton.isDisable())
            ClearHandle();

        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        bfs = false;
        dfs = false;
        dijkstra = false;
        articulationPoint = false;
        mst = true;
        algo.newMST();

        dfsButton.setSelected(false);
        bfsButton.setSelected(false);
        dijkstraButton.setSelected(false);
        mstButton.setSelected(true);
    }

    public void changeID(NodeFX source) {
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }
        selectedNode = null;

        TextInputDialog dialog = new TextInputDialog(Integer.toString(nNode));
        dialog.setTitle(null);
        dialog.setHeaderText("Enter Node ID :");
        dialog.setContentText(null);

        String res = null;
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            res = result.get();
        }

        circles.get(circles.indexOf(source)).id.setText(res);
        circles.get(circles.indexOf(source)).node.name = res;

        System.out.println("AFTER----------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }
    }

    public void deleteNode(NodeFX sourceFX) {
        selectedNode = null;
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }

        Node source = sourceFX.node;
        circles.remove(sourceFX);

        List<Edge> tempEdges = new ArrayList<>();
        List<Node> tempNodes = new ArrayList<>();
        for (Edge e : source.adjacents) {
            Node u = e.target;
            for (Edge x : u.adjacents) {
                if (x.target == source) {
                    x.target = null;
                    tempNodes.add(u);
                    tempEdges.add(x);
                }
            }
            edges.remove(e.getLine());
            canvasGroup.getChildren().remove(e.getLine());
            mstEdges.remove(e);
        }
        for (Node q : tempNodes) {
            q.adjacents.removeAll(tempEdges);
        }
        List<Edge> tempEdges2 = new ArrayList<>();
        List<Shape> tempArrows = new ArrayList<>();
        List<Node> tempNodes2 = new ArrayList<>();
        for (NodeFX z : circles) {
            for (Edge s : z.node.adjacents) {
                if (s.target == source) {
                    tempEdges2.add(s);
                    tempArrows.add(s.line);
                    tempNodes2.add(z.node);
                    canvasGroup.getChildren().remove(s.line);
                }
            }
        }
        for (Node z : tempNodes2) {
            z.adjacents.removeAll(tempEdges2);
        }
        realEdges.removeAll(tempEdges);
        realEdges.removeAll(tempEdges2);
        canvasGroup.getChildren().remove(sourceFX.id);
        canvasGroup.getChildren().remove(sourceFX);

        System.out.println("AFTER----------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }

    }

    public void deleteEdge(Edge sourceEdge) {
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }

        System.out.println(sourceEdge.source.name + " -- " + sourceEdge.target.name);
        List<Edge> ls1 = new ArrayList<>();
        List<Shape> lshape2 = new ArrayList<>();
        for (Edge e : sourceEdge.source.adjacents) {
            if (e.target == sourceEdge.target) {
                ls1.add(e);
                lshape2.add(e.line);
            }
        }
        for (Edge e : sourceEdge.target.adjacents) {
            if (e.target == sourceEdge.source) {
                ls1.add(e);
                lshape2.add(e.line);
            }
        }
        System.out.println("sdsdsd  " + ls1.size());
        sourceEdge.source.adjacents.removeAll(ls1);
        sourceEdge.target.adjacents.removeAll(ls1);
        realEdges.removeAll(ls1);

        edges.removeAll(lshape2);
        canvasGroup.getChildren().removeAll(lshape2);

        System.out.println("AFTER----------");
        for (NodeFX p : circles) {
            System.out.println(p.node.name + " - ");
            for (Edge q : p.node.adjacents) {
                System.out.println(q.source.name + " " + q.target.name);
            }
        }
    }

    public void changeWeight(Edge sourceEdge) {
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name + " weight: " + v.weight);
            }
        }

        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle(null);
        dialog.setHeaderText("Enter Weight of the Edge :");
        dialog.setContentText(null);

        String res = null;
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            res = result.get();
        }

        for (Edge e : sourceEdge.source.adjacents) {
            if (e.target == sourceEdge.target) {
                e.weight = Double.valueOf(res);
                e.weightLabel.setText(res);
            }
        }
        for (Edge e : sourceEdge.target.adjacents) {
            if (e.target == sourceEdge.source) {
                e.weight = Double.valueOf(res);
            }
        }
        for (Edge e : mstEdges) {
            if (e.source == sourceEdge.source && e.target == sourceEdge.target) {
                e.weight = Double.valueOf(res);
            }
        }

        System.out.println("AFTER----------");
        for (NodeFX p : circles) {
            System.out.println(p.node.name + " - ");
            for (Edge q : p.node.adjacents) {
                System.out.println(q.source.name + " " + q.target.name + " weigh: " + q.weight);
            }
        }
    }

    public class NodeFX extends Circle {

        Node node;
        Point point;
        Label distance = new Label("Dist. : INFINITY");
        Label visitTime = new Label("Visit : 0");
        Label lowTime = new Label("Low : 0");
        Label id;
        boolean isSelected = false;

        public NodeFX(double x, double y, double rad, String name) {
            super(x, y, rad);
            node = new Node(name, this);
            point = new Point((int) x, (int) y);
            id = new Label(name);
            canvasGroup.getChildren().add(id);
            id.setLayoutX(x - 18);
            id.setLayoutY(y - 18);
            this.setOpacity(0.5);
            this.setBlendMode(BlendMode.MULTIPLY);
            this.setId("node");

            RightClickMenu rt = new RightClickMenu(this);
            ContextMenu menu = rt.getMenu();
            globalMenu = menu;
            this.setOnContextMenuRequested(e -> {
                if (addEdge || addNode) {
                    menu.show(this, e.getScreenX(), e.getScreenY());
                    menuBool = true;
                }
            });
            menu.setOnAction(e -> {
                menuBool = false;
            });

            circles.add(this);
            System.out.println("ADDing: " + circles.size());
        }
    }

    public class Algorithm {

        public void newDijkstra(Node source) {
            new Dijkstra(source);
        }

        class Dijkstra {

            Dijkstra(Node source) {
                for (NodeFX n : circles) {
                    distances.add(n.distance);
                    n.distance.setLayoutX(n.point.x + 20);
                    n.distance.setLayoutY(n.point.y);
                    canvasGroup.getChildren().add(n.distance);
                }
                sourceText.setLayoutX(source.circle.point.x + 20);
                sourceText.setLayoutY(source.circle.point.y + 10);
                canvasGroup.getChildren().add(sourceText);
                SequentialTransition st = new SequentialTransition();
                source.circle.distance.setText("Dist. : " + 0);

                source.minDistance = 0;
                PriorityQueue<Node> pq = new PriorityQueue<Node>();
                pq.add(source);
                while (!pq.isEmpty()) {
                    Node u = pq.poll();
                    System.out.println(u.name);
                    FillTransition ft = new FillTransition(Duration.millis(time), u.circle);
                    ft.setToValue(Color.CHOCOLATE);
                    st.getChildren().add(ft);
                    String str = "";
                    str = str.concat("Popped : Node(" + u.name + "), Current Distance: " + u.minDistance + "\n");
                    final String str2 = str;
                    FadeTransition fd = new FadeTransition(Duration.millis(10), textFlow);
                    fd.setOnFinished(e -> {
                        textFlow.appendText(str2);
                    });
                    fd.onFinishedProperty();
                    st.getChildren().add(fd);
                    System.out.println(u.name);
                    for (Edge e : u.adjacents) {
                        if (e != null) {
                            Node v = e.target;
                            System.out.println("HERE " + v.name);
                            if (u.minDistance + e.weight < v.minDistance) {
                                pq.remove(v);
                                v.minDistance = u.minDistance + e.weight;
                                v.previous = u;
                                pq.add(v);
                                if (undirected) {
                                    StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), e.line);
                                    ftEdge.setToValue(Color.FORESTGREEN);
                                    st.getChildren().add(ftEdge);
                                } else if (directed) {
                                    FillTransition ftEdge = new FillTransition(Duration.millis(time), e.line);
                                    ftEdge.setToValue(Color.FORESTGREEN);
                                    st.getChildren().add(ftEdge);
                                }
                                FillTransition ft1 = new FillTransition(Duration.millis(time), v.circle);
                                ft1.setToValue(Color.FORESTGREEN);
                                ft1.setOnFinished(ev -> {
                                    v.circle.distance.setText("Dist. : " + v.minDistance);
                                });
                                ft1.onFinishedProperty();
                                st.getChildren().add(ft1);

                                str = "\t";
                                str = str.concat("Pushing : Node(" + v.name + "), (" + u.name + "--" + v.name + ") Distance : " + v.minDistance + "\n");
                                final String str1 = str;
                                FadeTransition fd2 = new FadeTransition(Duration.millis(10), textFlow);
                                fd2.setOnFinished(ev -> {
                                    textFlow.appendText(str1);
                                });
                                fd2.onFinishedProperty();
                                st.getChildren().add(fd2);
                            }
                        }
                    }
                    FillTransition ft2 = new FillTransition(Duration.millis(time), u.circle);
                    ft2.setToValue(Color.BLUEVIOLET);
                    st.getChildren().add(ft2);
                }
                st.setOnFinished(ev -> {
                    for (NodeFX n : circles) {
                        FillTransition ft1 = new FillTransition(Duration.millis(time), n);
                        ft1.setToValue(Color.BLACK);
                        ft1.play();
                    }
                    if (directed) {
                        for (Shape n : edges) {
                            n.setFill(Color.BLACK);
                        }
                    } else if (undirected) {
                        for (Shape n : edges) {
                            n.setStroke(Color.BLACK);
                        }
                    }
                    FillTransition ft1 = new FillTransition(Duration.millis(time), source.circle);
                    ft1.setToValue(Color.RED);
                    ft1.play();
                    playPauseButton.setText("Play");
                    paused = true;
                    playing = false;
                    textFlow.appendText("---Finished--\n");
                });
                st.onFinishedProperty();
                st.play();
                playing = true;
                paused = false;
            }
        }

        public void newBFS(Node source) {
            new BFS(source);
        }

        class BFS {

            BFS(Node source) {
                for (NodeFX n : circles) {
                    distances.add(n.distance);
                    n.distance.setLayoutX(n.point.x + 20);
                    n.distance.setLayoutY(n.point.y);
                    canvasGroup.getChildren().add(n.distance);
                }
                sourceText.setLayoutX(source.circle.point.x + 20);
                sourceText.setLayoutY(source.circle.point.y + 10);
                canvasGroup.getChildren().add(sourceText);
                st = new SequentialTransition();
                source.circle.distance.setText("Dist. : " + 0);

                source.minDistance = 0;
                source.visited = true;
                LinkedList<Node> q = new LinkedList<Node>();
                q.push(source);
                while (!q.isEmpty()) {
                    Node u = q.removeLast();
                    FillTransition ft = new FillTransition(Duration.millis(time), u.circle);
                    if (u.circle.getFill() == Color.BLACK) {
                        ft.setToValue(Color.CHOCOLATE);
                    }
                    st.getChildren().add(ft);

                    String str = "";
                    str = str.concat("Popped : Node(" + u.name + ")\n");
                    final String str2 = str;
                    FadeTransition fd = new FadeTransition(Duration.millis(10), textFlow);
                    fd.setOnFinished(e -> {
                        textFlow.appendText(str2);
                    });
                    fd.onFinishedProperty();
                    st.getChildren().add(fd);
                    System.out.println(u.name);
                    for (Edge e : u.adjacents) {
                        if (e != null) {
                            Node v = e.target;

                            if (!v.visited) {
                                v.minDistance = u.minDistance + 1;
                                v.visited = true;
                                q.push(v);
                                v.previous = u;
                                if (undirected) {
                                    StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), e.line);
                                    ftEdge.setToValue(Color.FORESTGREEN);
                                    st.getChildren().add(ftEdge);
                                } else if (directed) {
                                    FillTransition ftEdge = new FillTransition(Duration.millis(time), e.line);
                                    ftEdge.setToValue(Color.FORESTGREEN);
                                    st.getChildren().add(ftEdge);
                                }
                                FillTransition ft1 = new FillTransition(Duration.millis(time), v.circle);
                                ft1.setToValue(Color.FORESTGREEN);
                                ft1.setOnFinished(ev -> {
                                    v.circle.distance.setText("Dist. : " + v.minDistance);
                                });
                                ft1.onFinishedProperty();
                                st.getChildren().add(ft1);

                                str = "\t";
                                str = str.concat("Pushing : Node(" + v.name + ")\n");
                                final String str1 = str;
                                FadeTransition fd2 = new FadeTransition(Duration.millis(10), textFlow);
                                fd2.setOnFinished(ev -> {
                                    textFlow.appendText(str1);
                                });
                                fd2.onFinishedProperty();
                                st.getChildren().add(fd2);
                            }
                        }
                    }
                    FillTransition ft2 = new FillTransition(Duration.millis(time), u.circle);
                    ft2.setToValue(Color.BLUEVIOLET);
                    st.getChildren().add(ft2);
                }
                st.setOnFinished(ev -> {
                    for (NodeFX n : circles) {
                        FillTransition ft1 = new FillTransition(Duration.millis(time), n);
                        ft1.setToValue(Color.BLACK);
                        ft1.play();
                    }
                    if (directed) {
                        for (Shape n : edges) {
                            n.setFill(Color.BLACK);
                        }
                    } else if (undirected) {
                        for (Shape n : edges) {
                            n.setStroke(Color.BLACK);
                        }
                    }
                    FillTransition ft1 = new FillTransition(Duration.millis(time), source.circle);
                    ft1.setToValue(Color.RED);
                    ft1.play();
                    playPauseButton.setText("Play");
                    paused = true;
                    playing = false;
                    textFlow.appendText("---Finished--\n");
                });
                st.onFinishedProperty();
                st.play();
                playing = true;
                paused = false;

            }

        }

        public void newMST() {
            new MST();
        }

        class MST {

            int mstValue = 0;

            Node findParent(Node x) {
                if (x == x.previous) {
                    return x;
                }
                x.previous = findParent(x.previous);
                return x.previous;
            }

            void unionNode(Node x, Node y) {
                Node px = findParent(x);
                Node py = findParent(y);
                if (px == py) {
                    return;
                }
                if (Integer.valueOf(px.name) < Integer.valueOf(py.name)) {
                    px.previous = py;
                } else {
                    py.previous = px;
                }
            }

            public MST() {

                st = new SequentialTransition();
                for (NodeFX x : circles) {
                    x.node.previous = x.node;
                }
                String init = "Intially : \n";
                for (NodeFX x : circles) {
                    final String s = "Node : " + x.node.name + " , Parent: " + x.node.previous.name + "\n";
                    FadeTransition fd = new FadeTransition(Duration.millis(10), textFlow);
                    fd.setOnFinished(e -> {
                        textFlow.appendText(s);
                    });
                    fd.onFinishedProperty();
                    st.getChildren().add(fd);
                }
                final String s = "Start Algorithm :---\n";
                FadeTransition fdss = new FadeTransition(Duration.millis(10), textFlow);
                fdss.setOnFinished(ev -> {
                    textFlow.appendText(s);
                });
                fdss.onFinishedProperty();
                st.getChildren().add(fdss);
                Collections.sort(mstEdges, new Comparator<Edge>() {
                    public int compare(Edge o1, Edge o2) {
                        if (o1.weight == o2.weight) {
                            return 0;
                        }
                        return o1.weight > o2.weight ? 1 : -1;
                    }
                });

                for (Edge e : mstEdges) {

                    StrokeTransition ft1 = new StrokeTransition(Duration.millis(time), e.line);
                    ft1.setToValue(Color.DARKORANGE);
                    st.getChildren().add(ft1);
                    final String se = "Selected Edge:- (" + e.source.name.trim() + "--" + e.target.name.trim() + ") Weight: " + String.valueOf(e.weight) + " \n";
                    FadeTransition fdx = new FadeTransition(Duration.millis(10), textFlow);
                    fdx.setOnFinished(evx -> {
                        textFlow.appendText(se);
                    });
                    fdx.onFinishedProperty();
                    st.getChildren().add(fdx);

                    final String s1 = "\t-> Node :" + e.source.name.trim() + "  Parent: " + findParent(e.source.previous).name.trim() + "\n";
                    FadeTransition fdx2 = new FadeTransition(Duration.millis(10), textFlow);
                    fdx2.setOnFinished(evx -> {
                        textFlow.appendText(s1);
                    });
                    fdx2.onFinishedProperty();
                    st.getChildren().add(fdx2);

                    final String s2 = "\t-> Node :" + e.target.name.trim() + "  Parent: " + findParent(e.target.previous).name.trim() + "\n";
                    FadeTransition fdx3 = new FadeTransition(Duration.millis(10), textFlow);
                    fdx3.setOnFinished(evx -> {
                        textFlow.appendText(s2);
                    });
                    fdx3.onFinishedProperty();
                    st.getChildren().add(fdx3);

                    if (findParent(e.source.previous) != findParent(e.target.previous)) {
                        unionNode(e.source, e.target);
                        mstValue += e.weight;
                        final String sa = "\t---->Unioned\n";
                        final String sa1 = "\t\t->Node :" + e.source.name.trim() + "  Parent: " + findParent(e.source.previous).name.trim() + "\n";
                        final String sa2 = "\t\t->Node :" + e.target.name.trim() + "  Parent: " + findParent(e.target.previous).name.trim() + "\n";
                        FadeTransition fdx4 = new FadeTransition(Duration.millis(10), textFlow);
                        fdx4.setOnFinished(evx -> {
                            textFlow.appendText(sa);
                        });
                        fdx4.onFinishedProperty();
                        st.getChildren().add(fdx4);
                        FadeTransition fdx5 = new FadeTransition(Duration.millis(10), textFlow);
                        fdx5.setOnFinished(evx -> {
                            textFlow.appendText(sa1);
                        });
                        fdx5.onFinishedProperty();
                        st.getChildren().add(fdx5);
                        FadeTransition fdx6 = new FadeTransition(Duration.millis(10), textFlow);
                        fdx6.setOnFinished(evx -> {
                            textFlow.appendText(sa2);
                        });
                        fdx6.onFinishedProperty();
                        st.getChildren().add(fdx6);

                        StrokeTransition ft2 = new StrokeTransition(Duration.millis(time), e.line);
                        ft2.setToValue(Color.DARKGREEN);
                        st.getChildren().add(ft2);

                        FillTransition ft3 = new FillTransition(Duration.millis(time), e.source.circle);
                        ft3.setToValue(Color.AQUA);
                        st.getChildren().add(ft3);

                        ft3 = new FillTransition(Duration.millis(time), e.target.circle);
                        ft3.setToValue(Color.AQUA);
                        st.getChildren().add(ft3);
                    } else {
                        final String sa = "\t---->Cycle Detected\n";
                        FadeTransition fdx7 = new FadeTransition(Duration.millis(10), textFlow);
                        fdx7.setOnFinished(evx -> {
                            textFlow.appendText(sa);
                        });
                        fdx7.onFinishedProperty();
                        st.getChildren().add(fdx7);
                        StrokeTransition ft2 = new StrokeTransition(Duration.millis(time), e.line);
                        ft2.setToValue(Color.DARKRED);
                        st.getChildren().add(ft2);

                        ft2 = new StrokeTransition(Duration.millis(time), e.line);
                        ft2.setToValue(Color.web("#E0E0E0"));
                        st.getChildren().add(ft2);

                    }
                }
                st.setOnFinished(ev -> {
                    playPauseButton.setText("Play");
                    paused = true;
                    playing = false;
                    textFlow.appendText("Minimum Cost of the Graph " + mstValue);
                });
                st.onFinishedProperty();
                st.play();
                playing = true;
                System.out.println("" + mstValue);
            }
        }

        public void newDFS(Node source) {
            new DFS(source);
        }

        class DFS {

            DFS(Node source) {
                for (NodeFX n : circles) {
                    distances.add(n.distance);
                    n.distance.setLayoutX(n.point.x + 20);
                    n.distance.setLayoutY(n.point.y);
                    canvasGroup.getChildren().add(n.distance);
                }
                sourceText.setLayoutX(source.circle.point.x + 20);
                sourceText.setLayoutY(source.circle.point.y + 10);
                canvasGroup.getChildren().add(sourceText);
                st = new SequentialTransition();
                source.circle.distance.setText("Dist. : " + 0);

                source.minDistance = 0;
                source.visited = true;
                DFSRecursion(source, 0);
                st.setOnFinished(ev -> {
                    for (NodeFX n : circles) {
                        FillTransition ft1 = new FillTransition(Duration.millis(time), n);
                        ft1.setToValue(Color.BLACK);
                        ft1.play();
                    }
                    if (directed) {
                        for (Shape n : edges) {
                            n.setFill(Color.BLACK);
                        }
                    } else if (undirected) {
                        for (Shape n : edges) {
                            n.setStroke(Color.BLACK);
                        }
                    }
                    FillTransition ft1 = new FillTransition(Duration.millis(time), source.circle);
                    ft1.setToValue(Color.RED);
                    ft1.play();
                    playPauseButton.setText("Play");
                    paused = true;
                    playing = false;
                    textFlow.appendText("---Finished--\n");
                });
                st.onFinishedProperty();
                st.play();
                playing = true;
                paused = false;
            }

            public void DFSRecursion(Node source, int level) {
                FillTransition ft = new FillTransition(Duration.millis(time), source.circle);
                if (source.circle.getFill() == Color.BLACK) {
                    ft.setToValue(Color.FORESTGREEN);
                }
                st.getChildren().add(ft);

                String str = "";
                for (int i = 0; i < level; i++) {
                    str = str.concat("\t");
                }
                str = str.concat("DFS(" + source.name + ") Enter\n");
                final String str2 = str;
                FadeTransition fd = new FadeTransition(Duration.millis(10), textFlow);
                fd.setOnFinished(e -> {
                    textFlow.appendText(str2);
                });
                fd.onFinishedProperty();
                st.getChildren().add(fd);
                for (Edge e : source.adjacents) {
                    if (e != null) {
                        Node v = e.target;
                        if (!v.visited) {
                            v.minDistance = source.minDistance + 1;
                            v.visited = true;
                            v.previous = source;
                            if (undirected) {
                                StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), e.line);
                                ftEdge.setToValue(Color.FORESTGREEN);
                                st.getChildren().add(ftEdge);
                            } else if (directed) {
                                FillTransition ftEdge = new FillTransition(Duration.millis(time), e.line);
                                ftEdge.setToValue(Color.FORESTGREEN);
                                st.getChildren().add(ftEdge);
                            }
                            DFSRecursion(v, level + 1);
                            if (undirected) {
                                StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), e.line);
                                ftEdge.setToValue(Color.BLUEVIOLET);
                                st.getChildren().add(ftEdge);
                            } else if (directed) {
                                FillTransition ftEdge = new FillTransition(Duration.millis(time), e.line);
                                ftEdge.setToValue(Color.BLUEVIOLET);
                                st.getChildren().add(ftEdge);
                            }
                            FillTransition ft1 = new FillTransition(Duration.millis(time), v.circle);
                            ft1.setToValue(Color.BLUEVIOLET);
                            ft1.onFinishedProperty();
                            ft1.setOnFinished(ev -> {
                                v.circle.distance.setText("Dist. : " + v.minDistance);
                            });
                            st.getChildren().add(ft1);
                        }
                    }
                }
                str = "";
                for (int i = 0; i < level; i++) {
                    str = str.concat("\t");
                }
                str = str.concat("DFS(" + source.name + ") Exit\n");
                final String str1 = str;
                fd = new FadeTransition(Duration.millis(10), textFlow);
                fd.setOnFinished(e -> {
                    textFlow.appendText(str1);
                });
                fd.onFinishedProperty();
                st.getChildren().add(fd);
            }
        }

        public List<Node> getShortestPathTo(Node target) {
            List<Node> path = new ArrayList<Node>();
            for (Node i = target; i != null; i = i.previous) {
                path.add(i);
            }
            Collections.reverse(path);
            return path;
        }
    }

}
