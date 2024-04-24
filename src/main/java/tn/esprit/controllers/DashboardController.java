package tn.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.controllers.LoginController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.DataSource;
import javafx.scene.layout.Pane;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController{

    List<Node> originalOrder = new ArrayList<>();

    private List<UserCardController>userCardControllers;

    @FXML
    private TextField search_field;

    @FXML
    public VBox UsersLayout;

    public List<User> users;
    public int verifyUpdateFrom=0;
    private LoginController loginController;
    @FXML
    public Text usernameConnected;

    @FXML
    private Text employees_nb;

    @FXML
    private Text inventory_nb;

    @FXML
    private Text projects_nb;

    @FXML
    public Text users_nb;

    public DashboardController() {
    }

    public List<User> users() throws SQLException {
        List<User> ls = new ArrayList<>();
        String query="SELECT username,employee_id FROM user WHERE username <> ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,userConnected.getUsername());
        ResultSet rst=statement.executeQuery();

        while (rst.next())
        {

            String username = rst.getString("username");
            int emp_id = rst.getInt("employee_id");
            String role = getRole(emp_id);
            String FullName = getFullName(emp_id);
            User user = new User(username,role,FullName);
            ls.add(user);

        }
        users=ls;
        return ls;
    }

    Connection connection = DataSource.getInstance().getCnx();

    public String username_to_get;
    public User userConnected;

    private final ServiceUser su = new ServiceUser();

    // Constructor to receive the user object

    public void setLoginController(LoginController login,User user)
        {
            this.loginController=login;
            userConnected=user;
            usernameConnected.setText(userConnected.getUsername());
        }
    public void setUser(User user) {
        userConnected=user;
        usernameConnected.setText(userConnected.getUsername());

    }

    public void initData() throws IOException {
        try {



            loadUsers();
            users_nb.setText(get_dashboard_stats("user"));
            employees_nb.setText(get_dashboard_stats("employees"));
            projects_nb.setText(get_dashboard_stats("project"));
            inventory_nb.setText(get_dashboard_stats("depot"));


            } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void loadUsers() throws SQLException, IOException {
        List<User> users = new ArrayList<>(users());
        System.out.println(users);
        for(int i=0;i<users.size();i++)
            {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/UserCard.fxml"));
                HBox hBox = fxmlLoader.load();
                UserCardController card = fxmlLoader.getController();

                card.initDashboard(this);
                card.SetData(users.get(i));
                hBox.getProperties().put("controller", card);
                UsersLayout.getChildren().add(hBox);


            }
        List<Node> test = new ArrayList<>(UsersLayout.getChildren());
            originalOrder=test;
    }
    public String get_dashboard_stats(String tablename) throws SQLException {
        String count = "";
        String query = "SELECT COUNT(*) AS count FROM " + tablename;
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                count = String.valueOf(resultSet.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }
        return count;
    }

    public String getRole(int emp_id) throws SQLException {
        Connection connection = DataSource.getInstance().getCnx();
        String query = "SELECT emp_function FROM employees WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,emp_id);
        ResultSet resultSet=statement.executeQuery();
        resultSet.next(); // Move the cursor to the first (and only) row
        return resultSet.getString("emp_function");
    }

    public String getFullName(int emp_id) throws SQLException {
        Connection connection = DataSource.getInstance().getCnx();
        String query = "SELECT emp_name,emp_last_name FROM employees WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,emp_id);
        ResultSet resultSet=statement.executeQuery();
        resultSet.next(); // Move the cursor to the first (and only) row
        return resultSet.getString("emp_name") + " " + resultSet.getString("emp_last_name");
    }


    public void showAddUser(MouseEvent mouseEvent) throws IOException {
        int users=Integer.parseInt(users_nb.getText());
        int employees=Integer.parseInt(employees_nb.getText());
        if(users == employees)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Users");
                alert.setContentText("There is no users to add");
                alert.showAndWait();
            }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUserAccount.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            AddAccountController addAccountController = loader.getController();
            addAccountController.setDashboardController(this);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }
    }


    public void logout(ActionEvent actionEvent) throws IOException {
        userConnected=null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }





    public void showSelfUpdate(ActionEvent actionEvent) throws SQLException, IOException {
        String query = "SELECT id FROM user WHERE username=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,usernameConnected.getText());
        ResultSet rst=statement.executeQuery();
        if(rst.next())
        {
            verifyUpdateFrom=2;
            int id=rst.getInt("id");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            UpdateUserController updateUser = loader.getController();
            updateUser.setDashboardController(this,id,usernameConnected.getText());
            updateUser.initializeFields();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }
    }

    public void handleSearch() {


        String query = search_field.getText().trim().toLowerCase();
        List<Node> nodesToRemove = new ArrayList<>();
        List<Node> nodesToAdd = new ArrayList<>();
         // Store the original order of user cards

        // If the query is empty, reset visibility and restore original order
        if (query.isEmpty()) {

            for (Node child : UsersLayout.getChildren()) {
                if (child instanceof HBox) {
                    child.setVisible(true);
                }
            }
            // Clear the layout and add user cards back in original order
            UsersLayout.getChildren().clear();
            UsersLayout.getChildren().addAll(originalOrder);
            return;
        }

        for (Node child : originalOrder) { // Iterate over the original order list
            if (child instanceof HBox) {
                UserCardController card = (UserCardController) child.getProperties().get("controller");
                if (card != null) {
                    if (card.username.getText().toLowerCase().contains(query)) {
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
        UsersLayout.getChildren().removeAll(nodesToRemove);
        if (!nodesToAdd.isEmpty()) {
            if (UsersLayout.getChildren().size() > 1) {
                UsersLayout.getChildren().addAll(1, nodesToAdd);
            } else {
                // If the list size is 1 or less, just add nodes without specifying index
                UsersLayout.getChildren().addAll(nodesToAdd);
            }
        }
    }
}
