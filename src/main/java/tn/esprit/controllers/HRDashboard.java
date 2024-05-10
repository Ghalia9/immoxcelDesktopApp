package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;
import tn.esprit.controllers.AfficherEmployees;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class HRDashboard implements Initializable {

    @FXML
    public Pane paneToChange;

    Connection connection = DataSource.getInstance().getCnx();

    int verifyUpdateFrom=0;
    public User userConnected;
    LoginController loginController;

    @FXML
    public Text username;
    @FXML
    private HBox cardLayout;
    @FXML
    private VBox leavesLayout;
    private List<Leaves> pendingLeaves,oldleaves;

    @FXML
    private VBox employeesLayout;

    public @FXML Pane getPane(){return paneToChange;}

    public VBox getEmployeesLayout() {
        return employeesLayout;
    }

    public HBox getCardLayout() {
        return cardLayout;
    }

    public VBox getLeavesLayout() {
        return leavesLayout;
    }

    private final ServiceLeaves sl= new ServiceLeaves();
    private final ServiceEmployees se = new ServiceEmployees();

    public void setLoginController(LoginController login, User user)
    {
        this.loginController=login;
        userConnected=user;
        username.setText(userConnected.getUsername());
    }

    private int idSelectedEmployee(Employees employee){
        return employee.getId();
    }
    //button add on top of list of employees
    @FXML
    void addOnClick(ActionEvent event) throws IOException {
        createAddEmployeeForm();
        //showEmployeesList();

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //pendingLeaves=new ArrayList<>(pendingLeaves());
        //PENDING LEAVES
        showPendingLeaves();
        //ALLLEAVES

        showOldLeaves();
        //EMPLOYEESLIST
        showEmployeesList();

    }
    public void showEmployeesList(){
        Set<Employees> setemployees= se.getAll();
        List<Employees> employeesList=new ArrayList<>(setemployees);
        try{
            for (int i=0;i<employeesList.size();i++){
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/EmployeeAfficher.fxml"));
                HBox hBox=loader.load();
                AfficherEmployees afficheremployees=loader.getController();
                afficheremployees.setdashbord(this);
                afficheremployees.setCurrentEmployee(employeesList.get(i));
                afficheremployees.setData(employeesList.get(i));
                employeesLayout.getChildren().add(hBox);
                afficheremployees.employeesLayout=employeesLayout;
                // afficheremployees.delete_employee();
            }
        }catch (IOException e){
            System.out.println("aaaaaaaaaaaaaaaaaa");
            e.printStackTrace();
        }
    }
public void showPendingLeaves(){
    pendingLeaves=new ArrayList<>(pendingLeaves());
    //PENDING LEAVES
    try{
        for (int i=0;i<pendingLeaves.size();i++){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/CardLeaves.fxml"));
            HBox cardBox=loader.load();
            AfficherLeaves afficherLeaves=loader.getController();
            afficherLeaves.setdashbord(this);
            afficherLeaves.setCurrentLeave(pendingLeaves.get(i));
            afficherLeaves.setData(pendingLeaves.get(i));
            afficherLeaves.setCurrentEmployee(pendingLeaves.get(i).getEmployee());
            cardLayout.getChildren().add(cardBox);
        }

    }catch (IOException e){
        System.out.println("aaaaaaaaaaaaaaaaaa");
        e.printStackTrace();
    }
}
public void showOldLeaves(){
    oldleaves=new ArrayList<>(oldLeaves());
    try{
        for (int i=0;i<oldleaves.size();i++){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/OldLeave.fxml"));
            HBox lBox=loader.load();
            AfficherLeaves afficherLeaves=loader.getController();
            afficherLeaves.setDataALL(oldleaves.get(i));
            leavesLayout.getChildren().add(lBox);
        }

    }catch (IOException e){
        System.out.println("aaaaaaaaaaaaaaaaaa");
        e.printStackTrace();
    }
}
    private List<Leaves> pendingLeaves(){
        Set<Leaves> allLeaves=sl.getAll();
        List<Leaves> pl=new ArrayList<>();
        for (Leaves leave : allLeaves) {
            System.out.println(leave);
            //leave.setStatus("Approved");
            if (leave.getStatus().equals("Pending")) {
                pl.add(leave);
            }
        }
        System.out.println("list listinggg");
        System.out.println(allLeaves);
        System.out.println(pl);
        return pl;
    }
    private List<Leaves> oldLeaves(){
        Set<Leaves> allLeaves=sl.getAll();
        List<Leaves> oldleaves=new ArrayList<>();
        for (Leaves leave : allLeaves) {
            System.out.println(leave);
            //leave.setStatus("Approved");
            if (!leave.getStatus().equals("Pending")) {
                oldleaves.add(leave);
            }
        }
       /* System.out.println("list listinggg");
        System.out.println(allLeaves);
        System.out.println(pl);*/
        return oldleaves;
    }

    public void createAddEmployeeForm() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEmployee.fxml"));
            Parent root = loader.load();
            AddEmployee employee=loader.getController();
            employee.setdashbord(this);
            Stage addEmpStage = new Stage();
            addEmpStage.initStyle(StageStyle.DECORATED);
            addEmpStage.setScene(new Scene(root, 638, 574));
            addEmpStage.setTitle("Add Employee Form");
            addEmpStage.showAndWait();
           // showEmployeesList();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    //Selim
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
        statement.setString(1,username.getText());
        ResultSet rst=statement.executeQuery();
        if(rst.next())
        {
            verifyUpdateFrom=2;
            int id=rst.getInt("id");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            UpdateUserController updateUser = loader.getController();
            //updateUser.setEController(this,id,username.getText());
            updateUser.initializeFields();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }
    }


    public void ShowPersonalInformation(ActionEvent actionEvent) throws IOException {
        Employees employeeInfo=se.getOneById(userConnected.getEmp_id());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEmployee.fxml"));
        Parent Details = loader.load();
        DetailsEmployee employeedetails=loader.getController();
       // employeedetails.setdashbord(this);
        Stage detailsEmpStage = new Stage();
        detailsEmpStage.initStyle(StageStyle.DECORATED);
        detailsEmpStage.setScene(new Scene(Details, 638, 574));
        detailsEmpStage.setTitle("Employee Details");
        employeedetails.setDetailsData(employeeInfo);
        employeedetails.setCurrentEmployee(employeeInfo);
        detailsEmpStage.showAndWait();
    }

}
