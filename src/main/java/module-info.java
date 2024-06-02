module com.example.algorithmvisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires java.datatransfer;
    requires java.desktop;


    opens com.example.algorithmvisualizer to javafx.fxml;
    exports com.example.algorithmvisualizer;
}