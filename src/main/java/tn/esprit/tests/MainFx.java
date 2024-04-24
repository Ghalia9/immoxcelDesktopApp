package tn.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Connection connection = DataSource.getInstance().getCnx();
        if (connection != null && !connection.isClosed()) {
            System.out.println("Database connection established successfully.");
        } else {
            System.out.println("Failed to establish database connection.");
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowDepots.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add User");
        stage.show();
    }
}
