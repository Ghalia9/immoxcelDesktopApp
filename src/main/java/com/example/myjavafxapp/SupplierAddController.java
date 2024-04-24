package com.example.myjavafxapp;

import Entities.Supplier;
import Entities.Transaction;
import Services.ServiceSupplier;
import Services.ServiceTransaction;
import Utils.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SupplierAddController  implements Initializable {

    @FXML
    private Button CancelButton;

    @FXML
    private Label LabelMessage;

    @FXML
    private TextField PhoneNumberTextFiled;

    @FXML
    private TextField PatentTextField;

    @FXML
    private TextField ProductTextField;

    @FXML
    private TextField addressTextFiled;

    @FXML
    private TextField companyNameTextFiled;

    @FXML
    private Button saveButton;
    @FXML
    private ImageView ImageView;
    @FXML
    private Pane pane_AddSupplier;

    @FXML
    private TextField prefixLabel;


    @FXML
    private ComboBox<String> comboboxCountries;
    ServiceSupplier sup = new ServiceSupplier();
    private Image image;

    private Alert alert;

    Connection cnx = DataSource.getInstance().getCnx();

    private  DisplayController displayController;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Read the JSON file
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\Alice\\IdeaProjects\\MyJavaFxApp\\src\\main\\resources\\CountryCodes.json")) {
            // Parse the JSON file
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;

            // Extract country names from JSON and add them to a list
            List<String> countryNames = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String countryName = (String) jsonObject.get("name");
                String phoneNumberPrefix=(String) jsonObject.get("dial_code");
                countryNames.add(countryName);
            }
            System.out.println("Country Names: " + countryNames);

            // Populate the ComboBox with country names
            comboboxCountries.getItems().addAll(countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void setDisplayController(DisplayController controller){
        this.displayController=controller;
    }
    private void refreshDisplay() {
        if (displayController != null) {
            displayController.refrechData();
        }
    }


    public void supplierInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(pane_AddSupplier.getScene().getWindow());
        if (file != null) {
            String imagePath = file.toURI().toString();
            image = new Image(imagePath);
            ImageView.setImage(image);
        }
    }

    private boolean isNumeric (String str ){
        try {
            double d = Double.parseDouble(str);

        }catch (NumberFormatException | NullPointerException e ){
            return false ;
        }
        return true;
    }
    public void saveSupplierButtonOnAction(ActionEvent event) {

        System.out.println("hell i am inside the addSupplier function");
        //String companyName = companyNameTextFiled.getText();
        //String address = addressTextFiled.getText();
        //String Products = ProductTextField.getText();
        //String patentRef = PatentTextField.getText();
       // int phone = Integer.parseInt(PhoneNumberTextFiled.getText());
        String imagePath = "";
        System.out.println("i am just after getting the data from the fields");
        if (image != null) {
            imagePath = image.getUrl();
        }
        if (companyNameTextFiled.getText().isEmpty() || addressTextFiled.getText().isEmpty() || ProductTextField.getText().isEmpty() || PhoneNumberTextFiled.getText().isEmpty() || PatentTextField.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("You need to fill blank field ");
            alert.showAndWait();
        }
        else {
            if (!isNumeric(PhoneNumberTextFiled.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Requires numbers" + "Check The Quantity and cost Field ");
                alert.showAndWait();
            } else {
                if (companyNameTextFiled.getText().length() < 3 || addressTextFiled.getText().length() < 3 || ProductTextField.getText().length() < 3 || PhoneNumberTextFiled.getText().length() < 8 || PatentTextField.getText().length() < 8) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setContentText("fields requires more than 3 caracteres ");
                    alert.showAndWait();
                } else {
                    try {
                        String companyName = companyNameTextFiled.getText();
                        String address = addressTextFiled.getText();
                        String Products = ProductTextField.getText();
                        String patentRef = PatentTextField.getText();
                        //if(comboboxCountries.getValue().equals()


                         int phone = Integer.parseInt(PhoneNumberTextFiled.getText());
                        String check = "SELECT phone_number FROM supplier WHERE phone_number=?";
                        PreparedStatement statement = cnx.prepareStatement(check);
                        statement.setString(1, PhoneNumberTextFiled.getText());
                        ResultSet res = statement.executeQuery();
                        if (res.next()) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setContentText("Supplier :" + companyNameTextFiled.getText() + " was already created");
                            alert.showAndWait();
                        } else {
                            sup.ajouter(new Supplier(companyName, address, Products, phone, patentRef, imagePath));
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.show();
                            refreshDisplay();
                        }
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("SQL Exception");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    @FXML
    private void setCancelButtonIDAction(ActionEvent event) {
        Stage stage = (Stage)  CancelButton.getScene().getWindow();
        stage.close();
    }


    public void CountriesOnClick(ActionEvent event){
        // Add an event handler to listen for changes in the selected item
            String selectedItem = comboboxCountries.getValue();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\Alice\\IdeaProjects\\MyJavaFxApp\\src\\main\\resources\\CountryCodes.json")) {
            // Parse the JSON file
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            Map< List<String>,List<String> > map = new HashMap<>();



            // Extract country names from JSON and add them to a list
            List<String> countryNames = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String countryName = (String) jsonObject.get("name");
                String phoneNumberPrefix=(String) jsonObject.get("dial_code");

                countryNames.add(countryName);


                if(comboboxCountries.getValue().equals(countryName)){

                    System.out.println("Selected Item: " + selectedItem);
                    System.out.println("the phonenumber=" +phoneNumberPrefix);
                    prefixLabel.setText(phoneNumberPrefix);

                }
            }
            // Populate the ComboBox with country names
            comboboxCountries.getItems().addAll(countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
