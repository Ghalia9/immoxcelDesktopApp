package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import tn.esprit.models.Tasks;
import tn.esprit.services.ServiceProjects;
import tn.esprit.services.ServiceTasks;

import java.util.Objects;

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


        taskTitleLabel.setOnDragDetected(this::onDragDetected);
        taskDeadlineLabel.setOnDragDetected(this::onDragDetected);
        deleteTaskButton.setOnDragDetected(this::onDragDetected);
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

    // Drag handlers
    @FXML
    private void onDragDetected(MouseEvent event) {
        System.out.println("dragging");
        Dragboard dragboard = taskTitleLabel.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(task.getTitle()); // Pass task ID or relevant data
        System.out.println(task.getTitle());
        dragboard.setContent(content);
        event.consume();
    }
    @FXML
    private void onDragDone(DragEvent event) {
        System.out.println("Drag Done");
        event.consume();
    }

    @FXML
    private void onDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasString()) {
            String taskName = dragboard.getString();
            // Determine where the task was dropped
            if (Objects.equals(task.getStatus(), "Doing"))  {
                // Update task status to "Doing"
                serviceTasks.updateStatus(taskName, "Doing");
                success = true;
            } else if (Objects.equals(task.getStatus(), "Done"))  {
                // Update task status to "Done"
                serviceTasks.updateStatus(taskName, "Done");
                success = true;
            } else if (Objects.equals(task.getStatus(), "To Do"))  {
                // Update task status to "Done"
                serviceTasks.updateStatus(taskName, "To Do");
                success = true;
            }
            // Refresh tasks view
            if (success) {
                showTasksController.refreshTasks();
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
}

