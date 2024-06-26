package tn.esprit.controllers;

import tn.esprit.models.Transaction;
import tn.esprit.models.Supplier;
import tn.esprit.models.Capital;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.services.ServiceSupplier;
import tn.esprit.services.ServiceTransaction;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
public class RegisterController  implements Initializable {

    @FXML
    private Button CancelButton ;
    @FXML
    private Label LabelMessage;
    @FXML
    private ComboBox<String> typeTextField;
    @FXML
    private ComboBox<String> SupplierComboBox;
    @FXML
    private TextField DescrptionTextField;
    @FXML
    private TextField CostTextField;
    @FXML
    private TextField QuantityTextField;
    @FXML
    private Label idtrans;

    private ServiceTransaction sp = new ServiceTransaction();
    private  ServiceTransaction transaction = new ServiceTransaction();
    private ServiceSupplier supplier = new ServiceSupplier() ;
    private Map<String, Integer> supplierMap;

    private String [] type ={"Income","Salary","Expenses"};
    private Alert alert;
    SendSms send = new SendSms();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeTextField.getItems().addAll(type);
        supplierMap = supplier.getSupplierNameAndIdMap(); // Initialize supplierMap
        SupplierComboBox.getItems().addAll(supplierMap.keySet());
    }
    public void saveButtonOnAction(ActionEvent event){
        if (QuantityTextField.getText().isEmpty() || DescrptionTextField.getText().isEmpty() || CostTextField.getText().isEmpty() || SupplierComboBox.getValue()== null ||typeTextField.getValue()==null) {
            QuantityTextField.setStyle("-fx-border-color: #821515 ; -fx-border-width:2px; ");
            CostTextField.setStyle("-fx-border-color: #821515 ; -fx-border-width:2px; ");
            SupplierComboBox.setStyle("-fx-border-color: #821515 ; -fx-border-width:2px; ");
            typeTextField.setStyle("-fx-border-color: #821515 ; -fx-border-width:2px; ");
            if (DescrptionTextField.getText().length() < 3) {
                DescrptionTextField.setStyle("-fx-border-color: #821515 ; -fx-border-width:2px; ");
            } else {
                DescrptionTextField.setStyle(null);
            }
        }else {
            DescrptionTextField.setStyle(null);
            QuantityTextField.setStyle(null);
            CostTextField.setStyle(null);
            SupplierComboBox.setStyle(null);
            typeTextField.setStyle(null);
            if(!isNumeric(QuantityTextField.getText()) || !isNumeric(CostTextField.getText()) ){
                displayErrorAlert("Requires Numbers Check The Quantity and cost Field ");
            }
            else
            {
                    QuantityTextField.setStyle(null);
                    CostTextField.setStyle(null);
                    DescrptionTextField.setStyle(null);
                    LabelMessage.setText("You Try to do a Transaction ");
                    float quantity = Float.parseFloat(QuantityTextField.getText());
                    // extracting the selected supplier
                    String selectedName = SupplierComboBox.getValue(); // Get selected supplier name
                    System.out.println("selectted Name = "+selectedName);
                    int supplierId = supplierMap.get(selectedName); // Get ID from supplierMap
                    // Convert CostTextField input to a float
                    float cost = Float.parseFloat(CostTextField.getText());
                    if (SupplierComboBox.getValue().isEmpty()) {
                        System.out.println("it is empty");
                        supplierId=5;
                    } else if (Integer.parseInt(QuantityTextField.getText())<0 || Integer.parseInt(CostTextField.getText())<0) {
                        displayErrorAlert("Required positive number");
                    } else
                    {
                        Supplier supplier1 = transaction.getOneByIdSupplier(supplierId);
                        Capital capital=sp.retrieveCurrentCapitalFromDatabase();
                        float totalAmount = quantity *cost;
                        String type = typeTextField.getValue();
                        if ("Salary".equals(type)){
                            if(capital.getSalary() < totalAmount) {
                                System.out.println("You cannot proceed. Insufficient salary.");
                                displayErrorAlert("You cannot proceed. Insufficient salary.");
                            } else {
                                sp.ajouter(new Transaction(typeTextField.getValue(), DescrptionTextField.getText(), quantity, cost, supplier1));
                                send.sendsms("Salary");
                                displayConfirmationAlert("Added Succefully ");

                                Stage stage = (Stage) CostTextField.getScene().getWindow();
                                stage.close();

                            }
                       }
                        else if("Expenses".equals(type)){
                            if(capital.getExepenses() < totalAmount) {
                                System.out.println("You cannot proceed. Insufficient Money in Expenses.");
                                displayErrorAlert("You cannot proceed. Insufficient Money in Expenses.");
                            } else {
                                sp.ajouter(new Transaction(typeTextField.getValue(), DescrptionTextField.getText(), quantity, cost, supplier1));
                                send.sendsms("Expenses");

                                displayConfirmationAlert("Added Succefully ");
                                Stage stage = (Stage) CostTextField.getScene().getWindow();
                                stage.close();
                            }
                        }
                        else {
                            sp.ajouter(new Transaction(typeTextField.getValue(), DescrptionTextField.getText(), quantity, cost, supplier1));
                            send.sendsms("Income");

                            displayConfirmationAlert("Added Succefully ");
                            Stage stage = (Stage) CostTextField.getScene().getWindow();
                            stage.close();
                        }
                    }
            }
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
    public void setCancelButtonIDAction(ActionEvent event ){
        Stage stage = (Stage)  CancelButton.getScene().getWindow();
        stage.close();
    }
    public void setFields(int id,String Type, int q, String Description , float c  ){
        try {
            String idyo = String.valueOf(id);
            String Quantity = String.valueOf(q);
            String cost = String.valueOf(c);
            typeTextField.setValue(Type);
            QuantityTextField.setText(Quantity);
            DescrptionTextField.setText(Description);
            CostTextField.setText(cost);
            idtrans.setText(idyo);
            System.out.println("quantity:"+Quantity+"cost"+cost);
        } catch (Exception e) {
            // Handle any exceptions that might occur during setting text fields
            System.err.println("Error occurred while setting text fields: " + e.getMessage());
            e.printStackTrace();
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
    public void swtichToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Dispa.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading Dispa.fxml");
            alert.showAndWait();
        }
    }









}
