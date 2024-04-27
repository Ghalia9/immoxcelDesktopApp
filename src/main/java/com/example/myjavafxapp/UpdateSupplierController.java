package com.example.myjavafxapp;

import Entities.Supplier;
import Entities.Transaction;
import Services.ServiceSupplier;
import Utils.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UpdateSupplierController implements Initializable {
    @FXML
    private Button CancelButton;

    @FXML
    private Label LabelMessage;
    @FXML
    private Label idSupp;
    @FXML
    private TextField idSuppTextField;

    @FXML
    private TextField PhoneNumberTextFiled;

    @FXML
    private TextField PatentTextField;

    @FXML
    private TextField ProductTextField;

    @FXML
    private TextField addressTextFiled;
    @FXML
    private ComboBox<String> comboboxCountriesUpdate;

    @FXML
    private TextField prefixLabel;

    @FXML
    private TextField companyNameTextFiled;

    @FXML
    private Button saveButton;
    @FXML
    private javafx.scene.image.ImageView ImageView;
    @FXML
    private Pane pane_112;
    ServiceSupplier sp = new ServiceSupplier();
    private Image image;
    private Alert alert;
    String imagePath = "";
    private DisplayController displayController;
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            comboboxCountriesUpdate.getItems().addAll(countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supplierInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(pane_112.getScene().getWindow());
        if (file != null) {
            String imagePath = file.toURI().toString();
            image = new Image(imagePath);
            ImageView.setImage(image);
        }
    }
    public void setDisplayController(DisplayController controller){
        this.displayController=controller;
    }

    private void clearFields() {
        companyNameTextFiled.setText("");
        addressTextFiled.setText("");
        ProductTextField.setText("");
        PhoneNumberTextFiled.setText("");
        PatentTextField.setText("");
        ImageView.setImage(null); // Clear image
    }
    private void refreshDisplay() {
        if (displayController != null) {
            displayController.showAllSuppliers();
        }
    }

    public void EditOnClickButtonUpdate(ActionEvent event){
        System.out.println(" l id of update supplier function = "+idSuppTextField.getText());

        String imagePath = "";
        System.out.println("i am just after getting the data from the fields");
        if (image != null) {
            imagePath = image.getUrl();
        }

        if (companyNameTextFiled.getText().isEmpty() || addressTextFiled.getText().isEmpty() || ProductTextField.getText().isEmpty() || PhoneNumberTextFiled.getText().isEmpty() || PatentTextField.getText().isEmpty() || ImageView.getImage()==null) {
            displayErrorAlert("You need to fill blank field ");
        }else {
            if(!isNumeric(PhoneNumberTextFiled.getText())  ){
                displayErrorAlert("Requires numbers Check The Quantity and cost Field");
            }
            else {
                if (companyNameTextFiled.getText().length() < 3 || addressTextFiled.getText().length() < 3 || ProductTextField.getText().length() < 3|| PhoneNumberTextFiled.getText().length() < 8|| PatentTextField.getText().length() <8)
                {
                    displayErrorAlert("fields  [ company name | adress | Product ] requires more than 3  And [Phone Number | Patent ] Requires more than 8 ");
                }
                else {
                    int phone = Integer.parseInt(PhoneNumberTextFiled.getText());
                    // Convert CostTextField input to a float
                    int id = Integer.parseInt(idSuppTextField.getText());
                    try {
                        /*String check = "SELECT phone_number FROM supplier WHERE phone_number=?";
                        PreparedStatement statement = cnx.prepareStatement(check);
                        statement.setString(1, PhoneNumberTextFiled.getText());
                        ResultSet res = statement.executeQuery();
                        if (res.next()) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setContentText("Supplier :" + companyNameTextFiled.getText() + " was already created");
                            alert.showAndWait();
                        } else {*/
                        sp.modifier(new Supplier(id,companyNameTextFiled.getText(), addressTextFiled.getText(),ProductTextField.getText(),phone,PatentTextField.getText(),imagePath));
                        refreshDisplay();
                        displayConfirmationAlert("Modified Succfuly");
                        // Clear fields
                        clearFields();//}
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
    public void setFields(int id,String companyName,String address, String product , int c,String PatentRef ,String Image ){
        System.out.println("///////////////////////////////////////////////////////////");
        try {
            String idsupplierString = String.valueOf(id);
            System.out.println("////////////  THE ID = "+ idSuppTextField);

            String phonenumber = String.valueOf(c);
            idSuppTextField.setText(idsupplierString);
            companyNameTextFiled.setText(companyName);
            addressTextFiled.setText(address);
            ProductTextField.setText(product);
            PhoneNumberTextFiled.setText(phonenumber);
            PatentTextField.setText(PatentRef);
            try {
                String imagePath = Image;
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
                    ImageView.setImage(image);
                    System.out.println("the path of the picture is ");
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during setting text fields
            System.err.println("Error occurred while setting text fields: " + e.getMessage());
            e.printStackTrace();
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
    @FXML
    void setCancelButtonIDActionUpdate(ActionEvent event) {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    public void CountriesOnClick(ActionEvent event) {
        // Add an event handler to listen for changes in the selected item
        String selectedItem = comboboxCountriesUpdate.getValue();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\Alice\\IdeaProjects\\MyJavaFxApp\\src\\main\\resources\\CountryCodes.json")) {
            // Parse the JSON file
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            Map<List<String>, List<String>> map = new HashMap<>();
            // Extract country names from JSON and add them to a list
            List<String> countryNames = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String countryName = (String) jsonObject.get("name");
                String phoneNumberPrefix = (String) jsonObject.get("dial_code");
                countryNames.add(countryName);
                if (comboboxCountriesUpdate.getValue().equals(countryName)) {
                    System.out.println("Selected Item: " + selectedItem);
                    System.out.println("the phonenumber=" + phoneNumberPrefix);
                    prefixLabel.setText(phoneNumberPrefix);
                }
            }
            // Populate the ComboBox with country names
            comboboxCountriesUpdate.getItems().addAll(countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void displayConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
