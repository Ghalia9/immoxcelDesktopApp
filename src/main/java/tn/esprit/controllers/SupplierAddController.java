package tn.esprit.controllers;

import tn.esprit.models.Transaction;
import tn.esprit.models.Supplier;
import tn.esprit.models.Capital;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tn.esprit.services.ServiceSupplier;
import tn.esprit.utils.DataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private ComparingImage compareim ;

    private Image image;

    private Alert alert;

    Connection cnx = DataSource.getInstance().getCnx();

    private DisplayController displayController;

    private PdfGenerator pdfSupplier;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        compareim = new ComparingImage();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\Alice\\IdeaProjects\\MyJavaFxApp\\src\\main\\resources\\CountryCodes.json")) {
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;

            List<String> countryNames = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String countryName = (String) jsonObject.get("name");
                String phoneNumberPrefix=(String) jsonObject.get("dial_code");
                countryNames.add(countryName);
            }
            System.out.println("Country Names: " + countryNames);

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
            float d = Float.parseFloat(str);
        }catch (NumberFormatException | NullPointerException e ){
            return false ;
        }
        return true;
    }
    public void saveSupplierButtonOnAction(ActionEvent event) throws IOException {

        String imagePath = "";
        if (image != null) {
            imagePath = image.getUrl();
            double percentage =compareim.compare( imagePath );
            if(percentage <20){
                System.out.println("it is inserted ");
            }else {
                displayErrorAlert("The Image isn't a document ");
            }
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
                if (companyNameTextFiled.getText().length() < 3 || addressTextFiled.getText().length() < 3 || ProductTextField.getText().length() < 3 ) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setContentText("fields requires more than 3 caracteres ");
                    alert.showAndWait();
                } else if ( PhoneNumberTextFiled.getText().length() < 8 || PatentTextField.getText().length() < 8) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setContentText("fields requires more than 8 caracteres ");
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
                            alert.setContentText("This Phone Number :" + PhoneNumberTextFiled.getText() + " Already Exist");
                            alert.showAndWait();
                        } else {
                           // pdfSupplier.GeneratePDFSupplier(companyName,address,prefixLabel.getText(),PhoneNumberTextFiled.getText(),PatentTextField.getText());
                            sup.ajouter(new Supplier(companyName, address, Products, phone, patentRef, imagePath));
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setContentText("Add Successfully ✅");
                            alert.show();
                            Stage stage = (Stage) companyNameTextFiled.getScene().getWindow();
                            stage.close();                        }
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


    public void CountriesOnClick(ActionEvent event) {
        // Add an event handler to listen for changes in the selected item
        String selectedItem = comboboxCountries.getValue();
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
                if (comboboxCountries.getValue().equals(countryName)) {
                    prefixLabel.setText(phoneNumberPrefix);
                }
            }
            // Populate the ComboBox with country names
            comboboxCountries.getItems().addAll(countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
