package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.Initializable;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;


public class ShowLeavePerEmployee
{
    @FXML
    private VBox leavesDisplay;
    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }
    /*@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(currentEmployee);

        if (currentEmployee != null) {
            System.out.println("yeeeeey");
            displayLeaves(currentEmployee);
        }
        else System.out.println("booooooooooo");
    }*/
    private final ServiceLeaves sl= new ServiceLeaves();
    private final ServiceEmployees se = new ServiceEmployees();

    public void displayLeaves(Employees currentEmployee) {
        // Retrieve the list of leaves for the current employee
        if (currentEmployee != null) {
            System.out.println("seeeeeeeeeee");
        }
        System.out.println(currentEmployee);
        Set<Leaves> leaves = sl.getLeavesByEmployee(currentEmployee);

        // Clear existing leaves from the container
        leavesDisplay.getChildren().clear();

        // Iterate over the leaves and create a visual representation for each one
        for (Leaves leave : leaves) {
            if (leave != null) {
                System.out.println(leave);
                // Create a new instance of AfficherLeave for each leave
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/LeavesEmployee.fxml"));
                try {
                    HBox lBox = loader.load();
                    LeavesEmployee leavesPerEmployee = loader.getController();
                    leavesPerEmployee.setDataALL(leave);
                    leavesDisplay.getChildren().add(lBox);
                    leavesPerEmployee.setCurrentLeave(leave);
                   // editLeave.setCurrentLeave(leave);
                    leavesPerEmployee.setCurrentEmployee(leave.getEmployee());
                    leavesPerEmployee.setdashbord(dashboard);
                    leavesPerEmployee.setDisplay(leavesDisplay);//VBox for instant refresh
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    HRDashboard dashboard;
    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }



}
