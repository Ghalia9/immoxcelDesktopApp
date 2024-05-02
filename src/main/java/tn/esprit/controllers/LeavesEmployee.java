package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceLeaves;

import java.text.SimpleDateFormat;

public class LeavesEmployee {


    @FXML
    private Label description;

    @FXML
    private Label fromALL;

    @FXML
    private Label leavetypeALL;

    @FXML
    private ImageView status;

    @FXML
    private Label toALL;
    @FXML
    private HBox itemLeave;

    @FXML
    private ImageView delete;

    @FXML
    private ImageView edit;
    private final ServiceLeaves sl= new ServiceLeaves();
    private Leaves currentLeave;

    public void setCurrentLeave(Leaves currentLeave)
    {

        this.currentLeave=currentLeave;
    }
    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }
    private DisplayEmployees de;

    public void setDE(DisplayEmployees de) {
        this.de = de;
    }
    private DisplayLeaves dl;

    public void setDl(DisplayLeaves dl) {
        this.dl = dl;
    }


    @FXML
    void deleteLeaveOnClick(ActionEvent event) {
        if (currentLeave.getStatus().equals("Pending")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Pending Leave");
            alert.setContentText("Are you sure you want to delete this employee? Once it's deleted the HR managment won't see it!");
            alert.getDialogPane().getStyleClass().add("delete-confirmation-dialog");
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            // Optional<ButtonType> result = alert.showAndWait();
            // if (result.isPresent() && result.get() == ButtonType.OK) {
            if (result == ButtonType.OK) {

                // User clicked OK, proceed with delete operation
                sl.supprimer(currentLeave.getId());
                display.getChildren().remove(itemLeave);
                //dl.getCardLayout().getChildren().remove(itemLeave);
            }
        }else {
            // Display a message saying only pending leaves can be deleted
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Only pending leaves can be deleted.");
            alert.showAndWait();
        }
    }

    @FXML
    void editLeaveOnClick(ActionEvent event) {
        if (currentLeave.getStatus().equals("Pending")) {

            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditLeave.fxml"));
            Parent EditLeave = loader.load();
            EditLeave leaveedit=loader.getController();
            if (dashboard==null)
                System.out.println("hneeeeeeeeeeeeeeeeeeeeeeee");
            leaveedit.setDl(dl);
            //leaveedit.setdashbord(dashboard);
            Stage editLeaveStage = new Stage();
            editLeaveStage.initStyle(StageStyle.DECORATED);
            editLeaveStage.setScene(new Scene(EditLeave, 337, 405));
            editLeaveStage.setTitle("Edit Leave");
            leaveedit.setCurrentLeave(currentLeave);
            leaveedit.setDetailsData(currentLeave);
            leaveedit.setCurrentEmployee(currentEmployee);
            leaveedit.setDisplay(display);
            System.out.println(currentEmployee);
            editLeaveStage.showAndWait();
            // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }}else {
            // Display a message saying only pending leaves can be deleted
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Only pending leaves can be edited.");
            alert.showAndWait();
        }
    }

    public void setDataALL(Leaves leave){
            if (leave.getStatus().equals("Approved")){
                status.setImage(new Image("file:src/main/resources/images/Accept.png"));
                delete.setImage(new Image("file:src/main/resources/images/delete.png"));
                edit.setImage(new Image("file:src/main/resources/images/edit.png"));
            }
            else if (leave.getStatus().equals("Disapproved"))

            {
                status.setImage(new Image("file:src/main/resources/images/refuse.png"));
                delete.setImage(new Image("file:src/main/resources/images/delete.png"));
                edit.setImage(new Image("file:src/main/resources/images/edit.png"));
            }
            else {
                status.setImage(new Image("file:src/main/resources/images/pending.png"));
                delete.setImage(new Image("file:src/main/resources/images/delete.png"));
                edit.setImage(new Image("file:src/main/resources/images/edit.png"));
            }
            description.setText(leave.getLeaveDescription());
            //employeeNameALL.setText(leave.getEmployee().getEmpLastName()+" "+leave.getEmployee().getEmpName());
            leavetypeALL.setText(leave.getLeaveType());
            fromALL.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartDate()));
            toALL.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getFinishDate()));

    }

    private HRDashboard dashboard;

    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }
    private VBox display;

    public void setDisplay(VBox display)
    {
        this.display=display;
    }


}
