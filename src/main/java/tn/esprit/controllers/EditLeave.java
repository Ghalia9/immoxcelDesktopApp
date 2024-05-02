package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.ContractType;
import tn.esprit.models.Employees;
import tn.esprit.models.LeaveType;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;

import java.sql.Date;
import java.time.LocalDate;

public class EditLeave {
    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker finishDateField;

    @FXML
    private ComboBox<LeaveType> leaveTypeField;

    @FXML
    private DatePicker startDateField;
    private VBox display;

    public void setDisplay(VBox display)
    {
        this.display=display;
    }
    private DisplayLeaves dl;

    public void setDl(DisplayLeaves dl) {
        this.dl = dl;
    }

    @FXML
    void saveLeaveOnClick(ActionEvent event) {

        if (validateLeaveInputs()) {
            if (currentLeave == null)
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Leaves old = currentLeave;

        Leaves l = setInformations(currentLeave.getId());
        System.out.println("leaveeeeeeeeeeeeeee");
        System.out.println(l);
        if (l.getStatus().equals("Pending")) {
            sl.modifier(l);
            showNotification("Leave updated successfully.");
            Stage stage = (Stage) descriptionField.getScene().getWindow();
            stage.close();
            Stage stage2 = (Stage) display.getScene().getWindow();
            stage2.close();
            //dl.getCardLayout().getChildren().clear();
            //dl.showPendingLeaves();
        } else {
            // Display a message saying only pending leaves can be deleted
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Only pending leaves can be deleted.");
            alert.showAndWait();
        }
    }

        // Close the form stage

        //display.getChildren().clear();

    }

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

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setDetailsData(Leaves leave) {
        leaveTypeField.setValue(LeaveType.valueOf(leave.getLeaveType()));
        descriptionField.setText(leave.getLeaveDescription());
        startDateField.setValue(leave.getStartDate().toLocalDate());
        finishDateField.setValue(leave.getFinishDate().toLocalDate());
    }
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
    private Leaves currentLeave;

    public void setCurrentLeave(Leaves currentLeave)
    {
        this.currentLeave=currentLeave;
    }
    private final ServiceLeaves sl=new ServiceLeaves();

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

    public Leaves setInformations(int id){
        Leaves leave=new Leaves(id,leaveTypeField.getValue().toString(), Date.valueOf(startDateField.getValue()),Date.valueOf(finishDateField.getValue()),"Pending",descriptionField.getText(),currentEmployee);
        return leave;

    }


}
