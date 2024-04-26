package tn.esprit.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.models.Projects;
import tn.esprit.models.Tasks;
import tn.esprit.services.ServiceTasks;

import java.sql.Date;
import java.time.LocalDate;

public class AddTaskController {

    @FXML
    private Button createButton;

    @FXML
    private DatePicker taskDeadlineInput;

    @FXML
    private TextArea taskDescriptionInput;

    @FXML
    private TextField taskTitleInput;

    private final ServiceTasks serviceTasks = new ServiceTasks();
    private BooleanProperty taskAdded = new SimpleBooleanProperty(false);
    private Projects project;
    public void setTaskAddedProperty(BooleanProperty taskAdded) {
        this.taskAdded = taskAdded;
    }

    public void setProject(Projects project){
        this.project=project;
    }

    @FXML
    private void addTask() {
        try {
            // Get data from input fields
            String title = taskTitleInput.getText();
            String description = taskDescriptionInput.getText();
            LocalDate deadline = taskDeadlineInput.getValue();

            // Validate input
            if (title.isEmpty() || deadline == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return;
            }

            // Check if the deadline is in the future
            LocalDate today = LocalDate.now();
            if (deadline.isBefore(today)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Task deadline must be in the future.");
                return;
            }

            // Convert LocalDate to Date
            Date taskDeadline = Date.valueOf(deadline);

            // Create Tasks object
            Tasks task = new Tasks(title, description, taskDeadline, "To Do", null); // Assuming "To Do" status for new tasks
            task.setProject(project);

            // Save task to database or perform any other action
            serviceTasks.ajouter(task);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Task added successfully");

            // Clear input fields
            clearFields();

            // Notify parent controller that a task was added
            taskAdded.set(true);

            // Close the AddTask window
            createButton.getScene().getWindow().hide();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add task. Check input data.");
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        taskTitleInput.clear();
        taskDescriptionInput.clear();
        taskDeadlineInput.setValue(null); // Clear the selected date
    }
}
