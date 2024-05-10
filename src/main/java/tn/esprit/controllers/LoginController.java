package tn.esprit.controllers;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


import javafx.concurrent.Worker;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import javafx.stage.StageStyle;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tn.esprit.models.User;
import tn.esprit.utils.DataSource;
public class LoginController {



    // Define a WebView and WebEngine globally in your class

    public WebView recaptcha;

    public WebEngine response;


    public boolean isCaptchaSuccess = false;

    public void setCaptcha(boolean captcha)
    {
        this.isCaptchaSuccess=captcha;
    }




    private int loginAttempts = 0; // Counter for login attempts
    private boolean isBlocked = false; // Flag to indicate if the login is blocked
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // Timer scheduler
    User test1;
    DashboardController test;

    @FXML
        private TextField fusername;

    @FXML
        private PasswordField fpassword;
    @FXML
        private Button loginButton;

    Connection connection = DataSource.getInstance().getCnx();

    private void unblockLogin() {
        isBlocked = false;
        fpassword.setDisable(false);
        loginButton.setDisable(false);
    }


    private void blockLogin() {
        isBlocked = true;
        fpassword.setDisable(true);
        loginButton.setDisable(true);

        // Schedule a task to unblock login after 30 seconds
        scheduler.schedule(() -> {
            unblockLogin();
        }, 30, TimeUnit.SECONDS);
    }

    public void LoginAction(ActionEvent actionEvent) {

        if(!fusername.getText().isEmpty() && !fpassword.getText().isEmpty()) {
            if (isBlocked) {
                // If blocked, display an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Login button and password field are blocked. Please try again later.");
                alert.showAndWait();
                return;
            }

            try {



                String req = "SELECT id,username , password ,employee_id , email FROM user WHERE username=?";
                PreparedStatement statement = connection.prepareStatement(req);
                statement.setString(1, fusername.getText());
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(13);
                    String password = result.getString("password");
                    int emp_id = result.getInt("employee_id");
                    if (!encoder.matches(fpassword.getText(), password)) {
                        loginAttempts++;
                        if (loginAttempts >= 3) {
                            blockLogin();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("Too many incorrect login attempts. Please try again later.");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("Wrong Username or Password");
                            alert.showAndWait();
                        }

                    } else {



                        loginAttempts = 0;
                        String request = "SELECT emp_function FROM employees WHERE id=?";
                        PreparedStatement statement_emp = connection.prepareStatement(request);
                        statement_emp.setInt(1, emp_id);
                        ResultSet emp_result = statement_emp.executeQuery();
                        if (emp_result.next()) {
                            String role = emp_result.getString("emp_function");
                            User user = new User(result.getInt("id"), result.getString("password"), result.getString("username"), result.getString("email"), result.getInt("employee_id"));
                            switch (role) {
                                case "Admin":
                                    FXMLLoader AdminLoader = new FXMLLoader(getClass().getResource("/Admin.fxml"));
                                    Parent AdminRoot = AdminLoader.load();
                                    DashboardController AdminController = AdminLoader.getController();
                                    AdminController.setLoginController(this, user);
                                    AdminController.initData();
                                    Stage AdminStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    Scene AdminScene = new Scene(AdminRoot);
                                    AdminStage.setScene(AdminScene);
                                    AdminStage.show();
                                    break;

                                case "HR_Manager":
                                    FXMLLoader HrLoader = new FXMLLoader(getClass().getResource("/DisplayEmployees.fxml"));
                                    Parent HrRoot = HrLoader.load();
                                    //HRDashboard HrController = HrLoader.getController();
                                    DisplayEmployees HrController = HrLoader.getController();


                                    HrController.setLoginController(this,user);
                                    Stage HrStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    Scene HrScene = new Scene(HrRoot);
                                    HrStage.setScene(HrScene);
                                    HrStage.show();
                                    break;

                                case "Production_Manager":
                                    FXMLLoader ProjectLoader = new FXMLLoader(getClass().getResource("/ShowProjects.fxml"));
                                    Parent ProjectRoot = ProjectLoader.load();
                                    ProjectsDashboardController ProjectController = ProjectLoader.getController();
                                    ProjectController.setLoginController(this, user);
                                    Stage ProjectStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    Scene ProjectScene = new Scene(ProjectRoot);
                                    ProjectStage.setScene(ProjectScene);
                                    ProjectStage.show();
                                    break;

                                case "Inventory_Manager":
                                    FXMLLoader InventoryLoader = new FXMLLoader(getClass().getResource("/ShowDepots.fxml"));
                                    Parent InventoryRoot = InventoryLoader.load();
                                    ShowDepotController InventoryController = InventoryLoader.getController();
                                    InventoryController.setLoginController(this, user);
                                    InventoryController.loadData();
                                    Stage InventoryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    Scene InventoryScene = new Scene(InventoryRoot);
                                    InventoryStage.setScene(InventoryScene);
                                    InventoryStage.show();
                                    break;

                                case "Financial_Manager":
                                    FXMLLoader FinanceLoader = new FXMLLoader(getClass().getResource("/Display.fxml"));
                                    Parent FinanceRoot = FinanceLoader.load();
                                    DisplayController FinanceController = FinanceLoader.getController();
                                    FinanceController.setLoginController(this, user);
                                    Stage FinanceStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    Scene FinanceScene = new Scene(FinanceRoot);
                                    FinanceStage.setScene(FinanceScene);
                                    FinanceStage.show();
                                    break;
                            }

                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("User not found");
                    alert.showAndWait();
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    User getuser(){return test1;}

    public void ShowSendMail(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SendMailPassword.fxml"));
        Parent parent = loader.load();

        // Set the instance of DashboardController to AddAccountController
        SendUserMailController MailController = loader.getController();


        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }


}
