package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.ContractType;
import tn.esprit.models.Employees;
import tn.esprit.models.Functions;
import tn.esprit.models.Sexe;
import tn.esprit.services.ServiceEmployees;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public class EditEmployee {
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
    private Employees currentEmployee;

    public void setCurrentEmployee(Employees currentEmployee)
    {
        this.currentEmployee=currentEmployee;
    }
    public void setdashbord(HRDashboard dashboard)
    {
        this.dashboard=dashboard;
    }
    private AnchorPane displayPage;

    public void setDisplayPage(AnchorPane displayPage)
    {
        this.displayPage=displayPage;
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
                String fileName= "CV_"+currentEmployee.getEmpName()+"_"+currentEmployee.getEmpLastName()+".pdf";
               // String fileName = file.getName();
                //cvField.setText(cvFileName);
                // Construct the full destination path
                String destinationPath = UPLOAD_DIRECTORY + "/" + fileName;
                System.out.println("cvvvvvvvvvvvvvvvvvvvvvvvvv");
                System.out.println(destinationPath);
                if (currentEmployee.getEmpCV() != null) {
                    System.out.println("aaaaaaaaaaaaaa");
                    System.out.println("delete");
                    String oldCVFileName = "CV_" + currentEmployee.getEmpName() +"_"+ currentEmployee.getEmpLastName(); // Or any other identifier you choose
                    File oldCVFile = new File(UPLOAD_DIRECTORY + "/" + oldCVFileName);
                    System.out.println(oldCVFileName);
                    if (oldCVFile.delete()) {
                        System.out.println("Old CV file deleted successfully.");
                    } else {
                        System.out.println("Failed to delete old CV file.");
                    }
                }

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
    @FXML
    void save_employeeOnClick(ActionEvent event) throws IOException {
        System.out.println(currentEmployee);
        if (validateInputs()) {
        Employees old=currentEmployee;
        Employees e=setInformations(currentEmployee.getId());
        if(old.equals(e)) {
            se.modifier(e);
            // Show notification
            showNotification("Employee updated successfully.");


            // Close the form stage
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();
            System.out.println(dashboard);
            dashboard.getEmployeesLayout().getChildren().clear();
            dashboard.showEmployeesList();
            dashboard.getLeavesLayout().getChildren().clear();
            dashboard.showOldLeaves();
            dashboard.getCardLayout().getChildren().clear();
            dashboard.showPendingLeaves();
            //displayPage.getChildren().clear();
            //REFRESH DETAILS EMPLOYEE
           /* FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEmployee.fxml"));
            Parent Details = loader.load();
            DetailsEmployee employeedetails=loader.getController();
           // employeedetails.details_employee(event);
           /* Stage detailsEmpStage = new Stage();
            detailsEmpStage.initStyle(StageStyle.DECORATED);
            detailsEmpStage.setScene(new Scene(Details, 638, 574));
            detailsEmpStage.setTitle("Employee Details");
            employeedetails.setDetailsData(currentEmployee);
            employeedetails.setCurrentEmployee(currentEmployee);
            detailsEmpStage.showAndWait();*/
            //END REFRESH EMPLOYEE DETAILS
            //dashboard.getEmployeesLayout().getChildren().clear();
            //dashboard.showEmployeesList();
        }else {
        // If the employee already exists, show an error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Employee already exists!");
        alert.setContentText("Employee CIN must be unique!");

// Add custom CSS class to the dialog pane
        alert.getDialogPane().getStyleClass().add("error-notification-dialog");

// Show the error alert dialog
        alert.showAndWait();
    }}
    }

    private boolean validateInputs() {
        // Validate age
        LocalDate birthday = birthdayField.getValue();
        LocalDate currentDate = LocalDate.now();
        if (birthday != null && birthday.plusYears(18).isAfter(currentDate)) {
            showValidationError("Employee must be at least 18 years old.");
            return false;
        }

        // Validate phone number
        String phoneNumber = phoneField.getText();
        if (!phoneNumber.matches("\\d{8}")) {
            showValidationError("Phone number must be 8 digits.");
            return false;
        }

        // Validate email
        String email = emailField.getText();
        if (!isValidEmail(email)) {
            showValidationError("Invalid email address.");
            return false;
        }

        // Validate hire date and end contract date
        LocalDate hireDate = hiraDateField.getValue();
        LocalDate endContractDate = endContractField.getValue();
        if (hireDate != null && endContractDate != null && hireDate.isAfter(endContractDate)) {
            showValidationError("Hire date must be before end contract date.");
            return false;
        }

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
    public void setDetailsData(Employees employee){
        addressField.setText(employee.getEmpAddress());
        birthdayField.setValue(employee.getBirthDate().toLocalDate());
        cinField.setText(employee.getEmpCin());
        contractTypeField.setValue(ContractType.valueOf(employee.getContractType()));
        //cv.setText(employee.getEmpCV().toString());
        if(employee.getEmpCV() == null){
            cvField.setText("no cv uploaded yet!");
        }else {
            String defaultFileName = "CV_" + employee.getEmpName() +"_"+ employee.getEmpLastName(); // Or any other identifier you choose
            cvField.setText(defaultFileName);
        }
        emailField.setText(employee.getEmpEmail());
        endContractField.setValue(employee.getEndContractDate().toLocalDate());
        firstNameField.setText(employee.getEmpName());
        functionField.setValue(Functions.valueOf(employee.getEmpFunction()));

        hiraDateField.setValue(employee.getHireDate().toLocalDate());
        lastNameField.setText(employee.getEmpLastName());
        phoneField.setText(employee.getEmpPhone());
        sexeField.setValue(Sexe.valueOf(employee.getEmpSex()));
    }
    public Employees setInformations(int id){
        try {
            Blob cv =new SerialBlob(cvField.getText().getBytes());
            Employees employee=new Employees(id,firstNameField.getText(),lastNameField.getText(),sexeField.getValue().toString(),emailField.getText(),addressField.getText(),phoneField.getText(),functionField.getValue().toString(), Date.valueOf(birthdayField.getValue()),Date.valueOf(hiraDateField.getValue()),Date.valueOf(endContractField.getValue()),contractTypeField.getValue().toString(),12,cv,currentEmployee.getEmpTakenLeaves(),cinField.getText());

            System.out.println("settttttttttt");
            System.out.println(employee);
            return employee;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    private final ServiceEmployees se = new ServiceEmployees();

    @FXML
    public void initialize(){
        contractTypeField.setItems(FXCollections.observableArrayList(ContractType.values()));
        functionField.setItems(FXCollections.observableArrayList(Functions.values()));
        sexeField.setItems(FXCollections.observableArrayList(Sexe.values()));
    }
}
