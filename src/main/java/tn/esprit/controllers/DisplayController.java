package tn.esprit.controllers;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import tn.esprit.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceSupplier;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayController implements Initializable {

    @FXML
    public Pane paneToChange;

    @FXML
    public Text username;

    @FXML
    private GridPane supplierContainer;

    @FXML
    private TextField text_search;

    @FXML
    private Button CancelButtonn;
    private final ServiceSupplier sp = new ServiceSupplier();

    private UpdateSupplierController updateSupplierController;

    private SupplierAddController supplierAddController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAllSuppliers();
    }

    public void setUpdateSupplierController(UpdateSupplierController controller)
    {
        this.updateSupplierController=controller;
    }
    public void setSupplierAddController(SupplierAddController controller)
    {
        this.supplierAddController=controller;
    }
    public void refrechData(){
        supplierContainer.getChildren().clear();
        List<Supplier> recentlyAddedSupplier = recentlyAddedSuppliers();
        for (Supplier supplier : recentlyAddedSupplier) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SupplierContainer.fxml"));
            try {
                Pane pane = fxmlLoader.load();
                SupplierContainerController controller = fxmlLoader.getController();
                controller.setData(supplier);
                supplierContainer.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // search
    public void search(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER)
        {
            supplierContainer.getChildren().clear();
            List<Supplier> filteredSuppliers = filterSuppliers(text_search.getText());
            displaySuppliers(filteredSuppliers);
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            text_search.clear();
            List<Supplier> recentlyAddedSupplier = recentlyAddedSuppliers();
            displaySuppliers(recentlyAddedSupplier);
        }
    }

    private List<Supplier> filterSuppliers(String searchQuery) {
        return recentlyAddedSuppliers().stream()
                .filter(supplier -> supplierContainsSearchWords(supplier, searchQuery))
                .collect(Collectors.toList());
    }
    // searching for the word inside the list and returning it
    private boolean supplierContainsSearchWords(Supplier supplier, String searchQuery) {
        String supplierText = supplierToString(supplier).toLowerCase();
        return supplierText.contains(searchQuery.toLowerCase());
    }
    // on what are you searching
    private String supplierToString(Supplier supplier) {
        return supplier.getCompany_name() + supplier.getAddress() + supplier.getPatent_ref() +
                supplier.getPhone_number() + supplier.getMaterails_s();
    }
    // we put the list of pane here
    public void showAllSuppliers() {
        displaySuppliers(recentlyAddedSuppliers());

    }
    // displaying  data in the gridPane with verfication empty or not
    private void displaySuppliers(List<Supplier> suppliers) {
        if (suppliers.isEmpty()) {
            Label noDataLabel = new Label("No data found.");
            supplierContainer.getChildren().add(noDataLabel);
        } else {
            for (int i = 0; i < suppliers.size(); i++) {
                int column = i % 3;
                int row = i / 3 + 1;
                Pane pane = createSupplierPane(suppliers.get(i));
                supplierContainer.add(pane, column, row);
                GridPane.setMargin(pane, new Insets(20));
            }
        }
    }

    private Pane createSupplierPane(Supplier supplier) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
            Pane cardBox = fxmlLoader.load();
            SupplierContainerController cardController = fxmlLoader.getController();
            cardController.setData(supplier);
            return cardBox;
        } catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    private List<Supplier> recentlyAddedSuppliers() {
        Set<Supplier> allSuppliers = sp.getAll();
        return List.copyOf(allSuppliers);
    }

    public void menuGO(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dispa.fxml"));
            Parent root = loader.load();
            Display2Controller display2Controller = loader.getController();
            display2Controller.setLoginController(loginController,userConnected);
            text_search.getScene().setRoot(root);
        } catch (IOException e) {
            displayErrorAlert("Error loading Dispa.fxml");
        }
    }

    public void newSupplierPopUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SupplierAdd.fxml"));
            showPopUpforADD(root);
        } catch (IOException e) {
            displayErrorAlert("Error loading SupplierAdd.fxml");
        }
    }
    private void showPopUpforADD(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
    public void listSuppliersGO(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Display.fxml"));
            Parent root = loader.load();
            DisplayController displayController = loader.getController();
            displayController.setLoginController(loginController,userConnected);
             text_search.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading Display.fxml");
            alert.showAndWait();
        }
    }
    public void  setCancelButtonIDActionOn(ActionEvent event) {
        Stage stage = (Stage) CancelButtonn.getScene().getWindow();
        stage.close();
    }

    private void showPopUp(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    } public void refreshTransactionDisplay(KeyEvent event) {
        if(event.getCode()== KeyCode.F) {
            supplierContainer.getChildren().clear();
            List<Supplier> recentlyAddedSupplier = recentlyAddedSuppliers();
            displaySuppliers(recentlyAddedSupplier);
        }
    }

    private void displayErrorAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Selim
    private LoginController loginController;
    public User userConnected;

    public void setLoginController(LoginController loginController, User user) {
        this.loginController=loginController;
        this.userConnected=user;
        this.username.setText(userConnected.getUsername());
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

    public int verifyUpdateFrom=0;
    public void showSelfUpdate(ActionEvent actionEvent) throws SQLException, IOException {

            verifyUpdateFrom=2;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            UpdateUserController updateUser = loader.getController();
            updateUser.setSupplierContorller(this,userConnected.getId(),username.getText());
            updateUser.initializeFields();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

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
