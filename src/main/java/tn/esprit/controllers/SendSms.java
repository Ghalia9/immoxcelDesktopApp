package tn.esprit.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
public class SendSms {

    @FXML
    private Button ClearButton;

    public static String url_str;

    @FXML
    private Button Editbtn;

    @FXML
    private Label LabelMessage;

    @FXML
    private TextField MessageTextField;

    @FXML
    private TextField PhoneNumberTextFiled;

    @FXML
    private Label idtrans;

    @FXML
    private Pane pane_112;


    @FXML
    void setCancelButtonIDAction(ActionEvent event) {
    }


}
