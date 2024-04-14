module com.example.algorithmvisualizer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.algorithmvisualizer to javafx.fxml;
    exports com.example.algorithmvisualizer;
}