package tn.esprit.controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.controllers.HRDashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import tn.esprit.models.Employees;
import tn.esprit.services.ServiceEmployees;

import java.util.Optional;

public class AfficherEmployees {
    @FXML
    private Label employeefunction;

    @FXML
    private Label employeename;

    @FXML
    private Label employeephone;

    @FXML
    private ImageView img;
    @FXML
    private HBox itemEmployee;

    @FXML
    void delete_employee(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Employee");
        alert.setContentText("Are you sure you want to delete this employee?");
        alert.getDialogPane().getStyleClass().add("delete-confirmation-dialog");
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

       // Optional<ButtonType> result = alert.showAndWait();
       // if (result.isPresent() && result.get() == ButtonType.OK) {
        if (result == ButtonType.OK) {

            // User clicked OK, proceed with delete operation
            se.supprimer(currentEmployee.getId());
            employeesLayout.getChildren().remove(itemEmployee);
            /*
            dashboard.getCardLayout().getChildren().clear();
            dashboard.showPendingLeaves();
            dashboard.getLeavesLayout().getChildren().clear();
            dashboard.showOldLeaves();
             */
        }
    }
    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }

    HRDashboard dashboard;
    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }
    private DisplayEmployees de;

    public void setDE(DisplayEmployees de) {
        this.de = de;
    }

    public VBox employeesLayout;
    private final ServiceEmployees se = new ServiceEmployees();

    public void setData(Employees employee){
        if(employee.getEmpSex().equals("Female"))
            img.setImage(new Image("file:src/main/resources/images/female.png"));
        else
            img.setImage(new Image("file:src/main/resources/images/male.png"));
        employeename.setText(employee.getEmpLastName()+ " " +employee.getEmpName());
        employeefunction.setText(employee.getEmpFunction());
        employeephone.setText(employee.getEmpPhone());
    }
    public int idSelected(Employees employee){
        return employee.getId();
    }
    @FXML
    void details_employee(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEmployee.fxml"));
            Parent Details = loader.load();
            DetailsEmployee employeedetails=loader.getController();
            employeedetails.setDE(de);
            //employeedetails.setdashbord(dashboard);
            Stage detailsEmpStage = new Stage();
            detailsEmpStage.initStyle(StageStyle.DECORATED);
            detailsEmpStage.setScene(new Scene(Details, 638, 574));
            detailsEmpStage.setTitle("Employee Details");
            employeedetails.setDetailsData(currentEmployee);
            employeedetails.setCurrentEmployee(currentEmployee);
            detailsEmpStage.showAndWait();
            // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    @FXML
    void edit_employee(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditEmployee.fxml"));
            Parent Edit = loader.load();
            EditEmployee employeeedit=loader.getController();
            if (dashboard==null)
                System.out.println("hneeeeeeeeeeeeeeeeeeeeeeee");
            employeeedit.setDE(de);
            //employeeedit.setdashbord(dashboard);
            Stage editEmpStage = new Stage();
            editEmpStage.initStyle(StageStyle.DECORATED);
            editEmpStage.setScene(new Scene(Edit, 638, 574));
            editEmpStage.setTitle("Edit Employee");
            employeeedit.setDetailsData(currentEmployee);
            employeeedit.setCurrentEmployee(currentEmployee);
            System.out.println(currentEmployee);
            editEmpStage.showAndWait();
            // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    @FXML
    void emailEmployeeOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HRMailer.fxml"));
            Parent root = loader.load();
            //Stage stage = (Stage) employeesLayout.getScene().getWindow(); // Get the current stage
            //stage.setScene(new Scene(root));
            Mail mail=loader.getController();
            System.out.println(currentEmployee.getEmpEmail());
            mail.setTo(currentEmployee.getEmpEmail());
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
