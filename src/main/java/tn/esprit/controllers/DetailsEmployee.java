package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

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
    private Label allowedLeaveDays;
    @FXML
    private Label takenLeaveDays;
    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }
    @FXML
    private AnchorPane displayPage;

    //public void setDisplayPage(AnchorPane displayPage)
    {
        this.displayPage=displayPage;
    }
    @FXML
    void showLeavesOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowLeavePerEmployee.fxml"));
            Parent leaves = loader.load();
            ShowLeavePerEmployee employeeLeaves=loader.getController();
            employeeLeaves.setdashbord(dashboard);
            System.out.println(currentEmployee);
            employeeLeaves.displayLeaves(currentEmployee);
            if (currentEmployee != null) {
                employeeLeaves.setCurrentEmployee(currentEmployee);
            } else {
                System.out.println("currentEmployee is null!");
            }            Stage leavesEmpStage = new Stage();
            leavesEmpStage.initStyle(StageStyle.DECORATED);
            leavesEmpStage.setScene(new Scene(leaves, 343, 400));
            leavesEmpStage.setTitle("Employee leaves");
            //employeeLeaves.setDetailsData(currentEmployee);
            System.out.println(currentEmployee);
            leavesEmpStage.showAndWait();
            // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    @FXML
    void addLeave_employeeDetailsOnClick(ActionEvent event) {
        System.out.println("whyyyyyyyyyyyyy");
        System.out.println(currentEmployee.getEmpTakenLeaves());
        System.out.println(currentEmployee.getAllowedLeaveDays());

        if (currentEmployee.getEmpTakenLeaves() >= currentEmployee.getAllowedLeaveDays()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot add leave. Maximum leaves allowed for this employee is:"+currentEmployee.getAllowedLeaveDays());

            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddLeave.fxml"));
                Parent root = loader.load();
                AddLeave leave = loader.getController();
                leave.setdashbord(dashboard);
                System.out.println(currentEmployee);
                leave.setCurrentEmployee(currentEmployee);
                Stage addLeaveStage = new Stage();
                addLeaveStage.initStyle(StageStyle.DECORATED);
                addLeaveStage.setScene(new Scene(root, 337, 405));
                addLeaveStage.setTitle("Add leave Form");
                addLeaveStage.showAndWait();
                // showEmployeesList();
                // Close the details stage
                Stage stage = (Stage) firstName.getScene().getWindow();

                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    @FXML
    void edit_employeeDetailsOnClick(ActionEvent event) {
        try {
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa");
            System.out.println(currentEmployee);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditEmployee.fxml"));
            Parent Edit = loader.load();
            EditEmployee employeeedit=loader.getController();
            if (dashboard==null)
                System.out.println("hneeeeeeeeeeeeeeeeeeeeeeee");
            employeeedit.setdashbord(dashboard);
            Stage editEmpStage = new Stage();
            editEmpStage.initStyle(StageStyle.DECORATED);
            editEmpStage.setScene(new Scene(Edit, 638, 574));
            editEmpStage.setTitle("Edit Employee");
            employeeedit.setDetailsData(currentEmployee);
            employeeedit.setCurrentEmployee(currentEmployee);
            employeeedit.setDisplayPage(displayPage);
            System.out.println(currentEmployee);
            editEmpStage.showAndWait();
            // Close the details stage
            Stage stage = (Stage) firstName.getScene().getWindow();
            stage.close();
            // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
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
        takenLeaveDays.setText(String.valueOf(employee.getEmpTakenLeaves()));
        allowedLeaveDays.setText(String.valueOf(employee.getAllowedLeaveDays()));
    }
    private HRDashboard dashboard;

    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }
}
