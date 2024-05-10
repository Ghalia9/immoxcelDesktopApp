package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Capital;
import tn.esprit.models.Transaction;
import tn.esprit.services.ServiceSupplier;
import tn.esprit.services.ServiceTransaction;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayArchivedTrController implements Initializable {
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
    private BarChart<String, Number> homeChart;
    @FXML
    private ImageView imageView;

    @FXML
    private TextField text_search;

    private final ServiceSupplier sp = new ServiceSupplier();

    private final ServiceTransaction sptrans = new ServiceTransaction();
    Connection cnx = DataSource.getInstance().getCnx();
    Capital capital= new Capital();
    public  void search(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER) {
            cardLayout.getChildren().clear();
            // cardLayout.getChildren().addAll(SearchList(text_search.getText(), recentlyAdded()));*/
            String searchQuery = text_search.getText().toLowerCase().trim();
            List<Transaction> filteredTransactions = filterTransactions(searchQuery);
            if (filteredTransactions.isEmpty()) {

                cardLayout.getChildren().clear();
                Label noDataLabel = new Label("No data found.");
                cardLayout.getChildren().add(noDataLabel);
            } else {
                // Display the filtered transactions
                cardLayout.getChildren().clear();
                cardLayout.getChildren().addAll(createCardBoxesForTransactions(filteredTransactions));
            }
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
            fxmlLoader.setLocation(getClass().getResource("/Card.fxml"));
            HBox cardBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setData(transaction);
            return cardBox;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardLayout.getChildren().clear();
        try {
            // displaying the data of capital entity
            Capital capital = sptrans.retrieveCurrentCapitalFromDatabase();
            salaryLabel.setText(String.valueOf(capital.getSalary()));
            expensesLabel.setText(String.valueOf(capital.getExepenses()));
            profitsLabel.setText(String.valueOf(capital.getProfits()));
            fundsTextField.setText(String.valueOf(capital.getBig_capital()));
            List<Transaction> recentlyAdded = recentlyAdded();

            for (Transaction transaction : recentlyAdded) {
                System.out.println(transaction.getId());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Card.fxml"));
                HBox cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
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
        Set<Transaction> allTransactions = sp.getAllArchived();
        // Convert Set to List for easier manipulation
        transactions.addAll(allTransactions);
        return transactions;
    }

    @FXML
    void setCancelButtonIDAction(ActionEvent event) {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    public void menuGO(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dispa.fxml"));
            Parent root = loader.load();
            Display2Controller display2Controller = loader.getController();
            text_search.getScene().setRoot(root);
        } catch (IOException e) {
            displayErrorAlert("Error loading Dispa.fxml");
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
    public void homeChart(){
        homeChart.getData().clear();
        System.out.println("i am here ");
        String query = "SELECT salary, expenses, profits FROM capital WHERE id=1"; // Selecting only salary, expenses, and profits
        try (Statement statement = cnx.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            XYChart.Series<String, Number> series = new XYChart.Series<>(); // Specify the data type for X and Y axes
            if (resultSet.next()) {
                System.out.println("i am here ");
                System.out.println(resultSet.getFloat("salary"));
                series.getData().add(new XYChart.Data<>("Salary", resultSet.getFloat("salary"))); // Adding salary data to the chart
                series.getData().add(new XYChart.Data<>("Expenses", resultSet.getFloat("expenses"))); // Adding expenses data to the chart
                series.getData().add(new XYChart.Data<>("Profits", resultSet.getFloat("profits"))); // Adding profits data to the chart
            }
            homeChart.getData().add(series); // Adding series to the chart
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void displayErrorAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
