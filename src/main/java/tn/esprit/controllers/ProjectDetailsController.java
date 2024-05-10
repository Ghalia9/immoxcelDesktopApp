package tn.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import tn.esprit.models.Projects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import tn.esprit.models.User;

import java.io.IOException;
import java.sql.Date;

public class ProjectDetailsController {

    public DashboardController dashboard;
    @FXML
    private Text username;
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

    @FXML
    private Button tasksButton;

    private Projects project;

    private ProjectsDashboardController projectsDashboardController;

    private LoginController loginController;
    private User user;

    public void setProjectsDashboardController(ProjectsDashboardController controller, LoginController login, User userConnected) {
        this.projectsDashboardController = controller;
        this.loginController=login;
        this.user=userConnected;

    }


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
            editProjectController.setProjectsDashboardController(projectsDashboardController);
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

    @FXML
    void handleProjectTasks(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ShowTasks.fxml"));
            Parent parent = fxmlLoader.load();

            ShowTasksController showTasksController = fxmlLoader.getController();
            showTasksController.setData(project,loginController,user);
            // Pass the Projects object
            showTasksController.refreshTasks();


            if(dashboard==null)
            {
                projectsDashboardController.paneToChange.getChildren().setAll(showTasksController.paneToChange.getChildren());
            }
            else{
                dashboard.ShowTasks(showTasksController);
            }
            // Close all open windows except the ShowTasks stage
            /*closeAllWindowsExceptShowTasks();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Tasks");
            stage.setScene(new Scene(parent));
            stage.showAndWait();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeAllWindowsExceptShowTasks() {
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }
        tasksButton.getScene().getWindow().hide();
    }


    private String formatDate(Date date) {
        return date != null ? date.toString() : "Not set yet";
    }
}
