package com.example.myjavafxapp;

import Entities.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class SupplierDetailsController {


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
    private Label productName;
    public void setData(Supplier supplier){
        if (supplier != null) {
            System.out.println("i am inside supliercontainer ");
            idSup.setText(String.valueOf(supplier.getId_supp()));
            companyName.setText(supplier.getCompany_name());
             Address.setText(supplier.getAddress());
            productName.setText(supplier.getMaterails_s());
            phoneNumber.setText(String.valueOf(supplier.getPhone_number()));
             patentRef.setText(supplier.getPatent_ref());
            System.out.println("i am by the end of the function data  in supplierconrttainer controller");
        } else {
            // Handle the case where supplier is null, e.g., display a default message or clear the UI elements.
            // For example:
            idSup.setText("N/A");
            companyName.setText("N/A");
            Address.setText("N/A");
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
}
