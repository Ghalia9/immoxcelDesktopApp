package tn.esprit.controllers;

import javafx.scene.layout.AnchorPane;

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
import tn.esprit.models.Employees;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployees;
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

    private DashboardController dashboard=this;

    private Display2Controller transaction;

    private DisplayController supplier;
    private DisplayEmployees HR;
    private DisplayEmployees de;

    private ProjectsDashboardController projects;

    private ShowDepotController depot;

    @FXML
    public Pane paneToChange;

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



            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            UpdateUserController updateUser = loader.getController();
            updateUser.setLoginController(loginController);
        if(transaction!=null)
        {
            System.out.println("transaction");
            transaction.verifyUpdateFrom=2;
            updateUser.setTransactionContorller(transaction,userConnected.getId(),usernameConnected.getText());
            updateUser.initializeFields();
        }

            else if(supplier!=null)
            {
                System.out.println("supplier");
                supplier.verifyUpdateFrom=2;
                updateUser.setSupplierContorller(supplier,userConnected.getId(),usernameConnected.getText());
                updateUser.initializeFields();
            }

             else if(HR!=null)
            {
                System.out.println("hr");
                HR.verifyUpdateFrom=2;
                updateUser.setDisplayEmployeesController(HR,userConnected.getId(),usernameConnected.getText());
                updateUser.initializeFields();
            }
            else if(projects!=null)
                {
                    System.out.println("project");
                    projects.verifyUpdateFrom=2;
                    updateUser.setProjectController(projects,userConnected.getId(),usernameConnected.getText());
                    updateUser.initializeFields();
                }
            else if(depot!=null)
                {
                    System.out.println("depot");
                    depot.verifyUpdateFrom=2;
                    updateUser.setInventoryController(depot,userConnected.getId(),usernameConnected.getText());
                    updateUser.initializeFields();
                }

        verifyUpdateFrom=2;
        updateUser.setDashboardController(this, userConnected.getId(), usernameConnected.getText());
        updateUser.initializeFields();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();


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

    public void ShowEmployees(ActionEvent actionEvent) throws IOException {
        FXMLLoader HrLoader = new FXMLLoader(getClass().getResource("/DisplayEmployees.fxml"));
        Parent HrRoot = HrLoader.load();
        //HRDashboard HrController = HrLoader.getController();
        DisplayEmployees HrController = HrLoader.getController();


        HrController.setLoginController(loginController,userConnected);
        HrController.setDashboard(this);
        this.HR=HrController;

        if (HR != null) {
            // Find the side_bar node by its fx:id

            this.paneToChange.getChildren().setAll(HR.paneToChange.getChildren());
        } else {
            System.out.println("Pane or controller is null");
        }
    }

    public void ShowLeaves(DisplayLeaves leaves)
    {
        if(leaves!=null){
            System.out.println("hi");
        this.paneToChange.getChildren().setAll(leaves.paneToChange.getChildren());}
    }
    public void ShowDashboard(ActionEvent actionEvent) throws IOException {
        HR=null;
        projects=null;
        depot=null;
        supplier=null;
        transaction=null;
        verifyUpdateFrom=2;
        FXMLLoader DashboardLoader = new FXMLLoader(getClass().getResource("/Admin.fxml"));
        Parent DashboardRoot = DashboardLoader.load();
        DashboardController DashboardController = DashboardLoader.getController();
        DashboardController.setLoginController(loginController,userConnected);
        System.out.println(userConnected.getUsername());
        DashboardController.initData();
        dashboard=DashboardController;
        this.paneToChange.getChildren().setAll(DashboardController.paneToChange.getChildren());
    }
    public void ShowTasks(ShowTasksController tasks)
    {
        if(tasks!=null){
            System.out.println("hi");
            this.paneToChange.getChildren().setAll(tasks.paneToChange.getChildren());}
    }
    public void ShowProject(ActionEvent actionEvent) throws IOException {
        HR=null;
        dashboard=null;
        depot=null;
        supplier=null;
        transaction=null;
        FXMLLoader ProjectLoader = new FXMLLoader(getClass().getResource("/ShowProjects.fxml"));
        Parent ProjectRoot = ProjectLoader.load();
        ProjectsDashboardController ProjectController = ProjectLoader.getController();
        ProjectController.setLoginController(loginController, userConnected);
        projects=ProjectController;
        ProjectController.setDashboard(this);


        this.paneToChange.getChildren().setAll(ProjectController.paneToChange.getChildren());
    }

    public void ShowCalender(ActionEvent actionEvent) throws IOException {
        HR=null;
        dashboard=null;
        depot=null;
        supplier=null;
        transaction=null;
        FXMLLoader ProjectLoader = new FXMLLoader(getClass().getResource("/GoogleCalendarEvents.fxml"));
        Parent ProjectRoot = ProjectLoader.load();
        GoogleCalendarEventsController ProjectController = ProjectLoader.getController();
        ProjectController.setLoginController(loginController, userConnected);
        this.paneToChange.getChildren().setAll(ProjectController.paneToChange.getChildren());
    }
    public void ShowDepot(ActionEvent actionEvent) throws IOException, SQLException {
        HR=null;
        projects=null;
        dashboard=null;
        supplier=null;
        transaction=null;
        FXMLLoader InventoryLoader = new FXMLLoader(getClass().getResource("/ShowDepots.fxml"));
        Parent InventoryRoot = InventoryLoader.load();
        ShowDepotController InventoryController = InventoryLoader.getController();
        InventoryController.setLoginController(loginController, userConnected);
        InventoryController.loadData();
        depot=InventoryController;



        this.paneToChange.getChildren().setAll(InventoryController.paneToChange.getChildren());
    }

    public void ShowTransaction(ActionEvent actionEvent) throws IOException, SQLException {
        HR=null;
        projects=null;
        depot=null;
        supplier=null;
        dashboard=null;
        FXMLLoader Transactionloader = new FXMLLoader(getClass().getResource("/Dispa.fxml"));
        Parent root = Transactionloader.load();
        Display2Controller TransactionController = Transactionloader.getController();
        TransactionController.setLoginController(loginController,userConnected);
        transaction=TransactionController;



        this.paneToChange.getChildren().setAll(TransactionController.paneToChange.getChildren());
    }
    public void ShowSupplier(ActionEvent actionEvent) throws IOException, SQLException {
        HR=null;
        projects=null;
        depot=null;
        dashboard=null;
        transaction=null;
        FXMLLoader SupplierLoader = new FXMLLoader(getClass().getResource("/Display.fxml"));
        Parent FinanceRoot = SupplierLoader.load();
        DisplayController SupplierController = SupplierLoader.getController();
        SupplierController.setLoginController(loginController, userConnected);
        supplier=SupplierController;


        this.paneToChange.getChildren().setAll(SupplierController.paneToChange.getChildren());
    }

    private ServiceEmployees se=new ServiceEmployees();
    public void ShowPersonalInformation(ActionEvent actionEvent) throws IOException {
        FXMLLoader HrLoader = new FXMLLoader(getClass().getResource("/DisplayEmployees.fxml"));
        Parent HrRoot = HrLoader.load();
        //HRDashboard HrController = HrLoader.getController();
        DisplayEmployees HrController = HrLoader.getController();


        HrController.setLoginController(loginController,userConnected);
        Employees employeeInfo=se.getOneById(userConnected.getEmp_id());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEmployee.fxml"));
        Parent Details = loader.load();
        DetailsEmployee employeedetails=loader.getController();
        employeedetails.setdashbord(HrController);
        Stage detailsEmpStage = new Stage();
        detailsEmpStage.initStyle(StageStyle.DECORATED);
        detailsEmpStage.setScene(new Scene(Details, 638, 574));
        detailsEmpStage.setTitle("Employee Details");
        employeedetails.setDetailsData(employeeInfo);
        employeedetails.setCurrentEmployee(employeeInfo);
        detailsEmpStage.showAndWait();
    }
}
