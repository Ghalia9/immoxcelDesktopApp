package com.example.myjavafxapp;
import Entities.Supplier;
import Entities.Transaction;
import Services.ServiceTransaction;
import Services.ServiceSupplier;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;


import java.util.*;
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

    private  ServiceTransaction sp = new ServiceTransaction();
    private  ServiceTransaction transaction = new ServiceTransaction();
    private  ServiceSupplier supplier = new ServiceSupplier() ;
    private Map<String, Integer> supplierMap;

    private String [] type ={"Income","Salary","Expenses"};
    private Alert alert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeTextField.getItems().addAll(type);
        supplierMap = supplier.getSupplierNameAndIdMap(); // Initialize supplierMap
        SupplierComboBox.getItems().addAll(supplierMap.keySet());
    }
    public void saveButtonOnAction(ActionEvent event){
        if (QuantityTextField.getText().isEmpty() || DescrptionTextField.getText().isEmpty() || CostTextField.getText().isEmpty() || SupplierComboBox.getValue()== null ||typeTextField.getValue()==null) {
            /*if (DescrptionTextField.getText().length() < 3) {
                DescrptionTextField.setStyle("-fx-border-color: #821515 ; -fx-border-width:2px; ");

            } else {
                DescrptionTextField.setStyle(null);
            }*/
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("You need to fill blank field ");
            alert.showAndWait();

        }else {
            if(!isNumeric(QuantityTextField.getText()) || !isNumeric(CostTextField.getText()) ){
                alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Requires numbers"+ "Check The Quantity and cost Field ");
                alert.showAndWait();
            }
            else {
                if (DescrptionTextField.getText().length() < 3) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setContentText(" Description field requires more than 3 caracteres ");
                    alert.showAndWait();

                } else {
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
                    }
                    else {
                        Supplier supplier1 = transaction.getOneByIdSupplier(supplierId);
                        try {
                            System.out.println("//////////////////////////"+supplier1);
                            sp.ajouter(new Transaction(typeTextField.getValue(), DescrptionTextField.getText(), quantity, cost, supplier1));
                            System.out.println("the supplier is:"+supplier1.getId_supp());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setContentText("GG");
                            alert.show();
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
            double d = Double.parseDouble(str);

        }catch (NumberFormatException | NullPointerException e ){
            return false ;
        }
        return true;
    }
    public void swtichToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dispa.fxml"));
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
