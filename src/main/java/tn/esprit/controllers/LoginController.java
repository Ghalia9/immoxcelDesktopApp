package tn.esprit.controllers;

import java.io.IOException;

import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javafx.scene.control.TextField;

import tn.esprit.models.User;
import tn.esprit.utils.DataSource;
public class LoginController {
    User test1;
    DashboardController test;

    @FXML
        private TextField fusername;

    @FXML
        private PasswordField fpassword;

    Connection connection = DataSource.getInstance().getCnx();

    public void LoginAction(ActionEvent actionEvent) {

        try {
            String req="SELECT id,username , password ,employee_id , email FROM user WHERE username=?";
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1,fusername.getText());
            ResultSet result = statement.executeQuery();
            if(result.next())
                {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(13);
                    String password = result.getString("password");
                    int emp_id = result.getInt("employee_id");
                    if(!encoder.matches(fpassword.getText(),password))
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("Wrong Username or Password");
                            alert.showAndWait();
                        }
                    else
                        {
                            String request="SELECT emp_function FROM employees WHERE id=?";
                            PreparedStatement statement_emp=connection.prepareStatement(request);
                            statement_emp.setInt(1,emp_id);
                            ResultSet emp_result = statement_emp.executeQuery();
                            if(emp_result.next())
                                {
                                    String role = emp_result.getString("emp_function");
                                    User user= new User(result.getInt("id"),result.getString("password"),result.getString("username"),result.getString("email"),result.getInt("employee_id"));
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml"));
                                    Parent root = loader.load();
                                    DashboardController controller = loader.getController();
                                    controller.setLoginController(this, user);
                                    controller.initData();


                                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    Scene scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                }
                        }
                }
            else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("User not found");
                    alert.showAndWait();
                }
        } catch (SQLException |IOException e) {
            throw new RuntimeException(e);
        }
    }
    User getuser(){return test1;}
}
