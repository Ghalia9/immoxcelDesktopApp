package tn.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;
import tn.esprit.controllers.AfficherEmployees;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
public class DisplayEmployees implements Initializable {

    @FXML
    private VBox employeesLayout;

    public VBox getEmployeesLayout() {
        return employeesLayout;
    }

    private final ServiceLeaves sl= new ServiceLeaves();
    private final ServiceEmployees se = new ServiceEmployees();
    private DisplayEmployees de;

    public void setDE(DisplayEmployees de) {
        this.de = de;
    }
    private int idSelectedEmployee(Employees employee){
        return employee.getId();
    }
    //button add on top of list of employees
    @FXML
    void addOnClick(ActionEvent event) throws IOException {
        createAddEmployeeForm();
        //showEmployeesList();

    }

    @FXML
    private WebView webView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                afficheremployees.setDE(this);
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
    public void createAddEmployeeForm() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEmployee.fxml"));
            Parent root = loader.load();
            AddEmployee employee=loader.getController();
            employee.setDE(this);
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
    @FXML
    void navigateToLeaves(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayLeaves.fxml"));
            Parent root = loader.load();
            DisplayLeaves leaves=loader.getController();
            //employees.setDE(this);
            Stage stage = (Stage) employeesLayout.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));
            //DisplayLeaves leaves=loader.getController();
            //leaves.showPendingLeaves();
            //leaves.showOldLeaves();
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
    @FXML
    void AIOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AiAssistant.fxml"));
            AnchorPane root = loader.load();

            // Get the controller
            AiAssistant controller = loader.getController();
            controller.loadChatbot();

            // Create a scene and set it on the stage
            Scene scene = new Scene(root, 454, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Chatbot");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
