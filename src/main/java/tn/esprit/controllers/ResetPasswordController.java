package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResetPasswordController {

    @FXML
    private Button resetPassword;
    @FXML
    private PasswordField confirmPassword;

    @FXML
    private PasswordField newPassword;

    @FXML
    private TextField secretCode;

    private String secret;

    private User userToChange;

    Connection connection = DataSource.getInstance().getCnx();
    private final ServiceUser su = new ServiceUser();

    public void setSecretCode(String code,User user)
    {
        this.secret=code;
        this.userToChange=user;
    }


    public void ResetPassword(ActionEvent actionEvent) throws SQLException {
        if(secretCode.getText().equals(secret))
            {
                if(newPassword.getText().equals(confirmPassword.getText()))
                    {
                        String password;
                        int customCostFactor = 13;
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(customCostFactor);
                        password = encoder.encode(newPassword.getText());
                        userToChange.setPassword(password);
                        su.modifier(userToChange);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Password");
                        alert.setContentText("Password Changed Successfully");
                        alert.showAndWait();
                        Stage stage = (Stage) resetPassword.getScene().getWindow();
                        // Close the stage
                        stage.close();

                    }
                else
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("New Password and Confirm Password are not matching");
                        alert.showAndWait();
                    }
            }
        else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Wrong Secret Code");
                alert.showAndWait();
            }
    }
}
