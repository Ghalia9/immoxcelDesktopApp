package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Mail {
    @FXML
    private TextField message;

    @FXML
    private TextField recepient;

    @FXML
    private TextField subject;
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Pane paneProgressor;
    @FXML
    public void initialize() {
        progressIndicator.setVisible(false);
    }

    private String attachedFilePath;

    @FXML
    void attachFileOnClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Attach File");

        // Set initial directory, filters, etc., if needed

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            attachedFilePath = selectedFile.getAbsolutePath();
            System.out.println("Attached file path: " + attachedFilePath); // Print the file path for verification
            // Optionally, display the selected file name to the user or perform any other actions
        }
    }
    @FXML
    void sendMailOnClick(ActionEvent event) {

        String recipientEmail = recepient.getText();
        String emailSubject = subject.getText();
        String emailMessage = message.getText();

        try {
            sendEmail(progressIndicator,paneProgressor,recipientEmail, emailSubject, emailMessage,attachedFilePath);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            // Handle the error, such as showing an error message to the user
        }
    }
    private String loadEmailTemplate(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private void sendEmail(ProgressIndicator progressIndicator , Pane paneProgressor,String recipientEmail, String emailSubject, String emailMessage, String attachmentPath) throws MessagingException, IOException {


        // Your existing email sending code here
        final String username = "chiboub.ghalia@gmail.com";
        final String password = "twsv pulq brpo nfes";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    paneProgressor.setVisible(true);
                    progressIndicator.setVisible(true);
                    progressIndicator.setProgress(-1);
                });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("chiboub.ghalia@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(emailSubject);

        // Load HTML content from a file
        String htmlContent = loadEmailTemplate("src/main/resources/email_template.html");
        htmlContent = htmlContent.replace("{{name}}", recipientEmail)
                .replace("{{message}}", emailMessage)
                .replace("{{signature}}", "Signed from immoxcel's HR Manager");

        // Add an image attachment
        String imagePath = "src/main/resources/images/logo2.png"; // Replace this with the path to your image file
        addImageToEmail(message, imagePath);

        // Add file attachment
        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            try {
                addAttachmentToEmail(message, attachmentPath);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the IOException, such as showing an error message to the user
                return; // Stop sending the email if there's an issue with the attachment
            }
        }
        message.setContent(htmlContent, "text/html");

        Transport.send(message);

        // Show confirmation popup
        showConfirmationAlert();
            } catch (MessagingException e) {
                e.printStackTrace();
                System.out.println("Failed to send email");
            } finally {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    paneProgressor.setVisible(false);
                });
            }
        }).start();

    }

    private void addImageToEmail(Message message, String imagePath) throws MessagingException {
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource fds = new FileDataSource(imagePath);
        imagePart.setDataHandler(new DataHandler(fds));
        imagePart.setHeader("Content-ID", "<image>");

        // Add the image to the email
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(imagePart);
        message.setContent(multipart);
    }
    private void addAttachmentToEmail(Message message, String attachmentPath) throws MessagingException, IOException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachmentPath);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(source.getName());

        // Add the attachment to the email
        Multipart multipart = (Multipart) message.getContent();
        multipart.addBodyPart(attachmentPart);
        System.out.println("Attachment added successfully: " + attachmentPath); // Log that the attachment was added

    }
    private void showConfirmationAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email Sent");
            alert.setHeaderText(null);
            alert.setContentText("Your email has been sent successfully.");
            alert.showAndWait();
        });
    }
}
