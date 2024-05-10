package tn.esprit.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.models.Transaction;
import tn.esprit.services.ServiceTransaction;

import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class CodeSmsController  implements Initializable {
    @FXML
    private Label companyName;
    @FXML
    private Label codeTextFieldLabel;

    @FXML
    private TextField codeTextField;
    @FXML
    private Label timerLabel;
    private final ServiceTransaction sp = new ServiceTransaction();

    SendSms send =new  SendSms();
    private int secondsRemaining = 20;
    private Timeline timeline;


    public static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(9000) + 1000; // Génère un nombre aléatoire entre 1000 et 9999 inclus
    }

    String msg = String.valueOf(generateRandomNumber());
    public void initialize() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsRemaining--;
            if (secondsRemaining <= 0) {
                timeline.stop();
                Stage stage = (Stage) codeTextFieldLabel.getScene().getWindow();
                stage.close();
            } else {
                timerLabel.setText("Time left: " + secondsRemaining + " seconds");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    //send.sendsmsCodeVerification(msg);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsRemaining--;
            if (secondsRemaining < 0) {
                timeline.stop();
                Stage stage = (Stage) codeTextFieldLabel.getScene().getWindow();
                stage.close();

            } else {
                timerLabel.setText("Time left: " + secondsRemaining + " seconds");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();

    }
}

