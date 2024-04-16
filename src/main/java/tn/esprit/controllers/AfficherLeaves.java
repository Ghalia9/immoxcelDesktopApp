package tn.esprit.controllers;

import javafx.fxml.FXML;
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

    public void setData(Leaves leave){
    name.setText("name");
    takenLeaves.setText("1/5");
    leaveReason.setText(leave.getLeaveType());
    description.setText(leave.getLeaveDescription());
    from.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getStartDate()));
    to.setText(new SimpleDateFormat("yyyy-MM-dd").format(leave.getFinishDate()));

    }
}
