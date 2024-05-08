package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import tn.esprit.models.Tasks;
import tn.esprit.services.ServiceProjects;
import tn.esprit.services.ServiceTasks;

public class TaskItemController {
private Tasks task;
    @FXML
    private Button deleteTaskButton;

    @FXML
    private Label taskDeadlineLabel;

    @FXML
    private Label taskTitleLabel;

    private final ServiceTasks serviceTasks = new ServiceTasks();

    private ShowTasksController showTasksController;
    public void setShowTasksController(ShowTasksController controller) {
        this.showTasksController = controller;
    }
    @FXML
    private void initialize() {
        // Link the handleDeleteProject method to the click event of projectDeleteButton
        deleteTaskButton.setOnMouseClicked(this::handleDeleteTask);

    }

    public void setTask(Tasks task) {
        this.task=task;
        taskTitleLabel.setText(task.getTitle());
        taskDeadlineLabel.setText(task.getDeadline().toString());
    }
    @FXML
    private void handleDeleteTask(MouseEvent event) {
        if (task != null) {
            // Prompt user for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Task");
            alert.setHeaderText("Delete Task Confirmation");
            alert.setContentText("Are you sure you want to delete this Task?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete project from database
                    serviceTasks.supprimer(task.getId());
                    showTasksController.refreshTasks();
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
