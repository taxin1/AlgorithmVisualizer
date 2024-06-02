package com.example.algorithmvisualizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Panel1Controller implements Initializable {

    public static boolean directed = false, undirected = true, weighted = false, unweighted = true;

    @FXML
    public Button panel1Next, panel2Back;
    @FXML
    private RadioButton dButton, udButton, wButton, uwButton;
    @FXML
    private AnchorPane panel1;

    static CanvasController cref;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the radio buttons with default values
        dButton.setSelected(directed);
        udButton.setSelected(undirected);
        wButton.setSelected(weighted);
        uwButton.setSelected(unweighted);

        // Set the initial state of the panel1Next button
        panel1Next.setDisable(!(directed || undirected) || !(weighted || unweighted));

        // Button Action listeners
        dButton.setOnAction(e -> {
            directed = true;
            undirected = false;
            updateNextButtonState();
        });
        udButton.setOnAction(e -> {
            directed = false;
            undirected = true;
            updateNextButtonState();
        });
        wButton.setOnAction(e -> {
            weighted = true;
            unweighted = false;
            updateNextButtonState();
        });
        uwButton.setOnAction(e -> {
            weighted = false;
            unweighted = true;
            updateNextButtonState();
        });
        panel1Next.setOnAction(e -> loadNextScene());
        panel2Back.setOnAction(e -> loadPrevScene());
    }

    private void updateNextButtonState() {
        // Enable or disable the panel1Next button based on the selections
        boolean isNextButtonEnabled = (directed || undirected) && (weighted || unweighted);
        panel1Next.setDisable(!isNextButtonEnabled);
        if (isNextButtonEnabled) {
            panel1Next.setStyle("-fx-background-color : #487eb0;");
        } else {
            panel1Next.setStyle(null); // Reset to default style if necessary
        }
    }

    void loadNextScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Canvas.fxml"));
            Parent root = loader.load();
            Scene newScene = panel1Next.getScene();
            cref = loader.getController();

            newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Styling.css")).toExternalForm());
            newScene.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(Panel1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void loadPrevScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/algorithmvisualizer/Menu.fxml"));
            Parent menuRoot = loader.load();
            AlgorithmVisualizerMenuController controller = loader.getController();
            Scene scene = panel2Back.getScene();
            controller.setPrimaryScene(scene);
            if (scene == null) {
                System.out.println("Scene is null. Cannot set root.");
            } else {
                scene.setRoot(menuRoot);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
