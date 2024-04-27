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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.DataSource;

public class UpdateUserController {

    private final ServiceUser su = new ServiceUser();
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

    private int emp_id;

    private String usernameFromTable;

    // Method to set the DashboardController
    public void setDashboardController(DashboardController dashboardController,int id,String name) {
        this.dashboardController = dashboardController;
        this.userID=id;
        this.usernameFromTable=name;
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
<<<<<<< Updated upstream
        System.out.println(password);
=======
        if(username.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Username is empty");
            alert.showAndWait();
        }
        else {
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
                            su.modifier(new User(userID,hashed_password, username.getText(), email.getText(), emp_id));
=======
                            su.modifier(new User(userID, hashed_password, username.getText(), email.getText(), emp_id));
>>>>>>> Stashed changes
                            oldPassword.clear();
                            newPassword.clear();

                        }
                    }
                } else {

<<<<<<< Updated upstream
                    su.modifier(new User(userID,password, username.getText(), email.getText(), emp_id));
=======
                    su.modifier(new User(userID, password, username.getText(), email.getText(), emp_id));
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
        }
>>>>>>> Stashed changes
    public void updateUser(ActionEvent actionEvent) throws SQLException, IOException {
       if(dashboardController.verifyUpdateFrom==2)
       { if(username.getText().equals(dashboardController.usernameConnected.getText()))
            {
           updateData();
           dashboardController.usernameConnected.setText(username.getText());
            dashboardController.userConnected.setUsername(username.getText());
            }
        else
       {
           if(!searchUsername(username.getText()))
           {
               updateData();
               dashboardController.usernameConnected.setText(username.getText());
               dashboardController.userConnected.setUsername(username.getText());
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
       else if(dashboardController.verifyUpdateFrom==0){if(username.getText().equals(usernameFromTable))
       {
           updateData();
           initializeFields();
           refreshUsersTable();
       }
       else
       {
           if(!searchUsername(username.getText()))
           {
               updateData();
               initializeFields();
               refreshUsersTable();
           }
           else
           {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setContentText("Username already taken");
               alert.showAndWait();
           }
       }}


    }



}
