package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import tn.esprit.models.Employees;

public class EmployeeCardController {

    AddAccountController AddController;

    public Employees emp;

    public void setAdd(AddAccountController Add,Employees employee)
    {
        this.AddController=Add;
        this.emp=employee;
    }

    @FXML
    private HBox Add;

    @FXML
    private Label Function;

    @FXML
    private HBox cancelAdd;

    @FXML
    public Label fullName;



    public void SetCard(Employees emp)
    {
        fullName.setText(emp.getEmpName()+" "+emp.getEmpLastName());
        Function.setText(emp.getEmpFunction());
    }

    @FXML
    void AddUser(MouseEvent event) {
        AddController.setUserToAdd(emp.getId(),fullName.getText());
        AddController.setEmployeeCard(this);
    }

    @FXML
    void CancelDelete(MouseEvent event) {
        AddController.setUserToAdd(0,"");
    }
}
