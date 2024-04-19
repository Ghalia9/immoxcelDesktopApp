package tn.esprit.controllers;
import javafx.scene.layout.VBox;
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

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, proceed with delete operation
            se.supprimer(currentEmployee.getId());
            employeesLayout.getChildren().remove(itemEmployee);
        }
    }
    public Employees currentEmployee;
    HRDashboard dashboard;
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
}
