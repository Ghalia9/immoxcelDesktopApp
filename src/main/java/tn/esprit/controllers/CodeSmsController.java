package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import tn.esprit.models.Transaction;
import tn.esprit.services.ServiceTransaction;

import java.sql.SQLException;

public class CodeSmsController {
    @FXML
    private Label companyName;
    @FXML
    private Label codeTextFieldLabel;

    @FXML
    private TextField codeTextField;
    private final ServiceTransaction sp = new ServiceTransaction();
    public void setFields(int id){
        try {
            System.out.println("////////////  THE ID = " + id);
            System.out.println("the iddddddddd ");
            String idTras = String.valueOf(id);
            codeTextFieldLabel.setText(idTras);
        }catch (Exception e) {
            // Handle any exceptions that might occur during setting text fields
            System.err.println("Error occurred while setting text fields: " + e.getMessage());
            e.printStackTrace();}
    }
    @FXML
    void validOnAction(ActionEvent evt) {
            int id = Integer.parseInt(codeTextFieldLabel.getText());
            String msg = "4585";
            Transaction transaction = sp.getOneById(id);
            System.out.println(" the id retrieve equals to = " + id);
            if (codeTextField.getText().isEmpty()) {
                displayErrorAlert("Warning Blank Field❌,Check Your Phone and Enter The Code \uD83D\uDCAC");
            } else {
                if (codeTextField.getText().equals(msg)) {
                    confirmationAlert("The Entered Code is Correct ");
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Delete Transaction");
                    confirmationAlert.setHeaderText("Are you sure you want to delete this transaction ❓");
                    confirmationAlert.setContentText("This action cannot be undone.❗");
                    confirmationAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (transaction != null) {
                                System.out.println("Transaction is not null ");
                                sp.supprimer(id);
                                confirmationAlert("The transaction have been Deleted succsfully ✅ ");
                                Stage stage = (Stage) codeTextField.getScene().getWindow();
                                stage.close();
                            }
                        }
                    });
                } else {
                    displayErrorAlert("The Code is Incorrect ");
                }
            }
    }
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void confirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

