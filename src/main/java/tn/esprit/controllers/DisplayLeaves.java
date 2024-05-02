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
public class DisplayLeaves implements Initializable{
    @FXML
    private HBox cardLayout;
    @FXML
    private VBox leavesLayout;
    private List<Leaves> pendingLeaves,oldleaves;

    public HBox getCardLayout() {
        return cardLayout;
    }

    public VBox getLeavesLayout() {
        return leavesLayout;
    }

    private final ServiceLeaves sl= new ServiceLeaves();
    private final ServiceEmployees se = new ServiceEmployees();
    private DisplayLeaves dl;

    public void setDl(DisplayLeaves dl) {
        this.dl = dl;
    }


    private int idSelectedEmployee(Employees employee){
        return employee.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //pendingLeaves=new ArrayList<>(pendingLeaves());
        //PENDING LEAVES
        showPendingLeaves();
        //ALLLEAVES

        showOldLeaves();
    }
    public void showPendingLeaves(){
        pendingLeaves=new ArrayList<>(pendingLeaves());
        //PENDING LEAVES
        try{
            for (int i=0;i<pendingLeaves.size();i++){
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/CardLeaves.fxml"));
                HBox cardBox=loader.load();
                AfficherLeaves afficherLeaves=loader.getController();
                afficherLeaves.setDl(this);
                afficherLeaves.setCurrentLeave(pendingLeaves.get(i));
                afficherLeaves.setData(pendingLeaves.get(i));
                afficherLeaves.setCurrentEmployee(pendingLeaves.get(i).getEmployee());
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
    @FXML
    void navigateToEmployees(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayEmployees.fxml"));
            Parent root = loader.load();
            //DisplayEmployees employees=loader.getController();
            //employees.setDE(this);
            Stage stage = (Stage) cardLayout.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));
            DisplayEmployees employees=loader.getController();
           // employees.showEmployeesList();
/*
            addEmpStage.initStyle(StageStyle.DECORATED);
            addEmpStage.setScene(new Scene(root, 638, 574));
            addEmpStage.setTitle("Add Employee Form");
            addEmpStage.showAndWait();

 */
            // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    @FXML
    void toEmailOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HRMailer.fxml"));
            Parent root = loader.load();
            //Stage stage = (Stage) employeesLayout.getScene().getWindow(); // Get the current stage
            //stage.setScene(new Scene(root));
            Mail mail=loader.getController();
            Stage mailstage = new Stage();
            mailstage.initStyle(StageStyle.DECORATED);
            mailstage.setScene(new Scene(root, 498, 400));
            mailstage.setTitle("Add Employee Form");
            mailstage.showAndWait();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}
