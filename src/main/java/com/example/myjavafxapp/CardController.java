package com.example.myjavafxapp;

import Entities.Transaction;
import Services.ServiceTransaction;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

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
    private Label typeTextField;

    @FXML
    private HBox box;
    private final ServiceTransaction sp = new ServiceTransaction();
    private String [] colors  = {"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};

    public void setData(Transaction transaction){

        System.out.println("the id is retrieved from cardController "+transaction.getId());
        id.setText(String.valueOf(transaction.getId()));
        typeTextField.setText(transaction.getType());
        Quantity.setText(String.valueOf(transaction.getQuantity()));
        Descrption.setText(transaction.getDescription());
        TotalAmount.setText(String.valueOf(transaction.getCost()));
        box.setStyle("-fx-background-color: #"+ colors[(int)(Math.random()* colors.length)] +";" +
                "	-fx-background-radius:15;" +
                "    -fx-effect: dropShadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 10);" );

    }
    public void deleteOnClickButton(ActionEvent event ) {
        try {
            int  idTransaction= Integer.parseInt(id.getText());
            // Ask for confirmation before deleting
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Transaction");
            confirmationAlert.setHeaderText("Are you sure you want to delete this transaction?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Delete the transaction from the database
                        sp.supprimer(idTransaction);
                        // Remove the card from the UI
                        box.getChildren().clear();
                        box.setVisible(false); // Hide the card
                        box.setManaged(false); // Make sure it's not managed by the layout
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setContentText("Transaction deleted successfully!");
                        successAlert.showAndWait();
                    } catch (SQLException e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("SQL Exception");
                        errorAlert.setContentText("Error while deleting transaction: " + e.getMessage());
                        errorAlert.showAndWait();
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

    public void EditOnClickButton(ActionEvent event){/*

        try {
             int  idTransaction= Integer.parseInt(id.getText());
            System.out.println("l'id de transaction que vous voulez faire une transaction "+idTransaction);
            // Ask for confirmation before deleting
            Alert confirmationAlert =  new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Update Transaction");
            confirmationAlert.setHeaderText("Are you sure you want to update this transaction?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
                        Parent root = loader.load();
                        RegisterController registerController = loader.getController();
                        id.getScene().setRoot(root);

                        Transaction transaction = sp.getOneById(idTransaction);
                        if (transaction != null) {

                            System.out.println("id transaction before YOOOOOOOOOO "+idTransaction);

                            registerController.setFields(idTransaction,transaction.getType(), transaction.getQuantity(), transaction.getDescription(), transaction.getCost());
                            System.out.println("transactionid : "+transaction.getId());
                        } else {
                            System.out.println("Transaction with ID " + idTransaction + " not found.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("An error occurred while loading the Register form.");
                        alert.show();
                    }
                 /*  try {
                        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
                        id.getScene().setRoot(root);
                        sp.getOneById(idTransaction);
                        System.out.println("Hello WORLD ");
                        Transaction transaction =  sp.getOneById(idTransaction);
                        RegisterController registerController = loader.getController();
                       System.out.println("l'id apres "+idTransaction);
                        registerController.setFields(sp.getOneById(idTransaction).getType(),sp.getOneById(idTransaction).getQuantity(),sp.getOneById(idTransaction).getDescription(),sp.getOneById(idTransaction).getCost());
                        System.out.println("type "+ transaction.getType());
                        System.out.println("quantity "+ transaction.getQuantity());
                        System.out.println("Description "+ transaction.getDescription());
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Sorry");
                        alert.setTitle("Error");
                        alert.show();
                    }

                }
            });
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("An unexpected error occurred: " + e.getMessage());
            errorAlert.showAndWait();
        }*/
    }
}
