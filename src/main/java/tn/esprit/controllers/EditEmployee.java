package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.ContractType;
import tn.esprit.models.Employees;
import tn.esprit.models.Functions;
import tn.esprit.models.Sexe;
import tn.esprit.services.ServiceEmployees;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;

public class EditEmployee {
    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private TextField cinField;

    @FXML
    private ChoiceBox<ContractType> contractTypeField;

    @FXML
    private TextField cvField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker endContractField;

    @FXML
    private TextField firstNameField;

    @FXML
    private ChoiceBox<Functions> functionField;

    @FXML
    private DatePicker hiraDateField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ChoiceBox<Sexe> sexeField;
    private HRDashboard dashboard;
    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }
    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }

    @FXML
    void save_employeeOnClick(ActionEvent event) {
        System.out.println(currentEmployee);
        Employees old=currentEmployee;
        Employees e=setInformations(currentEmployee.getId());
        if(old.equals(e)) {
            se.modifier(e);
            // Show notification
            showNotification("Employee updated successfully.");


            // Close the form stage
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();
            dashboard.getEmployeesLayout().getChildren().clear();
            dashboard.showEmployeesList();
        }else {
        // If the employee already exists, show an error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Employee already exists!");
        alert.setContentText("Employee CIN must be unique!");

// Add custom CSS class to the dialog pane
        alert.getDialogPane().getStyleClass().add("error-notification-dialog");

// Show the error alert dialog
        alert.showAndWait();
    }
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
    public void setDetailsData(Employees employee){
        addressField.setText(employee.getEmpAddress());
        birthdayField.setValue(employee.getBirthDate().toLocalDate());
        cinField.setText(employee.getEmpCin());
        contractTypeField.setValue(ContractType.valueOf(employee.getContractType()));
        //cv.setText(employee.getEmpCV().toString());
        if(employee.getEmpCV() == null){
            cvField.setText("no cv uploaded yet!");
        }else {
            cvField.setText("we need a solution for the cv");
        }
        emailField.setText(employee.getEmpEmail());
        endContractField.setValue(employee.getEndContractDate().toLocalDate());
        firstNameField.setText(employee.getEmpName());
        functionField.setValue(Functions.valueOf(employee.getEmpFunction()));

        hiraDateField.setValue(employee.getHireDate().toLocalDate());
        lastNameField.setText(employee.getEmpLastName());
        phoneField.setText(employee.getEmpPhone());
        sexeField.setValue(Sexe.valueOf(employee.getEmpSex()));
    }
    public Employees setInformations(int id){
        try {
            Blob cv =new SerialBlob(cvField.getText().getBytes());
            Employees employee=new Employees(id,firstNameField.getText(),lastNameField.getText(),sexeField.getValue().toString(),emailField.getText(),addressField.getText(),phoneField.getText(),functionField.getValue().toString(), Date.valueOf(birthdayField.getValue()),Date.valueOf(hiraDateField.getValue()),Date.valueOf(endContractField.getValue()),contractTypeField.getValue().toString(),12,cv,0,cinField.getText());

            System.out.println("settttttttttt");
            System.out.println(employee);
            return employee;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    private final ServiceEmployees se = new ServiceEmployees();

    @FXML
    public void initialize(){
        contractTypeField.setItems(FXCollections.observableArrayList(ContractType.values()));
        functionField.setItems(FXCollections.observableArrayList(Functions.values()));
        sexeField.setItems(FXCollections.observableArrayList(Sexe.values()));
    }
}
