package tn.esprit.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Tasks;
import tn.esprit.services.ServiceTasks;

import java.sql.Date;
import java.time.LocalDate;

public class EditTaskController {

    @FXML
    private DatePicker taskCompletionDateInput;

    @FXML
    private DatePicker taskDeadlineInput;

    @FXML
    private TextArea taskDescriptionInput;

    @FXML
    private TextField taskTitleInput;


    @FXML
    private TextField taskStatusInput;

    @FXML
    private Button updateButton;

    private Tasks task;
    private final ServiceTasks serviceTasks = new ServiceTasks();
    private BooleanProperty taskUpdated = new SimpleBooleanProperty(false);

    private ShowTasksController showTasksController;

    public void setShowTasksController(ShowTasksController controller) {
        this.showTasksController = controller;
    }

    public void setData(Tasks task) {
        this.task = task;
        taskTitleInput.setText(task.getTitle());
        taskDescriptionInput.setText(task.getDescription());
        taskDeadlineInput.setValue(convertToLocalDate(task.getDeadline()));
        taskStatusInput.setText(task.getStatus());
    }

    @FXML
    private void updateTask() {
        try {
            // Get data from input fields
            String title = taskTitleInput.getText();
            String description = taskDescriptionInput.getText();
            LocalDate deadline = taskDeadlineInput.getValue();
            String status = taskStatusInput.getText();

            // Perform input validation
            if (title.isEmpty()  || deadline == null || status.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                return; // Exit the method if any field is empty
            }

            Date taskDeadline = Date.valueOf(deadline);

            // Update the existing task
            task.setTitle(title);
            task.setDescription(description);
            task.setDeadline(taskDeadline);
            task.setStatus(status);

            // Check if the status is set to "Done" and update the completion date accordingly
            if (status.equals("Done")) {
                LocalDate completionDate = LocalDate.now(); // Assuming completion date is set to current date
                task.setCompletion_date(Date.valueOf(completionDate));
            } else {
                task.setCompletion_date(null); // Set completion date to null
            }

            // Call service to update task in database
            serviceTasks.modifier(task);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Task updated successfully");

            // Updating projects
            showTasksController.refreshTasks();

            // Notify ShowTasksController that a task was updated
            taskUpdated.set(true);

            // Close the EditTask window
            updateButton.getScene().getWindow().hide();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update task. Check input data.");
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

    // Helper method to convert Date to LocalDate
    private LocalDate convertToLocalDate(Date date) {
        return (date != null) ? date.toLocalDate() : null;
    }
}
