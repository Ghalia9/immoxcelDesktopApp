module com.example.myjavafxapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.xml.crypto;
    requires json.simple;


    opens com.example.myjavafxapp to javafx.fxml;
    exports com.example.myjavafxapp;
}