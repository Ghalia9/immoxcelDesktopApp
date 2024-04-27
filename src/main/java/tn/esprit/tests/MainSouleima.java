package tn.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static javafx.application.Application.launch;

public class MainSouleima extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Dispa
        FXMLLoader fxmlLoader = new FXMLLoader(MainSouleima.class.getResource("/Display.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1105, 755);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}