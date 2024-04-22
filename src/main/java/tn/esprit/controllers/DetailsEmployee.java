package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Employees;

public class DetailsEmployee {
    @FXML
    private Label address;

    @FXML
    private Label birthdate;

    @FXML
    private Label cin;

    @FXML
    private Label conractType;

    @FXML
    private Label cv;

    @FXML
    private Label email;

    @FXML
    private Label endContractDate;

    @FXML
    private Label firstName;

    @FXML
    private Label function;

    @FXML
    private Label hireDate;

    @FXML
    private Label lastname;

    @FXML
    private Label phone;

    @FXML
    private Label sexe;

    @FXML
    void addLeave_employeeDetailsOnClick(ActionEvent event) {

    }

    @FXML
    void edit_employeeDetailsOnClick(ActionEvent event) {

    }
    public void setDetailsData(Employees employee){
        address.setText(employee.getEmpAddress());
        birthdate.setText(employee.getBirthDate().toString());
        cin.setText(employee.getEmpCin());
        conractType.setText(employee.getContractType());
        //cv.setText(employee.getEmpCV().toString());
        if(employee.getEmpCV() == null){
            cv.setText("no cv uploaded yet!");
        }else {
            cv.setText("we need a solution for the cv");
        }
        email.setText(employee.getEmpEmail());
        endContractDate.setText(employee.getEndContractDate().toString());
        firstName.setText(employee.getEmpName());
        function.setText(employee.getEmpFunction());
        hireDate.setText(employee.getHireDate().toString());
        lastname.setText(employee.getEmpLastName());
        phone.setText(employee.getEmpPhone());
        sexe.setText(employee.getEmpSex());
    }
}
