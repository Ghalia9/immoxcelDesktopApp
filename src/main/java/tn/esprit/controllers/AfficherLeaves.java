package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;


import java.text.SimpleDateFormat;

public class AfficherLeaves {
    @FXML
    private HBox box;

    @FXML
    private Label description;

    @FXML
    private Label from;

    @FXML
    private Label leaveReason;

    @FXML
    private Label name;

    @FXML
    private Label takenLeaves;

    @FXML
    private Label to;

    //ALL LEAVES
    @FXML
    private Label employeeNameALL;

    @FXML
    private Label fromALL;

    @FXML
    private Label leavetypeALL;

    @FXML
    private ImageView status;

    @FXML
    private Label toALL;
    private Employees employee;
    public void setData(Leaves leave){
    name.setText(leave.getEmployee().getEmpLastName()+" "+leave.getEmployee().getEmpName());
    takenLeaves.setText(leave.getEmployee().getEmpTakenLeaves()+"/"+leave.getEmployee().getAllowedLeaveDays());
    leaveReason.setText(leave.getLeaveType());
    description.setText(leave.getLeaveDescription());
    from.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartDate()));
    to.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getFinishDate()));
    }
    public void setDataALL(Leaves leave){
        if(leave.getStatus().equals("Approved"))
            status.setImage(new Image("file:src/main/resources/images/Accept.png"));
        else if (leave.getStatus().equals("Disapproved"))
            status.setImage(new Image("file:src/main/resources/images/refuse.png"));
        else
            status.setImage(new Image("file:src/main/resources/images/pending.png"));
        employeeNameALL.setText(leave.getEmployee().getEmpLastName()+" "+leave.getEmployee().getEmpName());
        leavetypeALL.setText(leave.getLeaveType());
        fromALL.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartDate()));
        toALL.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getFinishDate()));
    }
    private final ServiceLeaves sl= new ServiceLeaves();
    private Leaves currentLeave;

    public void setCurrentLeave(Leaves currentLeave)
    {

        this.currentLeave=currentLeave;
    }
    private HRDashboard dashboard;

    private DisplayLeaves dl;

    public void setDl(DisplayLeaves dl) {
        this.dl = dl;
    }

    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }
    private final ServiceEmployees se = new ServiceEmployees();

    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }

    @FXML
    void approveLeave(ActionEvent event) {
        Leaves l=sl.getOneById(currentLeave.getId());
        l.setStatus("Approved");
        sl.modifier(l);
        System.out.println("ok");
        System.out.println(currentEmployee.getEmpTakenLeaves());
        currentEmployee.setEmpTakenLeaves(currentEmployee.getEmpTakenLeaves()+1);
        System.out.println(currentEmployee.getEmpTakenLeaves());
        se.modifier(currentEmployee);
        /*OLD WORKKK
        dashboard.getCardLayout().getChildren().clear();
        dashboard.getLeavesLayout().getChildren().clear();
        dashboard.getEmployeesLayout().getChildren().clear();
        dashboard.showOldLeaves();
        dashboard.showPendingLeaves();
        dashboard.showEmployeesList();
        */
        dl.getCardLayout().getChildren().clear();
        dl.getLeavesLayout().getChildren().clear();
        dl.showOldLeaves();
        dl.showPendingLeaves();

    }

    @FXML
    void disapproveLeave(ActionEvent event) {
        Leaves l=sl.getOneById(currentLeave.getId());
        l.setStatus("Disapproved");
        sl.modifier(l);
        System.out.println(l);
        System.out.println("not ok");
        /*old work
        dashboard.getCardLayout().getChildren().clear();
        dashboard.getLeavesLayout().getChildren().clear();
        dashboard.showOldLeaves();
        dashboard.showPendingLeaves();
*/
        dl.getCardLayout().getChildren().clear();
        dl.getLeavesLayout().getChildren().clear();
        dl.showOldLeaves();
        dl.showPendingLeaves();

    }

}
