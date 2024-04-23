package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import tn.esprit.models.Projects;
import tn.esprit.services.ServiceProjects;

public class ProjectItemController {

    @FXML
    private Label onGoingBudgetLabel;

    @FXML
    private Label projectFinishDateLabel;

    @FXML
    private Label projectNameLabel;

    @FXML
    private Label projectStartDateLabel;

    @FXML
    private Button projectDelete;

    private Projects project;

    private final ServiceProjects serviceProjects = new ServiceProjects();

    private ProjectsDashboardController projectsDashboardController;

    public void setProjectsDashboardController(ProjectsDashboardController controller) {
        this.projectsDashboardController = controller;
    }
    @FXML
    private void initialize() {
        // Link the handleDeleteProject method to the click event of projectDeleteButton
        projectDelete.setOnMouseClicked(this::handleDeleteProject);

    }

    public void setData(Projects project){
        this.project=project;
        projectNameLabel.setText(project.getProject_name());
        projectStartDateLabel.setText(project.getDate_pred_start().toString());
        projectFinishDateLabel.setText(project.getDate_pred_finish().toString());
        onGoingBudgetLabel.setText(String.valueOf(project.getBudget()));
    }

    @FXML
    private void handleDeleteProject(MouseEvent event) {
        if (project != null) {
            // Prompt user for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Project");
            alert.setHeaderText("Delete Project Confirmation");
            alert.setContentText("Are you sure you want to delete this project?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete project from database
                    serviceProjects.supprimer(project.getId());
                        projectsDashboardController.refreshProjects();
                    // For simplicity, we'll just show an alert
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Project deleted successfully");
                }
            });
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
