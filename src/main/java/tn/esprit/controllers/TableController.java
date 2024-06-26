package tn.esprit.controllers;

import tn.esprit.models.Transaction;
import tn.esprit.models.Supplier;
import tn.esprit.models.Capital;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.services.ServiceTransaction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class TableController {
    @FXML
    private Button Delete;

    @FXML
    private HBox Card;

    @FXML
    private Label Descrption;

    @FXML
    private Button Edit;

    @FXML
    private Label Quantity;

    @FXML
    private Label TotalAmount;

    @FXML
    private Label dateLabel;

    @FXML
    private Label id;

    @FXML
    private Label typeTextField;
    private final ServiceTransaction sp = new ServiceTransaction();

    private PdfGenerator pdfIns = new PdfGenerator();
    public void setData(Transaction transaction){

        System.out.println("the id is retrieved from cardController "+transaction.getId());
        id.setText(String.valueOf(transaction.getId()));
        dateLabel.setText(String.valueOf(transaction.getDate()));
        typeTextField.setText(transaction.getType());
        Quantity.setText(String.valueOf(transaction.getQuantity()));
        Descrption.setText(transaction.getDescription());
        TotalAmount.setText(String.valueOf(transaction.getTotalamount()));

    }

    public void EditOnClickButton(ActionEvent event){

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
                       /* Parent root = FXMLLoader.load(getClass().getResource("TransactionAdd.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.initStyle ( StageStyle.UTILITY);
                        stage.show();*/
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateTransaction.fxml"));
                        Parent root = loader.load();
                        UpdateTransactionController updateTransactionController = loader.getController();
                        // Create a new stage for the popup window
                        Stage popupStage= new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);// Make the pop-up Window  model
                        popupStage.initStyle(StageStyle.UTILITY);
                        popupStage.setTitle("Update Transaction");
                        // set scene to the pop-up window
                        Scene scene = new Scene (root);
                        popupStage.setScene(scene);

                        Transaction transaction = sp.getOneById(idTransaction);
                        if (transaction != null) {
                            System.out.println("id transaction before YOOOOOOOOOO "+idTransaction);
                            updateTransactionController.setFields(idTransaction,transaction.getType(), transaction.getQuantity(), transaction.getDescription(), transaction.getCost());
                            System.out.println("transactionid : "+transaction.getId());
                        } else {
                            System.out.println("Transaction with ID " + idTransaction + " not found.");
                        }
                        popupStage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("An error occurred while loading the Register form.");
                        alert.show();
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

    @FXML
    void ArchiveOnClick(ActionEvent event) {
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
                        Transaction transaction = sp.getOneById(idTransaction);
                        if (transaction != null) {
                            System.out.println("Transaction id found in Archive found  : "+transaction.getId());
                            sp.archiver(idTransaction);
                            Card.getChildren().clear();
                            Card.setVisible(false); // Hide the card
                            Card.setManaged(false); // Make sure it's not managed by the layout
                        } else {
                            System.out.println("Transaction with ID " + idTransaction + " not found.");
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
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    } private void displayConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PDF Confirmation ✅ ");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void GeneratePDF() throws FileNotFoundException, MalformedURLException {

            int idTransaction = Integer.parseInt(id.getText());

            //pdfIns.GeneratePDFTransaction(idTransaction);
            displayConfirmation("Check Your Folder");


    }
}
