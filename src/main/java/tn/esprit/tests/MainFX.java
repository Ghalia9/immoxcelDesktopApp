package tn.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    public void start(Stage stage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/ShowTasks.fxml"));
        Parent root = loader.load();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.setTitle("mine");
        stage.show();
    }
    public static void main(String[] args) {
launch();
    }
}
