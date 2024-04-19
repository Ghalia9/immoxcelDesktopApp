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
import javax.sql.rowset.serial.SerialException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;

public class AddEmployee {
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

    @FXML
    void add_employeeOnClick(ActionEvent event) {
        Employees e=setInformations();
        se.ajouter(e);
        // Show notification
        showNotification("Employee added successfully.");

        // Close the form stage
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    private void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private final ServiceEmployees se = new ServiceEmployees();

    @FXML
            public void initialize(){
        contractTypeField.setItems(FXCollections.observableArrayList(ContractType.values()));
        functionField.setItems(FXCollections.observableArrayList(Functions.values()));
        sexeField.setItems(FXCollections.observableArrayList(Sexe.values()));
    }

    public Employees setInformations(){
        try {
            Blob cv =new SerialBlob(cvField.getText().getBytes());
            Employees employee=new Employees(firstNameField.getText(),lastNameField.getText(),sexeField.getValue().toString(),emailField.getText(),addressField.getText(),phoneField.getText(),functionField.getValue().toString(), Date.valueOf(birthdayField.getValue()),Date.valueOf(hiraDateField.getValue()),Date.valueOf(endContractField.getValue()),contractTypeField.getValue().toString(),12,cv,0,cinField.getText());
            return employee;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
