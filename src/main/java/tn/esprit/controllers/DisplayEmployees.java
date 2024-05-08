package tn.esprit.controllers;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;
import tn.esprit.controllers.AfficherEmployees;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
    private TextField search_field;

    @FXML
    void SearchOnClick(ActionEvent event) {
        // Add listener to the search field text property for real-time updating
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the list of employees dynamically
            updateEmployeesList(newValue);
        });
    }
    @FXML
    void addOnClick(ActionEvent event) throws IOException {
        createAddEmployeeForm();
        //showEmployeesList();
    }
    @FXML
    void sortOnClick(ActionEvent event) {
// Get all employees and sort them by hire date
        employeesLayout.getChildren().clear();
        se.getAll().stream()
                .sorted(Comparator.comparing(Employees::getHireDate))
                .forEach(employee -> {
                    System.out.println(employee.getHireDate());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/EmployeeAfficher.fxml"));
                    HBox hBox = null;
                    try {
                        hBox = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AfficherEmployees afficherEmployees = loader.getController();
                    afficherEmployees.setDE(this);
                    afficherEmployees.setCurrentEmployee(employee);
                    afficherEmployees.setData(employee);
                    afficherEmployees.employeesLayout = employeesLayout;
                    employeesLayout.getChildren().add(hBox);
                });
    }

    @FXML
    private WebView webView;
    // List to hold all employees
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //EMPLOYEESLIST
        showEmployeesList();
        // Add listener to the search field text property for real-time updating
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the list of employees dynamically
            updateEmployeesList(newValue);
        });
    }
    private List<Employees> allEmployees=se.getAll().stream().toList();

    // Method to update the list of employees based on the search query
    private void updateEmployeesList(String searchQuery) {
        //noEmployeesLabel.setVisible(false);

        List<Employees> filteredEmployees = allEmployees.stream()
                .filter(employee -> matchesSearchQuery(employee, searchQuery))
                .collect(Collectors.toList());

        // Clear existing UI
        employeesLayout.getChildren().clear();

        // Display filtered employees or show "No employees found" label
        if (filteredEmployees.isEmpty()) {
            showNoEmployeesAlert();
        } else {
            try {
                for (Employees employee : filteredEmployees) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/EmployeeAfficher.fxml"));
                    HBox hBox = loader.load();
                    AfficherEmployees afficherEmployees = loader.getController();
                    afficherEmployees.setDE(this);
                    afficherEmployees.setCurrentEmployee(employee);
                    afficherEmployees.setData(employee);
                    afficherEmployees.employeesLayout = employeesLayout;
                    employeesLayout.getChildren().add(hBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Method to check if an employee matches the search query
    private boolean matchesSearchQuery(Employees employee, String searchQuery) {
        // You can customize this method based on your search requirements
        // For example, you can check if the search query matches the employee's first name, last name, or function
        // For simplicity, this example checks if the employee's full name contains the search query
        String fullName = employee.getEmpName() + " " + employee.getEmpLastName();
        String fuction= employee.getEmpFunction();
        return fullName.toLowerCase().contains(searchQuery.toLowerCase()) ||fuction.toLowerCase().contains(searchQuery.toLowerCase());
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
    // Method to display alert when no employees are found
    private void showNoEmployeesAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Employees Found");
        alert.setHeaderText(null);
        alert.setContentText("No employee matches the search criteria.");
        alert.show();
        // Close the alert after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            alert.close();
            showEmployeesList(); // Call showEmployeesList() after 3 seconds
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
