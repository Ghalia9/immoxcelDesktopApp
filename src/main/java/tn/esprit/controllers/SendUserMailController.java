package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.User;
import tn.esprit.utils.DataSource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class SendUserMailController {



    @FXML
    private Button SendMail;

    @FXML
    private TextField emailToSend;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateCode()
    {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(19); // Generate a code of length 16 + 3 hyphens
        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                codeBuilder.append('-');
            }
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }
        return codeBuilder.toString();
    }

    Connection connection = DataSource.getInstance().getCnx();
    public void SendMail(ActionEvent actionEvent) throws SQLException {
        if(AddAccountController.validateMail(emailToSend.getText())) {
            String query="SELECT * FROM user WHERE email=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,emailToSend.getText());
            ResultSet rst=statement.executeQuery();

            if(rst.next())
            {
                User userToChange=new User(rst.getInt("id"),rst.getString("password"),rst.getString("username"),rst.getString("email"),rst.getInt("employee_id"));
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");


                Session session = Session.getInstance(properties, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("immoxcel@gmail.com", "ppkb asto zycg qfmu");
                    }
                });
                try {
                    String SecretCode=generateCode();
                    String recipientEmail = emailToSend.getText();
                    InternetAddress recipientAddress = new InternetAddress(recipientEmail);
                    Message msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress("immoxcel@gmail.com"));
                    msg.setRecipient(Message.RecipientType.TO, recipientAddress);
                    msg.setSubject("Reset Password");
                    msg.setText("This is your Secret Code to reset your Password : "+SecretCode);

                    Transport.send(msg);
                    System.out.println("mail sent");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mail");
                    alert.setContentText("Mail Sent");
                    alert.showAndWait();

                    Stage stage = (Stage) SendMail.getScene().getWindow();
                    // Close the stage
                    stage.close();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
                    Parent parent = loader.load();

                    // Set the instance of DashboardController to AddAccountController
                    ResetPasswordController resetPasswordController = loader.getController();
                    resetPasswordController.setSecretCode(SecretCode,userToChange);


                    Scene scene = new Scene(parent);
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.initStyle(StageStyle.UTILITY);
                    newStage.show();

                } catch (MessagingException | IOException e) {
                    e.printStackTrace(); // Print the stack trace for debugging
                    // Handle the exception appropriately, perhaps by displaying an error message to the user
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Mail not Found");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Wrong Mail Format");
            alert.showAndWait();
        }
    }
}
