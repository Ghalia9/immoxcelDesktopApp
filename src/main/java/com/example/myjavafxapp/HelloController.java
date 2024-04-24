package com.example.myjavafxapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Label HelloSouleima ;   @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void ClickHere() {
        HelloSouleima.setText("Hello Hell World !");
    }

}