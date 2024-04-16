package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import tn.esprit.models.Employees;

public class AfficherEmployees {
    @FXML
    private Label employeefunction;

    @FXML
    private Label employeename;

    @FXML
    private Label employeephone;

    @FXML
    private ImageView img;

    public void setData(Employees employee){
        if(employee.getEmpSex().equals("Female"))
            img.setImage(new Image("file:src/main/resources/images/female.png"));
        else
            img.setImage(new Image("file:src/main/resources/images/male.png"));
        employeename.setText(employee.getEmpLastName()+ " " +employee.getEmpName());
        employeefunction.setText(employee.getEmpFunction());
        employeephone.setText(employee.getEmpPhone());
    }
}
