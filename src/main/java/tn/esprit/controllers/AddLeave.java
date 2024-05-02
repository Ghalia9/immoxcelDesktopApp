package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tn.esprit.models.*;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddLeave {
    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker finishDateField;

    @FXML
    private ComboBox<LeaveType> leaveTypeField;

    @FXML
    private DatePicker startDateField;

    private HRDashboard dashboard;

    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }

    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }

    private DisplayLeaves dl;

    public void setDl(DisplayLeaves dl) {
        this.dl = dl;
    }

    private final ServiceLeaves sl=new ServiceLeaves();
    @FXML
    void addLeaveOnClick(ActionEvent event) {
        if (!allFieldsFilled()) {
            showValidationError("All fields must be filled.");
            return;
        }
        if (validateLeaveInputs()) {

            Leaves l=setInformations();


        Leaves[] leave = currentEmployee.getListofleaves();

        System.out.println(leave);
        int index = currentEmployee.getEmpTakenLeaves(); // Calculate the index

        if (index <= leave.length) {
            // Assign a value to the array element at the calculated index
            sl.ajouter(l);

            leave[index] = l;

        } else {
            // Handle the case where the index is out of bounds
            System.out.println("Index out of bounds");
        }
        currentEmployee.setListofleaves(leave);
            // Show notification
        for (Leaves le : leave) {
            System.out.println(le);
        }
            showNotification("Leave added successfully.");

            System.out.println(l);
            System.out.println(currentEmployee);
            // Close the form stage
            Stage stage = (Stage) descriptionField.getScene().getWindow();
            stage.close();
            /*
            dashboard.getCardLayout().getChildren().clear();
            dashboard.showPendingLeaves();

             */
    }}
    private boolean validateLeaveInputs() {
        // Check if leave type is selected
        if (leaveTypeField.getValue() == null) {
            showValidationError("Please select a leave type.");
            return false;
        }

        // Check if start date and end date are selected
        LocalDate startDate = startDateField.getValue();
        LocalDate endDate = finishDateField.getValue();
        LocalDate currentDate = LocalDate.now();

        if (startDate == null || endDate == null) {
            showValidationError("Please select start and end dates.");
            return false;
        }

        // Check if start date is greater than or equal to current date
        if (startDate.isBefore(currentDate)) {
            showValidationError("Start date must be on or after the current date.");
            return false;
        }

        // Check if end date is greater than or equal to start date
        if (endDate.isBefore(startDate)) {
            showValidationError("End date must be on or after the start date.");
            return false;
        }

        // All inputs are valid
        return true;
    }
    private boolean allFieldsFilled() {
        return  startDateField.getValue() != null &&
                finishDateField.getValue() != null &&
                leaveTypeField.getValue() != null;
    }
    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        // Schedule closing of the alert after the specified duration
       /* Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> alert.close())
        );
        timeline.play();

        */
    }
    private final ServiceEmployees se = new ServiceEmployees();

    @FXML
    public void initialize(){
        leaveTypeField.setItems(FXCollections.observableArrayList(LeaveType.values()));

    }

    public Leaves setInformations(){
        Leaves leave=new Leaves(leaveTypeField.getValue().toString(), Date.valueOf(startDateField.getValue()),Date.valueOf(finishDateField.getValue()),"Pending",descriptionField.getText(),currentEmployee);
        return leave;

    }
}
