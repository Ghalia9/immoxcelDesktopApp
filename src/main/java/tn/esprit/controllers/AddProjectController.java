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

import java.time.LocalDate;
import java.sql.Date;

public class AddProjectController {

    @FXML
    private TextField budgetInput;

    @FXML
    private DatePicker predFinishDateInput;

    @FXML
    private DatePicker predStartDateInput;

    @FXML
    private TextField projectNameInput;

    @FXML
    private Button createButton;

    private final ServiceProjects serviceProjects = new ServiceProjects();
    private BooleanProperty projectAdded = new SimpleBooleanProperty(false);

    public void setProjectAddedProperty(BooleanProperty projectAdded) {
        this.projectAdded = projectAdded;
    }

    @FXML
    private void initialize() {
        // Lier la méthode addProject() à l'événement de clic du bouton createButton
        createButton.setOnAction(event -> addProject());
    }

    @FXML
    private void addProject() {
        try {
            // Get data from input fields
            String projectName = projectNameInput.getText();
            float budget = Float.parseFloat(budgetInput.getText());
            LocalDate predStartDate = predStartDateInput.getValue();
            LocalDate predFinishDate = predFinishDateInput.getValue();

            // Convert LocalDate to Date
            Date startDate = Date.valueOf(predStartDate);
            Date finishDate = Date.valueOf(predFinishDate);

            // Create Projects object
            Projects project = new Projects(projectName, startDate, finishDate, budget);

            // Save project to database or perform any other action
            serviceProjects.ajouter(project);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project added successfully");

            // Clear input fields
            clearFields();

            // Notify ProjectsDashboardController that a project was added
            projectAdded.set(true);

            // Close the AddProject window
            createButton.getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Budget must be a valid number");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add project. Check input data.");
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
}
