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
import javafx.fxml.Initializable;


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
    public void displayLeaves(Employees currentEmployee) {
        // Retrieve the list of leaves for the current employee
        if (currentEmployee != null) {
            System.out.println("seeeeeeeeeee");
        }
        System.out.println(currentEmployee);
        Leaves[] leaves = currentEmployee.getListofleaves();

        // Clear existing leaves from the container
        leavesDisplay.getChildren().clear();

        // Iterate over the leaves and create a visual representation for each one
        for (Leaves leave : leaves) {
            System.out.println(leave);
            // Create a new instance of AfficherLeave for each leave
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OldLeave.fxml"));
            try {


                HBox lBox=loader.load();
                AfficherLeaves afficherLeaves=loader.getController();
                afficherLeaves.setDataALL(leave);
                leavesDisplay.getChildren().add(lBox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    HRDashboard dashboard;
    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }



}
