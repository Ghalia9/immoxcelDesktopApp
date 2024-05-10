package tn.esprit.controllers;

import tn.esprit.models.Transaction;
import tn.esprit.models.Supplier;
import tn.esprit.models.Capital;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.services.ServiceSupplier;
import tn.esprit.services.ServiceTransaction;

import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateTransactionController implements Initializable {
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

    ServiceTransaction sp = new ServiceTransaction();
    private  ServiceTransaction transaction = new ServiceTransaction();
    private ServiceSupplier supplier = new ServiceSupplier() ;
    private Map<String, Integer> supplierMap;


    private String [] type ={"Income","Salary","Expenses"};
    private Alert alert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeTextField.getItems().addAll(type);
        supplierMap = supplier.getSupplierNameAndIdMap(); // Initialize supplierMap
        SupplierComboBox.getItems().addAll(supplierMap.keySet());
    }

    public void setCancelButtonIDAction(ActionEvent event ){
        Stage stage = (Stage)  CancelButton.getScene().getWindow();
        stage.close();
    }
    public void setFields(int id,String Type, float q, String Description , float c  ){
        System.out.println("///////////////////////////////////////////////////////////");
        try {
            String idyo = String.valueOf(id);
            System.out.println("////////////  THE ID = "+ idyo);
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
    public void EditOnClickOnUP(ActionEvent event){
        if (QuantityTextField.getText().isEmpty() || DescrptionTextField.getText().isEmpty() || CostTextField.getText().isEmpty() || SupplierComboBox.getValue()== null ||typeTextField.getValue()==null) {

            displayErrorAlert( "You need to fill blank field " );
        }else {
            if(!isNumeric(QuantityTextField.getText()) || !isNumeric(CostTextField.getText()) ){
                displayErrorAlert( "Requires numbers Check The Quantity and cost Fields " );

            }
            else {
                if (DescrptionTextField.getText().length() < 3)
                {
                    displayErrorAlert( "Description field requires more than 3 caracteres" );
                } else if (Integer.parseInt(QuantityTextField.getText())<0 || Integer.parseInt(CostTextField.getText())<0) {
                    displayErrorAlert( "Required Positive Numbers" );
                } else
                {
                    LabelMessage.setText("You Try to do a Transaction ");
                    float quantity = Float.parseFloat(QuantityTextField.getText());
                    // Convert CostTextField input to a float
                    float cost = Float.parseFloat(CostTextField.getText());
                    int id = Integer.parseInt(idtrans.getText());
                    String selectedName = SupplierComboBox.getValue(); // Get selected supplier name
                    System.out.println("selectted Name = "+selectedName);
                    int supplierId = supplierMap.get(selectedName); // Get ID from supplierMap
                    if (SupplierComboBox.getValue().isEmpty()) {
                        System.out.println("it is empty");
                        supplierId=5;
                    }
                    else {
                        Supplier supplier1 = transaction.getOneByIdSupplier(supplierId);
                        Capital capital=sp.retrieveCurrentCapitalFromDatabase();
                        float totalAmount = quantity *cost;
                        String type = typeTextField.getValue();
                        if ("Salary".equals(type)){
                            if(capital.getSalary() < totalAmount) {
                                System.out.println("You cannot proceed. Insufficient salary.");
                                displayErrorAlert("You cannot proceed. Insufficient salary.");
                            } else {
                                sp.modifier(new Transaction(id, typeTextField.getValue(), DescrptionTextField.getText(), quantity, cost,supplier1));
                                displayConfirmationAlert("Modified Succefully ");
                                Stage stage = (Stage) CostTextField.getScene().getWindow();
                                stage.close();
                            }
                        }
                        else if("Expenses".equals(type)){
                            if(capital.getExepenses() < totalAmount) {
                                System.out.println("You cannot proceed. Insufficient Money in Expenses.");
                                displayErrorAlert("You cannot proceed. Insufficient Money in Expenses.");
                            } else {
                                sp.modifier(new Transaction(id, typeTextField.getValue(), DescrptionTextField.getText(), quantity, cost,supplier1));
                                displayConfirmationAlert("Modified Succefully ");
                                Stage stage = (Stage) CostTextField.getScene().getWindow();
                                stage.close();
                            }
                        }
                        else {
                            sp.modifier(new Transaction(id, typeTextField.getValue(), DescrptionTextField.getText(), quantity, cost,supplier1));
                            displayConfirmationAlert("Modified Succefully ");
                            Stage stage = (Stage) CostTextField.getScene().getWindow();
                            stage.close();
                        }
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
}
