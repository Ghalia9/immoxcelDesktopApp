package tn.esprit.controllers;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import tn.esprit.models.Employees;
import tn.esprit.models.Transaction;
import tn.esprit.models.Capital;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceSupplier;
import tn.esprit.services.ServiceTransaction;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class Display2Controller implements Initializable {

    @FXML
    public Pane paneToChange;
    public Text username;

    @FXML
    private Button CancelButton;

    @FXML
    private VBox cardLayout;
    @FXML
    private Label typeylabel;

    @FXML
    private Label salaryLabel;
    @FXML
    private Label expensesLabel;

    @FXML
    private Label profitsLabel;

    @FXML
    private Button newAddbutton1;
    @FXML
    private Label fundsTextField;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField text_search;

    @FXML
    void setCancelButtonIDAction(ActionEvent event) {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    private final ServiceSupplier sp = new ServiceSupplier();

    private final ServiceTransaction sptrans = new ServiceTransaction();

    public  void search(ActionEvent event) {
        cardLayout.getChildren().clear();
       // cardLayout.getChildren().addAll(SearchList(text_search.getText(), recentlyAdded()));*/
        String searchQuery = text_search.getText().toLowerCase().trim();
        List<Transaction> filteredTransactions = filterTransactions(searchQuery);
        if (filteredTransactions.isEmpty()) {
            // Display a message when no data is found
            cardLayout.getChildren().clear();
            Label noDataLabel = new Label("No data found.");
            cardLayout.getChildren().add(noDataLabel);
        } else {
            // Display the filtered transactions
            cardLayout.getChildren().clear();
            cardLayout.getChildren().addAll(createCardBoxesForTransactions(filteredTransactions));
        }
    }
    private boolean transactionContainsSearchWords(Transaction transaction, String searchQuery) {
        String transactionText = transactionToString(transaction).toLowerCase();
        return transactionText.contains(searchQuery);
    }
    private List<Transaction> filterTransactions(String searchQuery) {
        return recentlyAdded().stream()
                .filter(transaction -> transactionContainsSearchWords(transaction, searchQuery))
                .collect(Collectors.toList());
    }

    private List<HBox> createCardBoxesForTransactions(List<Transaction> transactions) {

        return transactions.stream()
                .map(this::createCardBoxForTransaction)
                .collect(Collectors.toList());
    }
    private List<HBox> SearchList(String searchWords, List<Transaction> listofTransactions) {
        return listofTransactions.stream()
                .filter(transaction -> transactionContainsSearchWords(transaction, searchWords))
                .map(this::createCardBoxForTransaction)
                .collect(Collectors.toList());
    }
   /* private boolean transactionContainsSearchWords(Transaction transaction, String searchWords) {
        String[] searchWordsArray = searchWords.toLowerCase().trim().split("\\s+");
        String transactionText = transactionToString(transaction).toLowerCase();
        return Arrays.stream(searchWordsArray).allMatch(transactionText::contains);
    }*/
    private String transactionToString(Transaction transaction) {
        // Implement this method based on how you want to represent a transaction as a string
        return transaction.getType() + transaction.getDescription() + transaction.getTotalamount() + transaction.getCost();
    }
    private HBox createCardBoxForTransaction(Transaction transaction) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Table.fxml"));
            HBox cardBox = fxmlLoader.load();
            TableController cardController = fxmlLoader.getController();
            cardController.setData(transaction);
            return cardBox;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search(null); // Initially display all transactions
        cardLayout.getChildren().clear();
        try {
            // displaying the data of capital entity
            Capital capital = sptrans.retrieveCurrentCapitalFromDatabase();
            salaryLabel.setText(String.valueOf(capital.getSalary()));
            expensesLabel.setText(String.valueOf(capital.getExepenses()));
            profitsLabel.setText(String.valueOf(capital.getProfits()));
            fundsTextField.setText(String.valueOf(capital.getBig_capital()));

            // here displaying the data of the transaction
            //
            // getting the list of all transactions
            List<Transaction> recentlyAdded = recentlyAdded();

            for (Transaction transaction : recentlyAdded) {
                System.out.println(transaction.getId());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Table.fxml"));
                HBox cardBox = fxmlLoader.load();
                TableController cardController = fxmlLoader.getController();
                cardController.setData(transaction);
                cardLayout.getChildren().add(cardBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<Transaction> recentlyAdded() {
        List<Transaction> transactions = new ArrayList<>();
        ServiceTransaction sp = new ServiceTransaction();
        // Retrieve all transactions from the database
        Set<Transaction> allTransactions = sp.getAll();
        // Convert Set to List for easier manipulation
        transactions.addAll(allTransactions);
        return transactions;
    }
    public void swtichToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Dispa.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading SupplierAdd.fxml");
            alert.showAndWait();
        }
    }
    public void listSuppliersGO(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Display.fxml"));
            Parent root = loader.load();
            DisplayController displayController = loader.getController();
            typeylabel.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading Display.fxml");
            alert.showAndWait();
        }
    }
    public void newTransactionPopUp(ActionEvent event){try {

        Parent root = FXMLLoader.load(getClass().getResource("/TransactionAdd.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle ( StageStyle.UTILITY);
        stage.show();
    } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Error loading TransactionAdd.fxml");
        alert.showAndWait();
    }
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
        updateUser.setTransactionContorller(this,userConnected.getId(),username.getText());
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
