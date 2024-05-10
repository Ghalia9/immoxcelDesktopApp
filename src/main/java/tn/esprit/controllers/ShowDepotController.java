package tn.esprit.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Depot;
import tn.esprit.models.Employees;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.utils.DataSource;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ShowDepotController {

    @FXML
    public Pane paneToChange;

    private LoginController loginController;
    public User userConnected;

    @FXML
    public Text username;
public class ShowDepotController implements Initializable {

    List<Node> originalOrder = new ArrayList<>();
    Connection cnx = DataSource.getInstance().getCnx();

    public List<Depot> depotToUpdate= new ArrayList<>();
    @FXML
    public VBox depotLayout;

    @FXML
    private TableView<ObservableList<String>> usersTable;

    public void setLoginController(LoginController loginController, User user) {
        this.loginController=loginController;
        this.userConnected=user;
        username.setText(userConnected.getUsername());
        System.out.println(userConnected.getUsername());

    }

    public List<Depot> depots() throws SQLException {
        List<Depot> ls = new ArrayList<>();
        String req="SELECT id,location,adresse,limit_stock,quantity_available FROM Depot";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rst=ps.executeQuery();
        while (rst.next())
        {
            int id = rst.getInt("id");
            String location = rst.getString("location");
            String adresse = rst.getString("adresse");
            int limit = rst.getInt("limit_stock");
            int available = rst.getInt("quantity_available");
            Depot depot=new Depot(id,location,adresse,limit,available);
            ls.add(depot);
        }
        depotToUpdate=ls;
        return ls;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadData() throws SQLException, IOException {
        System.out.println(userConnected);
        List<Depot> depots = new ArrayList<>(depots());
        for (int i=0; i<depots.size();i++)
        {

            FXMLLoader fxmlLoader=new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/CardDepot.fxml"));
            HBox hBox = fxmlLoader.load();

            CardDepotController cdc= fxmlLoader.getController();
            cdc.initDepot(this,depots.get(i));
            hBox.getProperties().put("controller",cdc);
            cdc.setData(depots.get(i));
            depotLayout.getChildren().add(hBox);

        }

        List<Node> test = new ArrayList<>(depotLayout.getChildren());
        originalOrder=test;

    }
    public void showAddDepot(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDepot.fxml"));
        Parent parent=loader.load();
        AddDepotController addDepot = loader.getController();
        addDepot.initDepot(this);
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
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

    public int verifyUpdateFrom=0;

    public void showSelfUpdate(ActionEvent actionEvent) throws SQLException, IOException {
        String query = "SELECT id FROM user WHERE username=?";
        PreparedStatement statement = cnx.prepareStatement(query);
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
            updateUser.setInventoryController(this,id,username.getText());
            updateUser.initializeFields();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }
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



    @FXML
    private TextField search_field;
    public void handleSearch() {


        String query = search_field.getText().trim().toLowerCase();
        List<Node> nodesToRemove = new ArrayList<>();
        List<Node> nodesToAdd = new ArrayList<>();
        // Store the original order of user cards

        // If the query is empty, reset visibility and restore original order
        if (query.isEmpty()) {

            for (Node child : depotLayout.getChildren()) {
                if (child instanceof HBox) {
                    child.setVisible(true);
                }
            }
            // Clear the layout and add user cards back in original order
            depotLayout.getChildren().clear();
            //original order of cards
            depotLayout.getChildren().addAll(originalOrder);
            return;
        }

        for (Node child : originalOrder) { // Iterate over the original order list
            if (child instanceof HBox) {
                CardDepotController card = (CardDepotController) child.getProperties().get("controller");
                if (card != null) {
                    if (card.Depotlocation.getText().toLowerCase().contains(query)) {
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
        depotLayout.getChildren().removeAll(nodesToRemove);
        if (!nodesToAdd.isEmpty()) {
            if (depotLayout.getChildren().size() > 1) {
                depotLayout.getChildren().addAll(1, nodesToAdd);
            } else {
                // If the list size is 1 or less, just add nodes without specifying index
                depotLayout.getChildren().addAll(nodesToAdd);
            }
        }
    }


    public void sortOnClick(MouseEvent actionEvent) {
        depotLayout.getChildren().clear();
        depotToUpdate.stream()
                .sorted(Comparator.comparing(Depot::getLimit_stock))
                .forEach(depot -> {
                    System.out.println(depot);
                    FXMLLoader fxmlLoader=new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/CardDepot.fxml"));
                    HBox hBox = null;
                    try {
                        hBox = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    CardDepotController cdc= fxmlLoader.getController();
                    cdc.initDepot(this,depot);
                    hBox.getProperties().put("controller",cdc);
                    cdc.setData(depot);
                    depotLayout.getChildren().add(hBox);
                });
    }
}
