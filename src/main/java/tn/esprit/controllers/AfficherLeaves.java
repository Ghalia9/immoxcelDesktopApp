package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import tn.esprit.models.Leaves;


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
    public void setData(Leaves leave){
    name.setText("name");
    takenLeaves.setText("1/5");
    leaveReason.setText(leave.getLeaveType());
    description.setText(leave.getLeaveDescription());
    from.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartDate()));
    to.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getFinishDate()));
    }
    public void setDataALL(Leaves leave){
        if(leave.getStatus().equals("Female"))
            status.setImage(new Image("file:src/main/resources/images/female.png"));
        else
            status.setImage(new Image("file:src/main/resources/images/male.png"));
        employeeNameALL.setText("name");
        leavetypeALL.setText(leave.getLeaveType());
        fromALL.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartDate()));
        toALL.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getFinishDate()));
    }
}
