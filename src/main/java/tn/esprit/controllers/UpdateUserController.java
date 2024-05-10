package tn.esprit.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.DataSource;

public class UpdateUserController {

    private final ServiceUser su = new ServiceUser();
    public Button Update;
    Connection connection = DataSource.getInstance().getCnx();

    @FXML
    private TextField email;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField oldPassword;

    @FXML
    private TextField username;

    private int userID;

    private String password;

    private DashboardController dashboardController;

    private int verifyFrom;

    private DisplayEmployees EmployeesDashboard;

    private Object Controller;
    private int emp_id;

    private String usernameFromTable;

    private ProjectsDashboardController projectsDashboardController;

    private ShowTasksController tasksController;

    private ShowDepotController depotController;

    private DisplayController supplierController;

    private Display2Controller transactionContorller;
    private GoogleCalendarEventsController Calendar;

    // Method to set the DashboardController

    public void setTransactionContorller(Display2Controller transaction,int id,String name) {
        this.transactionContorller=transaction;
        this.userID=id;
        this.usernameFromTable=name;
    }

    public void setSupplierContorller(DisplayController supplier,int id,String name) {
        this.supplierController=supplier;
        this.userID=id;
        this.usernameFromTable=name;
    }
    public void setDashboardController(DashboardController dashboardController,int id,String name) {
        this.dashboardController = dashboardController;
        this.userID=id;
        this.usernameFromTable=name;
    }

    public void setInventoryController(ShowDepotController depot,int id,String name) {
        this.depotController = depot;
        this.userID=id;
        this.usernameFromTable=name;
    }

    public void setProjectController(ProjectsDashboardController projController,int id,String name) {
        this.projectsDashboardController = projController;
        this.userID=id;
        this.usernameFromTable=name;
    }


    public void setTasksController(ShowTasksController task,int id,String name) {
        this.tasksController = task;
        this.userID=id;
        this.usernameFromTable=name;
    }
    public void setProjectTasksController(ProjectsDashboardController project ,int id,String name) {
        this.projectsDashboardController = project;
        this.userID=id;
        this.usernameFromTable=name;
    }

    public void setDisplayEmployeesController(DisplayEmployees DController,int id,String name) {
        this.EmployeesDashboard = DController;
        this.userID=id;
        this.usernameFromTable=name;
    }
    
    public void setCalendar(GoogleCalendarEventsController google,int id,String name) {
        this.Calendar = google;
        this.userID=id;
        this.usernameFromTable=name;
    }
    private DisplayLeaves leaves;
    public void setLeavesController(DisplayLeaves leaves,int id,String name) {
        this.leaves = leaves;
        this.userID=id;
        this.usernameFromTable=name;
    }
    private LoginController loginController;
    public void setLoginController(LoginController login)
    {
        this.loginController=login;
    }

    private boolean searchUsername(String username) throws SQLException {
            boolean result;
            String query = "SELECT username FROM user WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            ResultSet rst = statement.executeQuery();
            if (rst.next())
                {
                    result = true;
                }
            else
                {
                    result = false;
                }
            return result;
        }
    // Method to refresh the users table in DashboardController
    private void refreshUsersTable() throws IOException {
        if (dashboardController != null) {
            try {
                dashboardController.users.clear();
                dashboardController.users();
                ObservableList<Node> children = dashboardController.UsersLayout.getChildren();
                List<Node> nodesToRemove = new ArrayList<>();
                for (Node child : children) {
                    if (child instanceof HBox && child.getProperties().get("controller") != null && child.getProperties().get("controller") instanceof UserCardController) {
                        nodesToRemove.add(child);
                    }
                }
                dashboardController.UsersLayout.getChildren().removeAll(nodesToRemove);
                dashboardController.loadUsers();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception properly
            }
        }
    }

    public void initializeFields() {
        String query = "SELECT email,password,employee_id,username FROM user WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet rst = statement.executeQuery();
            if (rst.next()) {
                emp_id = rst.getInt("employee_id");
                password = rst.getString("password");
                username.setText(rst.getString("username"));
                email.setText(rst.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateData() throws SQLException {
        System.out.println(password);
            if (AddAccountController.validateMail(email.getText())) {
                if (!oldPassword.getText().isEmpty()) {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(13);
                    if (!encoder.matches(oldPassword.getText(), password)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Wrong Old Password");
                        alert.showAndWait();
                    } else {
                        if (newPassword.getText().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("New Password is empty");
                            alert.showAndWait();
                        } else {
                            String hashed_password = encoder.encode(newPassword.getText());

                            su.modifier(new User(userID,hashed_password, username.getText(), email.getText(), emp_id));
                            oldPassword.clear();
                            newPassword.clear();

                        }
                    }
                } else {

                    su.modifier(new User(userID,password, username.getText(), email.getText(), emp_id));
                    oldPassword.clear();
                    newPassword.clear();

                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Wrong Email format");
                alert.showAndWait();
            }
        }
    public void updateUser(ActionEvent actionEvent) throws SQLException, IOException {

        if(Calendar!=null && Calendar.verifyUpdateFrom==2)
        {
            if(username.getText().equals(Calendar.username.getText()))
            {
                updateData();
                Calendar.username.setText(username.getText());
                Calendar.userConnected.setUsername(username.getText());
                Calendar.verifyUpdateFrom=0;
                closePopUp();
            }
            else
            {
                if(!searchUsername(username.getText()))
                {
                    updateData();
                    Calendar.username.setText(username.getText());
                    Calendar.userConnected.setUsername(username.getText());
                    Calendar.verifyUpdateFrom=0;
                    closePopUp();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                }
            }
        }

        else if(transactionContorller!=null && transactionContorller.verifyUpdateFrom==2)
        {
            if(username.getText().equals(transactionContorller.username.getText()))
            {
                updateData();
                transactionContorller.username.setText(username.getText());
                transactionContorller.userConnected.setUsername(username.getText());
                transactionContorller.verifyUpdateFrom=0;
                closePopUp();
            }
            else
            {
                if(!searchUsername(username.getText()))
                {
                    updateData();
                    transactionContorller.username.setText(username.getText());
                    transactionContorller.userConnected.setUsername(username.getText());
                    transactionContorller.verifyUpdateFrom=0;
                    closePopUp();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                }
            }
        }

        else if(supplierController!=null && supplierController.verifyUpdateFrom==2)
        {
            if(username.getText().equals(supplierController.username.getText()))
            {
                updateData();
                supplierController.username.setText(username.getText());
                supplierController.userConnected.setUsername(username.getText());
                supplierController.verifyUpdateFrom=0;
                closePopUp();
            }
            else
            {
                if(!searchUsername(username.getText()))
                {
                    updateData();
                    supplierController.username.setText(username.getText());
                    supplierController.userConnected.setUsername(username.getText());
                    supplierController.verifyUpdateFrom=0;
                    closePopUp();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                }
            }
        }

       else if(depotController!=null && depotController.verifyUpdateFrom==2)
        {
            if(username.getText().equals(depotController.username.getText()))
            {
                updateData();
                depotController.username.setText(username.getText());
                depotController.userConnected.setUsername(username.getText());
                depotController.verifyUpdateFrom=0;
                closePopUp();
            }
            else
            {
                if(!searchUsername(username.getText()))
                {
                    updateData();
                    depotController.username.setText(username.getText());
                    depotController.userConnected.setUsername(username.getText());
                    depotController.verifyUpdateFrom=0;
                    closePopUp();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                }
            }
        }
        else if(tasksController!=null && tasksController.verifyUpdateFrom==2)
        {
            if(username.getText().equals(tasksController.username.getText()))
            {
                updateData();
                tasksController.username.setText(username.getText());
                tasksController.userConnected.setUsername(username.getText());
                tasksController.verifyUpdateFrom=0;
                closePopUp();
            }
            else
            {
                if(!searchUsername(username.getText()))
                {
                    updateData();
                    tasksController.username.setText(username.getText());
                    tasksController.userConnected.setUsername(username.getText());
                    tasksController.verifyUpdateFrom=0;
                    closePopUp();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                }
            }
        }

        else if(projectsDashboardController!=null && projectsDashboardController.verifyUpdateFrom==2)
        {
            if(username.getText().equals(projectsDashboardController.username.getText()))
            {
                updateData();
                projectsDashboardController.username.setText(username.getText());
                projectsDashboardController.userConnected.setUsername(username.getText());
                projectsDashboardController.verifyUpdateFrom=0;
                closePopUp();
            }
            else
            {
                if(!searchUsername(username.getText()))
                {
                    updateData();
                    projectsDashboardController.username.setText(username.getText());
                    projectsDashboardController.userConnected.setUsername(username.getText());
                    projectsDashboardController.verifyUpdateFrom=0;
                    closePopUp();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                }
            }
        }

        else if(EmployeesDashboard!=null && EmployeesDashboard.verifyUpdateFrom==2)
        {
            System.out.println("test");
            if(username.getText().equals(EmployeesDashboard.username.getText()))
            {
                updateData();
                EmployeesDashboard.username.setText(username.getText());
                EmployeesDashboard.userConnected.setUsername(username.getText());
                EmployeesDashboard.verifyUpdateFrom=0;
                closePopUp();
            }
            else
            {
                if(!searchUsername(username.getText()))
                {
                    updateData();
                    EmployeesDashboard.username.setText(username.getText());
                    EmployeesDashboard.userConnected.setUsername(username.getText());
                    EmployeesDashboard.verifyUpdateFrom=0;
                    closePopUp();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                }
            }
        }
       else if(dashboardController!=null && dashboardController.verifyUpdateFrom==2)
       {
           System.out.println("first : "+dashboardController.usernameConnected.getText());
           if(username.getText().equals(dashboardController.usernameConnected.getText()))
            {
                System.out.println("before : "+dashboardController.usernameConnected.getText());
                updateData();

                dashboardController.userConnected.setUsername(username.getText());
                dashboardController.usernameConnected.setText(dashboardController.userConnected.getUsername());
                System.out.println("after : "+dashboardController.usernameConnected.getText());
                dashboardController.setLoginController(loginController,dashboardController.userConnected);
                dashboardController.verifyUpdateFrom=0;
                closePopUp();
            }
            else
                {
                    if(!searchUsername(username.getText()))
                        {
                            System.out.println("before : "+dashboardController.usernameConnected.getText());
                           updateData();

                           dashboardController.userConnected.setUsername(username.getText());
                            dashboardController.usernameConnected.setText(dashboardController.userConnected.getUsername());
                            System.out.println("after : "+dashboardController.usernameConnected.getText());

                           dashboardController.verifyUpdateFrom=0;


                            closePopUp();

                        }
                    else
                       {
                           Alert alert = new Alert(Alert.AlertType.ERROR);
                           alert.setTitle("Error");
                           alert.setContentText("Username already taken");
                           alert.showAndWait();
                       }
                }
       }
       else if(dashboardController!=null && dashboardController.verifyUpdateFrom==0 && EmployeesDashboard==null){
           if(username.getText().equals(usernameFromTable))
       {
           System.out.println("test");
           updateData();
           initializeFields();
           refreshUsersTable();
           closePopUp();
       }
       else
       {
           if(!searchUsername(username.getText()))
           {
               updateData();
               initializeFields();
               refreshUsersTable();
               closePopUp();
           }
           else
           {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setContentText("Username already taken");
               alert.showAndWait();
           }
       }

       }


    }

    private void closePopUp()
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update");
            alert.setContentText("User Updated");
            alert.showAndWait();

            Stage stage = (Stage) Update.getScene().getWindow();
            // Close the stage
            stage.close();
        }



}
