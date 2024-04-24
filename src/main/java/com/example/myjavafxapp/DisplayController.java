package com.example.myjavafxapp;

import Entities.Supplier;
import Services.ServiceSupplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayController implements Initializable {

    @FXML
    private GridPane supplierContainer;

    @FXML
    private TextField text_search;

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SupplierContainer.fxml"));
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
    public void search(ActionEvent event) {
        supplierContainer.getChildren().clear();
        List<Supplier> filteredSuppliers = filterSuppliers(text_search.getText());
        displaySuppliers(filteredSuppliers);
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
    private void showAllSuppliers() {
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dispa.fxml"));
            Parent root = loader.load();
            Display2Controller display2Controller = loader.getController();
            text_search.getScene().setRoot(root);
        } catch (IOException e) {
            displayErrorAlert("Error loading Dispa.fxml");
        }
    }

    public void newSupplierPopUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SupplierAdd.fxml"));
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


    private void showPopUp(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
