package tn.esprit.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.models.ContractType;
import tn.esprit.models.Employees;
import tn.esprit.models.Functions;
import tn.esprit.models.Sexe;
import tn.esprit.services.ServiceEmployees;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AddEmployee {
    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private TextField cinField;

    @FXML
    private ChoiceBox<ContractType> contractTypeField;

    @FXML
    private TextField cvField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker endContractField;

    @FXML
    private TextField firstNameField;

    @FXML
    private ChoiceBox<Functions> functionField;

    @FXML
    private DatePicker hiraDateField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ChoiceBox<Sexe> sexeField;

    private HRDashboard dashboard;

    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }
    private DisplayEmployees de;

    public void setDE(DisplayEmployees de) {
        this.de = de;
    }
    private static final String UPLOAD_DIRECTORY = "C:/Users/ghali/Desktop/Fac3/S2/PIJava/immoxcel/src/main/resources/CVuploads"; // Change this to your desired directory

    @FXML
    void uploadCVOnClick(ActionEvent event) {
        System.out.println("uploaddddddddddddddddddddd");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload CV");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // Generate a unique file name (e.g., using UUID)
               // String fileName = UUID.randomUUID().toString() + "_" + file.getName();
                String fileName = file.getName();
                //cvField.setText(cvFileName);
                // Construct the full destination path
                String destinationPath = UPLOAD_DIRECTORY + "/" + fileName;
                System.out.println("cvvvvvvvvvvvvvvvvvvvvvvvvv");
                System.out.println(destinationPath);
                // Copy the file to the centralized directory
                Files.copy(file.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);

                // Store the file name or relative path in your database
                // Instead of cvField.setText(), you can store fileName or relativePath in your database
                cvField.setText(file.getName()); // For display purposes (optional)
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
                showNotification("Error occurred while uploading CV.");
            }
        }
    }

    // Method to construct the full file path
    private String getFullFilePath(String fileName) {
        return UPLOAD_DIRECTORY + "/" + fileName;
    }


    // Method to convert a file to Blob
    private Blob fileToBlob(File file) throws IOException, SQLException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fileInputStream.read(fileBytes);
        fileInputStream.close();
        return new SerialBlob(fileBytes);
    }
    @FXML
    void add_employeeOnClick(ActionEvent event) {
        if (validateInputs()) {
            Employees e = setInformations();
            if (!e.exists(e)) {

                se.ajouter(e);
                // Show notification
                showNotification("Employee added successfully.");


                // Close the form stage
                Stage stage = (Stage) firstNameField.getScene().getWindow();
                stage.close();
                de.getEmployeesLayout().getChildren().clear();
                de.showEmployeesList();
            } else {
                // If the employee already exists, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Employee already exists!");
                alert.setContentText("Employee CIN must be unique!");

// Add custom CSS class to the dialog pane
                alert.getDialogPane().getStyleClass().add("error-notification-dialog");

// Show the error alert dialog
                alert.showAndWait();
            }
        }
    }

    private boolean validateInputs() {
        // Check if all fields are empty
        if (firstNameField.getText().isEmpty() &&
                lastNameField.getText().isEmpty() &&
                addressField.getText().isEmpty() &&
                cinField.getText().isEmpty() &&
                emailField.getText().isEmpty() &&
                phoneField.getText().isEmpty() &&
                sexeField.getValue() == null &&
                functionField.getValue() == null &&
                contractTypeField.getValue() == null) {
            showValidationError("All fields must be filled.");
            return false;
        }
        // Validate first name
        String firstName = firstNameField.getText();
        if (firstName.isEmpty()) {
            showValidationError("First name is required.");
            return false;
        }
        if (!firstName.matches("[a-zA-Z]+")) {
            showValidationError("First name must contain only letters.");
            return false;
        }

        // Validate last name
        String lastName = lastNameField.getText();
        if (lastName.isEmpty()) {
            showValidationError("Last name is required.");
            return false;
        }
        if (!lastName.matches("[a-zA-Z]+")) {
            showValidationError("Last name must contain only letters.");
            return false;
        }
        // Validate CIN
        String cin = cinField.getText();
        if (cin.isEmpty()) {
            showValidationError("CIN is required.");
            return false;
        }
        if (!cin.matches("\\d{8}")) {
            showValidationError("CIN must contain exactly 8 numbers.");
            return false;
        }

        // Validate gender
        String gender = sexeField.getValue().toString();
        if (gender == null) {
            showValidationError("Gender is required.");
            return false;
        }

        // Validate birthdate
        LocalDate birthday = birthdayField.getValue();
        if (birthday == null) {
            showValidationError("Birthdate is required.");
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        if (birthday.plusYears(18).isAfter(currentDate)) {
            showValidationError("Employee must be at least 18 years old.");
            return false;
        }

        // Validate email
        String email = emailField.getText();
        if (email.isEmpty()) {
            showValidationError("Email is required.");
            return false;
        }
        if (!isValidEmail(email)) {
            showValidationError("Invalid email address.");
            return false;
        }

        // Validate function
        String function = functionField.getValue().toString();
        if (function == null) {
            showValidationError("Function is required.");
            return false;
        }

        // Validate hire date and end contract date
        LocalDate hireDate = hiraDateField.getValue();
        LocalDate endContractDate = endContractField.getValue();
        if (hireDate != null && endContractDate != null && hireDate.isAfter(endContractDate)) {
            showValidationError("Hire date must be before end contract date.");
            return false;
        }

        // Validate contract type
        String contractType = contractTypeField.getValue().toString();
        if (contractType == null) {
            showValidationError("Contract type is required.");
            return false;
        }

        // Validate address
        String address = addressField.getText();
        if (address.isEmpty()) {
            showValidationError("Address is required.");
            return false;
        }

        // Validate phone number
        String phoneNumber = phoneField.getText();
        if (phoneNumber.isEmpty()) {
            showValidationError("Phone number is required.");
            return false;
        }
        String regexPattern = "^(9\\d{7}|40\\d{6}|41\\d{6}|42\\d{6}|44\\d{6}|5[0-5]\\d{6}|58\\d{6}|2\\d{7}|46\\d{6})$";
        if (!phoneNumber.matches(regexPattern)) {
            showValidationError("Please enter a valid phone number.");
            return false;
        }

        // All validations passed
        return true;
    }
    private boolean isValidEmail(String email) {
        // Basic email validation
        // You can implement more sophisticated validation if needed
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        // Schedule closing of the alert after the specified duration
       /* Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> alert.close())
        );
        timeline.play();

        */
    }
    private final ServiceEmployees se = new ServiceEmployees();

    @FXML
    public void initialize(){
        contractTypeField.setItems(FXCollections.observableArrayList(ContractType.values()));
        functionField.setItems(FXCollections.observableArrayList(Functions.values()));
        sexeField.setItems(FXCollections.observableArrayList(Sexe.values()));
    }

    public Employees setInformations(){
        try {
            Blob cv = fileToBlob(new File(getFullFilePath(cvField.getText())));
            Employees employee=new Employees(firstNameField.getText(),lastNameField.getText(),sexeField.getValue().toString(),emailField.getText(),addressField.getText(),phoneField.getText(),functionField.getValue().toString(), Date.valueOf(birthdayField.getValue()),Date.valueOf(hiraDateField.getValue()),Date.valueOf(endContractField.getValue()),contractTypeField.getValue().toString(),12,cv,0,cinField.getText());
            return employee;
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}