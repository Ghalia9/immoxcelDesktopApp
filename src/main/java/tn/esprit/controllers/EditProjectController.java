package tn.esprit.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import tn.esprit.models.Projects;
import tn.esprit.services.ServiceProjects;

import java.sql.Date;
import java.time.LocalDate;

public class EditProjectController {

    @FXML
    private TextField projectNameInput;

    @FXML
    private TextField budgetInput;

    @FXML
    private DatePicker predStartDateInput;

    @FXML
    private DatePicker predFinishDateInput;

    @FXML
    private TextField costInput;

    @FXML
    private DatePicker completionDateInput;

    @FXML
    private Button createButton;

    private Projects project;
    private final ServiceProjects serviceProjects = new ServiceProjects();
    private BooleanProperty projectUpdated = new SimpleBooleanProperty(false);

    private ProjectsDashboardController projectsDashboardController;

    public void setProjectsDashboardController(ProjectsDashboardController controller) {
        this.projectsDashboardController = controller;
    }

    public void setData(Projects project) {
        this.project = project;
        projectNameInput.setText(project.getProject_name());
        budgetInput.setText(String.valueOf(project.getBudget()));
        predStartDateInput.setValue(convertToLocalDate(project.getDate_pred_start()));
        predFinishDateInput.setValue(convertToLocalDate(project.getDate_pred_finish()));
        costInput.setText(String.valueOf(project.getActual_cost()));
        completionDateInput.setValue(convertToLocalDate(project.getDate_completion()));
    }


    @FXML
    private void updateProject() {
        try {
            // Get data from input fields
            String projectName = projectNameInput.getText();
            String budgetText = budgetInput.getText();
            LocalDate predStartDate = predStartDateInput.getValue();
            LocalDate predFinishDate = predFinishDateInput.getValue();
            LocalDate completionDate = completionDateInput.getValue(); // Might be null

            // Check if any required input field is null or empty
            if (projectName.isEmpty() || budgetText.isEmpty() || predStartDate == null || predFinishDate == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields");
                return; // Exit the method early if any required field is empty or null
            }

            float budget = Float.parseFloat(budgetText);

// Check if project name is unique if it has been modified
            if (!projectName.equals(project.getProject_name()) && !serviceProjects.isProjectNameUnique(projectName)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Project name must be unique");
                return; // Exit the method if project name is not unique
            }

            // Check if predicted finish date is after predicted start date
            if (!predFinishDate.isAfter(predStartDate)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Predicted finish date must be after predicted start date");
                return; // Exit the method if predicted finish date is not after predicted start date
            }

            // Validate completion date if not null
            Date completion = null;
            if (completionDate != null) {
                completion = Date.valueOf(completionDate);
                if (completion.before(Date.valueOf(predStartDate))) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Completion date must be after predicted start date");
                    return; // Exit the method if completion date is before predicted start date
                }
            }

            float cost;
            if (!costInput.getText().isEmpty()) {
                cost = Float.parseFloat(costInput.getText());
            } else {
                cost = 0; // Set a default value if cost is empty
            }

            // Convert LocalDate to Date
            Date startDate = Date.valueOf(predStartDate);
            Date finishDate = Date.valueOf(predFinishDate);

            // Update the existing project
            project.setProject_name(projectName);
            project.setBudget(budget);
            project.setDate_pred_start(startDate);
            project.setDate_pred_finish(finishDate);
            project.setActual_cost(cost);
            project.setDate_completion(completion);

            // Call service to update project in database
            serviceProjects.modifier(project);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project updated successfully");

            // Updating projects
            projectsDashboardController.refreshProjects();

            // Notify ProjectsDashboardController that a project was updated
            projectUpdated.set(true);

            // Close the EditProject window
            createButton.getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Budget must be a valid number");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update project. Check input data.");
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
        projectNameInput.clear();
        budgetInput.clear();
        predStartDateInput.setValue(null); // Clear the selected date
        predFinishDateInput.setValue(null); // Clear the selected date
    }

    // Helper method to convert Date to LocalDate
    private LocalDate convertToLocalDate(Date date) {
        return (date != null) ? date.toLocalDate() : null;
    }
}
