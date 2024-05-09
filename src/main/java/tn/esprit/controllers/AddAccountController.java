package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.esprit.models.Employees;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.DataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.sql.Date;
//import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
public class AddAccountController {

    private String mailStatus;
    @FXML
    private VBox employeesLayout;
    List<Employees> employeesCopy=new ArrayList<>();
    Connection connection = DataSource.getInstance().getCnx();

    EmployeeCardController employeeCard;

    List<Node> originalOrder = new ArrayList<>();

    public String verifyEmail(String emailToVerify) throws IOException {
      // Replace with the email address you want to verify
        String apiKey = "4cba6e27b13603b5433ae634f9eda0e58ec9751c"; // Replace with your own API key from hunter.io

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://api.hunter.io/v2/email-verifier?email=" + emailToVerify + "&api_key=" + apiKey);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);

                // Extract relevant data
                JSONObject data = (JSONObject) jsonObject.get("data");
                String status = (String) data.get("status");
                long score = (long) data.get("score");

                // Push to dataLayer with event name emailStatus and parameters
                // You can replace this with your desired logic to handle the data
                System.out.println("Email status: " + status);
                System.out.println("Email score: " + score);
                mailStatus=status;
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return mailStatus;
    }


    public void setEmployeeCard(EmployeeCardController employeeCardController) {
        this.employeeCard=employeeCardController;
    }

    private List<Employees> employees() throws SQLException {
        List<Employees> ls=new ArrayList<>();
        String query = "SELECT * FROM Employees WHERE id NOT IN (SELECT employee_id FROM User)";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet res = statement.executeQuery();
        while (res.next())
            {
                int id = res.getInt("id");
                String empName = res.getString("emp_name");
                String empLastName = res.getString("emp_last_name");
                String empSex = res.getString("emp_sex");
                String empEmail = res.getString("emp_email");
                String empAddress = res.getString("emp_address");
                String empPhone = res.getString("emp_phone");
                String empFunction = res.getString("emp_function");
                Date birthDate = res.getDate("birth_date");
                Date hireDate = res.getDate("hire_date");
                Date endContractDate = res.getDate("end_contract_date");
                String contractType = res.getString("contract_type");
                int allowedLeaveDays = res.getInt("allowed_leave_days");
                Blob empcv = res.getBlob("empcv");
                int empTakenLeaves = res.getInt("emp_taken_leaves");
                String empCin = res.getString("emp_cin");
                Employees employee = new Employees(id, empName, empLastName, empSex, empEmail, empAddress, empPhone, empFunction, birthDate, hireDate, endContractDate, contractType, allowedLeaveDays, empcv, empTakenLeaves, empCin);
                ls.add(employee);
            }
        employeesCopy=ls;
        return ls;
    }

    @FXML
    private ComboBox<String> combobox;

    @FXML
    private ChoiceBox<String> employeeChoiceBox;

    @FXML
    public TextField fusername;

    @FXML
    public TextField email;

    @FXML
    public PasswordField password;

    @FXML
    public PasswordField confirm_password;

    private final ServiceUser su = new ServiceUser();

    private int emp_id=0;



    @FXML
    private Label employeeName;

    private String hashed_password;

    @FXML
    private TextField searchEmployee;

    final Map<String, Integer> employeeIdMap = new HashMap<>();

    private DashboardController dashboardController;

    public void setUserToAdd(int id,String fullName)
        {
            this.emp_id=id;
            employeeName.setText(fullName);

        }

    // Method to set the DashboardController
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    // Method to refresh the users table in DashboardController
    private void refreshUsersTable() {
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
            } catch (SQLException | IOException e) {
                e.printStackTrace(); // Handle the exception properly
            }
        }
    }

    private void refreshUsers() {
        if (dashboardController != null) {
            try {
                dashboardController.users_nb.setText(dashboardController.get_dashboard_stats("user"));
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception properly
            }
        }
    }
    @FXML
    public void initialize() throws SQLException, IOException {
        populateEmployeeTable();
    }

    public static boolean validateMail(String mail)
        {
            String emailRegex= "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern emailPat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
            Matcher matcher = emailPat.matcher(mail);
            return matcher.find();
        }
    private void populateEmployeeTable() throws SQLException, IOException {
        List<Employees> employees = new ArrayList<>(employees());
        for(int i=0;i<employees.size();i++)
            {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/EmployeeCard.fxml"));
                HBox hBox = fxmlLoader.load();
                EmployeeCardController card = fxmlLoader.getController();


                card.SetCard(employees.get(i));
                card.setAdd(this,employees.get(i));
                hBox.getProperties().put("controller", card);
                employeesLayout.getChildren().add(hBox);
            }
        List<Node> test = new ArrayList<>(employeesLayout.getChildren());
        originalOrder=test;
    }



    public void AddUserAction(javafx.event.ActionEvent actionEvent) throws IOException {
        try {




            String user_query= "SELECT username FROM User WHERE username=?";

            if(confirm_password.getText().isEmpty() && password.getText().isEmpty() && !validateMail(email.getText()) && email.getText().isEmpty() && fusername.getText().isEmpty() && emp_id==0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Choose a user to add");
                alert.showAndWait();
            }
            else {
                PreparedStatement userStatement = connection.prepareStatement(user_query);
                userStatement.setString(1, fusername.getText());
                ResultSet user_resultSet = userStatement.executeQuery();

                if (user_resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Username");
                    alert.setContentText("Username already taken");
                    alert.showAndWait();
                } else {

                    if (Objects.equals(password.getText(), confirm_password.getText()) && validateMail(email.getText()) && !password.getText().isEmpty() && !confirm_password.getText().isEmpty() && verifyEmail(email.getText()).equals("valid")) {
                        int customCostFactor = 13;
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(customCostFactor);
                        hashed_password = encoder.encode(password.getText());
                        verifyEmail(email.getText());
                        su.ajouter(new User(hashed_password, fusername.getText(), email.getText(), emp_id));
                        fusername.clear();
                        email.clear();
                        password.clear();
                        confirm_password.clear();
                        employeeName.setText("");

                        ObservableList<Node> children = employeesLayout.getChildren();
                        List<Node> nodesToRemove = new ArrayList<>();

// Find the specific HBox at the top and exclude it from removal
                        Node topHBox = null;
                        for (Node child : children) {
                            if (child instanceof HBox) {
                                topHBox = child;
                                break;
                            }
                        }

// Add all nodes except the top HBox to the list of nodes to remove
                        for (Node child : children) {
                            if (child != topHBox) {
                                nodesToRemove.add(child);
                            }
                        }

// Remove all nodes except the top HBox
                        children.removeAll(nodesToRemove);

                        employeesCopy.clear();
                        employees();
                        populateEmployeeTable();
                        refreshUsersTable();
                        refreshUsers();

                    } else if (!validateMail(email.getText()) || email.getText().isEmpty()) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Mail");
                        alert.setContentText("Wrong Mail Format");
                        alert.showAndWait();
                    } else if (!Objects.equals(password.getText(), confirm_password.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password");
                        alert.setContentText("Password is not matching Confirm Password");
                        alert.showAndWait();
                    } else if (password.getText().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password");
                        alert.setContentText("Password is blank");
                        alert.showAndWait();
                    } else if (confirm_password.getText().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password");
                        alert.setContentText("Confirm Password is blank");
                        alert.showAndWait();
                    }
                    else if(validateMail(email.getText()) && !verifyEmail(email.getText()).equals("valid"))
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Mail");
                            alert.setContentText("Suspicious Mail try another one");
                            alert.showAndWait();
                        }

                }
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SQL Exception");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void handleSearch() {


        String query = searchEmployee.getText().trim().toLowerCase();
        List<Node> nodesToRemove = new ArrayList<>();
        List<Node> nodesToAdd = new ArrayList<>();
        // Store the original order of user cards

        // If the query is empty, reset visibility and restore original order
        if (query.isEmpty()) {

            for (Node child : employeesLayout.getChildren()) {
                if (child instanceof HBox) {
                    child.setVisible(true);
                }
            }
            // Clear the layout and add user cards back in original order
            employeesLayout.getChildren().clear();
            employeesLayout.getChildren().addAll(originalOrder);
            return;
        }

        for (Node child : originalOrder) { // Iterate over the original order list
            if (child instanceof HBox) {
                EmployeeCardController card = (EmployeeCardController) child.getProperties().get("controller");
                if (card != null) {
                    if (card.fullName.getText().toLowerCase().contains(query)) {
                        child.setVisible(true);
                        nodesToRemove.add(child); // Remove the matching node
                        nodesToAdd.add(child); // Add it below the headers

                    } else {
                        child.setVisible(false);
                    }
                }
            }
        }

        // Perform removal and addition after the iteration
        employeesLayout.getChildren().removeAll(nodesToRemove);
        if (!nodesToAdd.isEmpty()) {
            if (employeesLayout.getChildren().size() > 1) {
                employeesLayout.getChildren().addAll(1, nodesToAdd);
            } else {
                // If the list size is 1 or less, just add nodes without specifying index
                employeesLayout.getChildren().addAll(nodesToAdd);
            }
        }
    }


}

