package com.example.myjavafxapp;

import Entities.Supplier;
import Entities.Transaction;
import Services.ServiceSupplier;
import Services.ServiceTransaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class SupplierContainerController {
    @FXML
    private Label Address;

    @FXML
    private Label companyName;

    @FXML
    private Label idSup;

    @FXML
    private ImageView imageViewRef;


    @FXML
    private Label patentRef;

    @FXML
    private Label phoneNumber;

    @FXML
    private HBox hbox;

    @FXML
    private Label productName;

    @FXML
    private Pane Pane112Container;

    private final ServiceSupplier sp = new ServiceSupplier();



    public void setData(Supplier supplier){
        if (supplier != null) {
            System.out.println("i am inside supliercontainer ");
            idSup.setText(String.valueOf(supplier.getId_supp()));
            companyName.setText(supplier.getCompany_name());
           // Address.setText(supplier.getAddress());
           //productName.setText(supplier.getMaterails_s());
            phoneNumber.setText(String.valueOf(supplier.getPhone_number()));
           // patentRef.setText(supplier.getPatent_ref());4
            System.out.println("i am by the end of the function data  in supplierconrttainer controller");
        } else {
            // Handle the case where supplier is null, e.g., display a default message or clear the UI elements.
            // For example:
            idSup.setText("N/A");
            companyName.setText("N/A");
            //Address.setText("N/A");
           // productName.setText("N/A");
            phoneNumber.setText("N/A");
            //patentRef.setText("N/A");
        }
        try {
            String imagePath = supplier.getImage();
            // Remove the "file:\" or "file:/" prefix if it exists
            if (imagePath.startsWith("file:\\")) {
                imagePath = imagePath.substring(6); // Removing "file:\" prefix
            } else if (imagePath.startsWith("file:/")) {
                imagePath = imagePath.substring(5); // Removing "file:/" prefix
            }
            File file = new File(imagePath);
            System.out.println("Image file path: " + file.getAbsolutePath()); // Debug output
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageViewRef.setImage(image);
            } else {
                System.err.println("Image file not found: " + file.getAbsolutePath()); // Error message
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void deleteOnClickButton(ActionEvent event ) {
        try {
            int  idSupplier= Integer.parseInt(idSup.getText());
            // Ask for confirmation before deleting
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Transaction");
            confirmationAlert.setHeaderText("Are you sure you want to delete this transaction?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Delete the transaction from the database
                        sp.supprimer(idSupplier);
                        // Remove the card from the UI
                        Pane112Container.getChildren().clear();
                        Pane112Container.setVisible(false); // Hide the card
                        Pane112Container.setManaged(false); // Make sure it's not managed by the layout
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
    public void EditOnClickButton(ActionEvent event){

        try {
            int  idSupplier= Integer.parseInt(idSup.getText());
            System.out.println("l'id de supplier "+idSupplier);
            // Ask for confirmation before deleting
            Alert confirmationAlert =  new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Update Supplier");
            confirmationAlert.setHeaderText("Are you sure you want to update this Supplier?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSupplier.fxml"));
                        Parent root = loader.load();
                        UpdateSupplierController updateSupplierController = loader.getController();
                        // Create a new stage for the popup window
                        Stage popupStage= new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);// Make the pop-up Window  model
                        popupStage.initStyle(StageStyle.UTILITY);
                        popupStage.setTitle("Update Supplier");
                        // set scene to the pop-up window
                        Scene scene = new Scene (root);
                        popupStage.setScene(scene);
                        Supplier supplier = sp.getOneById(idSupplier);
                        if (supplier != null) {
                            System.out.println("id transaction before "+idSupplier);
                            updateSupplierController.setFields(idSupplier,supplier.getCompany_name(), supplier.getAddress(), supplier.getMaterails_s(), supplier.getPhone_number(),supplier.getPatent_ref(),supplier.getImage());
                            System.out.println("transactionid : "+supplier.getId_supp());
                        } else {
                            System.out.println("Transaction with ID " + idSupplier + " not found.");
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

    // here is the details function where you will have more informations on the supplier
    @FXML
    void detailsOnclickbtn(ActionEvent event) {
        try {

            int  idSupplier= Integer.parseInt(idSup.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsSupplier.fxml"));
            Parent root = loader.load();
            UpdateSupplierController updateSupplierController = loader.getController();
            // Create a new stage for the popup window

            Stage popupStage= new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);// Make the pop-up Window  model
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Update Supplier");
            // set scene to the pop-up window
            Scene scene = new Scene (root);
            popupStage.setScene(scene);

            Supplier supplier = sp.getOneById(idSupplier);
            if (supplier != null) {
                System.out.println("id transaction before "+idSupplier);
                updateSupplierController.setFields(idSupplier,supplier.getCompany_name(), supplier.getAddress(), supplier.getMaterails_s(), supplier.getPhone_number(),supplier.getPatent_ref(),supplier.getImage());
                System.out.println("transactionid : "+supplier.getId_supp());
            } else {
                System.out.println("Transaction with ID " + idSupplier + " not found.");
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
}
