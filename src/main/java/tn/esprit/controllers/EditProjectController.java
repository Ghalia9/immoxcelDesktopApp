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
    private void initialize() {
        createButton.setOnAction(event -> updateProject());
    }

    @FXML
    private void updateProject() {
        try {
            // Get data from input fields
            String projectName = projectNameInput.getText();
            float budget = Float.parseFloat(budgetInput.getText());
            LocalDate predStartDate = predStartDateInput.getValue();
            LocalDate predFinishDate = predFinishDateInput.getValue();

            // Convert LocalDate to Date
            Date startDate = Date.valueOf(predStartDate);
            Date finishDate = Date.valueOf(predFinishDate);

            // Update the existing project
            project.setProject_name(projectName);
            project.setBudget(budget);
            project.setDate_pred_start(startDate);
            project.setDate_pred_finish(finishDate);

            // Call service to update project in database
            serviceProjects.modifier(project);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project updated successfully");

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
