package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.models.Projects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.Date;

public class ProjectDetailsController {

    @FXML
    private Label projectName;

    @FXML
    private Label budget;

    @FXML
    private Label completion_date;

    @FXML
    private Label cost;

    @FXML
    private Label pred_finish_date;

    @FXML
    private Label pred_start_date;

    @FXML
    private Button editButtton;

    private Projects project;


    public void setData(Projects project){
        this.project=project;
        projectName.setText(project.getProject_name());
        pred_start_date.setText(formatDate(project.getDate_pred_start()));
        pred_finish_date.setText(formatDate(project.getDate_pred_finish()));
        budget.setText(String.valueOf(project.getBudget()));
        cost.setText(String.valueOf(project.getActual_cost()));
        completion_date.setText(formatDate(project.getDate_completion()));
    }
    @FXML
    private void handleEditProject() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EditProject.fxml"));
            Parent parent = fxmlLoader.load();

            EditProjectController editProjectController = fxmlLoader.getController();
            editProjectController.setData(project);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Project");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String formatDate(Date date) {
        return date != null ? date.toString() : "Not set yet";
    }
}
