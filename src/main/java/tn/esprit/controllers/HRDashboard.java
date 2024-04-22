package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;
import tn.esprit.controllers.AfficherEmployees;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class HRDashboard implements Initializable {
    @FXML
    private HBox cardLayout;
    @FXML
    private VBox leavesLayout;
    private List<Leaves> pendingLeaves,oldleaves;

    @FXML
    private VBox employeesLayout;

    public VBox getEmployeesLayout() {
        return employeesLayout;
    }

    public HBox getCardLayout() {
        return cardLayout;
    }

    public VBox getLeavesLayout() {
        return leavesLayout;
    }

    private final ServiceLeaves sl= new ServiceLeaves();
    private final ServiceEmployees se = new ServiceEmployees();

    private int idSelectedEmployee(Employees employee){
        return employee.getId();
    }
    //button add on top of list of employees
    @FXML
    void addOnClick(ActionEvent event) throws IOException {
        createAddEmployeeForm();
        //showEmployeesList();

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //pendingLeaves=new ArrayList<>(pendingLeaves());
        //PENDING LEAVES
        showPendingLeaves();
        //ALLLEAVES

        showOldLeaves();
        //EMPLOYEESLIST
        showEmployeesList();
    }
    public void showEmployeesList(){
        Set<Employees> setemployees= se.getAll();
        List<Employees> employeesList=new ArrayList<>(setemployees);
        try{
            for (int i=0;i<employeesList.size();i++){
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/EmployeeAfficher.fxml"));
                HBox hBox=loader.load();
                AfficherEmployees afficheremployees=loader.getController();
                afficheremployees.setCurrentEmployee(employeesList.get(i));
                afficheremployees.setData(employeesList.get(i));
                employeesLayout.getChildren().add(hBox);
                afficheremployees.employeesLayout=employeesLayout;
                // afficheremployees.delete_employee();
            }
        }catch (IOException e){
            System.out.println("aaaaaaaaaaaaaaaaaa");
            e.printStackTrace();
        }
    }
public void showPendingLeaves(){
    pendingLeaves=new ArrayList<>(pendingLeaves());
    //PENDING LEAVES
    try{
        for (int i=0;i<pendingLeaves.size();i++){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/CardLeaves.fxml"));
            HBox cardBox=loader.load();
            AfficherLeaves afficherLeaves=loader.getController();
            afficherLeaves.setdashbord(this);
            afficherLeaves.setCurrentLeave(pendingLeaves.get(i));
            afficherLeaves.setData(pendingLeaves.get(i));
            cardLayout.getChildren().add(cardBox);
        }

    }catch (IOException e){
        System.out.println("aaaaaaaaaaaaaaaaaa");
        e.printStackTrace();
    }
}
public void showOldLeaves(){
    oldleaves=new ArrayList<>(oldLeaves());
    try{
        for (int i=0;i<oldleaves.size();i++){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/OldLeave.fxml"));
            HBox lBox=loader.load();
            AfficherLeaves afficherLeaves=loader.getController();
            afficherLeaves.setDataALL(oldleaves.get(i));
            leavesLayout.getChildren().add(lBox);
        }

    }catch (IOException e){
        System.out.println("aaaaaaaaaaaaaaaaaa");
        e.printStackTrace();
    }
}
    private List<Leaves> pendingLeaves(){
        Set<Leaves> allLeaves=sl.getAll();
        List<Leaves> pl=new ArrayList<>();
        for (Leaves leave : allLeaves) {
            System.out.println(leave);
            //leave.setStatus("Approved");
            if (leave.getStatus().equals("Pending")) {
                pl.add(leave);
            }
        }
        System.out.println("list listinggg");
        System.out.println(allLeaves);
        System.out.println(pl);
        return pl;
    }
    private List<Leaves> oldLeaves(){
        Set<Leaves> allLeaves=sl.getAll();
        List<Leaves> oldleaves=new ArrayList<>();
        for (Leaves leave : allLeaves) {
            System.out.println(leave);
            //leave.setStatus("Approved");
            if (!leave.getStatus().equals("Pending")) {
                oldleaves.add(leave);
            }
        }
       /* System.out.println("list listinggg");
        System.out.println(allLeaves);
        System.out.println(pl);*/
        return oldleaves;
    }

    public void createAddEmployeeForm() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEmployee.fxml"));
            Parent root = loader.load();
            AddEmployee employee=loader.getController();
            employee.setdashbord(this);
            Stage addEmpStage = new Stage();
            addEmpStage.initStyle(StageStyle.DECORATED);
            addEmpStage.setScene(new Scene(root, 638, 574));
            addEmpStage.setTitle("Add Employee Form");
            addEmpStage.showAndWait();
           // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}
