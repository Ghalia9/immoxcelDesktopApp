package tn.esprit.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Transaction;
import tn.esprit.models.Supplier;
import tn.esprit.models.Capital;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import tn.esprit.services.ServiceTransaction;

import java.io.IOException;
import java.sql.SQLException;

public class CardController {
    @FXML
    private Label Descrption;

    @FXML
    private Label Quantity;

    @FXML
    private Label TotalAmount;

    @FXML
    private Label id;
    @FXML
    private Label dateLabel;


    @FXML
    private Label typeTextField;

    @FXML
    private HBox box;
    private final ServiceTransaction sp = new ServiceTransaction();
    //private String [] colors  = {B9E5FF, "BDB2FE", "FB9AA8", "FF5056"};



    public void setData(Transaction transaction){

        System.out.println("the id is retrieved from cardController "+transaction.getId());
        id.setText(String.valueOf(transaction.getId()));
        dateLabel.setText(String.valueOf(transaction.getDate()));
        typeTextField.setText(transaction.getType());
        Quantity.setText(String.valueOf(transaction.getQuantity()));
        Descrption.setText(transaction.getDescription());
        TotalAmount.setText(String.valueOf(transaction.getCost()));
//        box.setStyle("-fx-background-color: #"+ colors[(int)(Math.random()* colors.length)] +";" +
//                "	-fx-background-radius:15;" +
//                "    -fx-effect: dropShadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 10);" );

    }
    public void deleteOnClickButton(ActionEvent event ) {

        try {
            int  idTrans= Integer.parseInt(id.getText());
            // Ask for confirmation before deleting
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Transaction");
            confirmationAlert.setHeaderText("Are you sure you want to delete this transaction?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("l'id egale a == du controlleur cardController "+ idTrans);
                    // try {
                    // smssendcode.sendsmsCodeVerification();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CodeSms.fxml"));
                        Parent root = loader.load();
                        CodeSmsController codeSmsController = loader.getController();
                        // Create a new stage for the popup window
                        Stage popupStage= new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);// Make the pop-up Window  model
                        popupStage.initStyle(StageStyle.UTILITY);
                        popupStage.setTitle("Update Transaction");
                        // set scene to the pop-up window
                        Scene scene = new Scene (root);
                        popupStage.setScene(scene);
                        codeSmsController.setFields(idTrans);
                        popupStage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                        displayErrorAlert("An error occurred while loading the Register form.");
                    }

                }
            });
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("An unexpected error occurred: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void showPopUpforCode(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void codePopUpValidator() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/CodeSms.fxml"));
            showPopUpforCode(root);
        } catch (IOException e) {
            displayErrorAlert("Error loading CodeSms.fxml");
        }
    }
    private void confirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
