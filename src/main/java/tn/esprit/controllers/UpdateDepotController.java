package tn.esprit.controllers;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tn.esprit.models.Depot;
import tn.esprit.services.ServiceDepot;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateDepotController  implements Initializable {
    @FXML
    private TextField AdresseUpdate;

    @FXML
    private TextField LimitstockUpdate;


    @FXML
    private ComboBox<String> locationComboBox;

    private ShowDepotController depotController;

    private Depot depot ;
    private ServiceDepot sd= new ServiceDepot();

    public void initupdatedepot (ShowDepotController controller, Depot depotupdate){
        this.depotController=controller;
        this.depot=depotupdate;
    }

    public void setdata(){
        this.locationComboBox.setValue(depot.getLocation());
        this.AdresseUpdate.setText(depot.getAdresse());
        this.LimitstockUpdate.setText(String.valueOf(depot.getLimit_stock()));
    }

    private void refreshTable() throws SQLException, IOException {
        depotController.depotToUpdate.clear();
        ObservableList<Node> children = depotController.depotLayout.getChildren();
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof HBox && child.getProperties().get("controller") != null && child.getProperties().get("controller") instanceof CardDepotController) {
                nodesToRemove.add(child);
            }
        }
        depotController.depotLayout.getChildren().removeAll(nodesToRemove);
        depotController.loadData();
    }

    public void UpdateDepot(ActionEvent actionEvent) throws SQLException, IOException {
        if(locationComboBox.getValue().isEmpty() || AdresseUpdate.getText().isEmpty() || LimitstockUpdate.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("All fields are required");
            alert.showAndWait();
            return;
        }
        else {
            int LimitStock = Integer.parseInt(LimitstockUpdate.getText());
            if(LimitStock < 0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Limit Stock must be positive");
                alert.showAndWait();
                return;
            }
            else {
                if (depot.getStock_available() == depot.getLimit_stock()) {
                    depot.setLocation(locationComboBox.getValue());
                    depot.setAdresse(AdresseUpdate.getText());
                    depot.setLimit_stock(Integer.parseInt(LimitstockUpdate.getText()));
                    depot.setStock_available(Integer.parseInt(LimitstockUpdate.getText()));
                    sd.modifier(depot);
                    refreshTable();

                } else {
                    int materialsQuantity = depot.getLimit_stock() - depot.getStock_available();
                    if (Integer.parseInt(LimitstockUpdate.getText()) < materialsQuantity) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText("Limit Stock must be greater or equal to the materials quantity");
                        alert.showAndWait();
                    } else {
                        int quantityToadd = Integer.parseInt(LimitstockUpdate.getText()) - depot.getLimit_stock();
                        depot.setLocation(locationComboBox.getValue());
                        depot.setAdresse(AdresseUpdate.getText());
                        depot.setLimit_stock(Integer.parseInt(LimitstockUpdate.getText()));
                        depot.setStock_available(depot.getStock_available() + quantityToadd);
                        sd.modifier(depot);
                        refreshTable();
                    }


                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\MSI\\Desktop\\immoxcel-java\\immoxcelDesktopApp\\src\\main\\resources\\regionTunis.json")) {
            // Parse the JSON file
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;

            // Extract country names from JSON and add them to a list
            List<String> locationName = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String Region = (String) jsonObject.get("location");
                locationName.add(Region);
            }
            System.out.println("Country Names: " + locationName);

            // Populate the ComboBox with country names
            locationComboBox.getItems().addAll(locationName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
