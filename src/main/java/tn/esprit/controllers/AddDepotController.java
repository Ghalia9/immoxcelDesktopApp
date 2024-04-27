package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import tn.esprit.models.Depot;
import tn.esprit.services.ServiceDepot;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddDepotController {
    Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private TextField Location ;

    @FXML
    private TextField Adresse;

    @FXML
    private TextField Limitstock;

    private ShowDepotController depot;

    public void initDepot(ShowDepotController depotController)
    {
        this.depot=depotController;
    }

    private void refreshTable() throws SQLException, IOException {

        ObservableList<Node> children = depot.depotLayout.getChildren();
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof HBox && child.getProperties().get("controller") != null && child.getProperties().get("controller") instanceof CardDepotController) {
                nodesToRemove.add(child);
            }
        }
        depot.depotLayout.getChildren().removeAll(nodesToRemove);
        depot.loadData();
    }

    private final ServiceDepot sm = new ServiceDepot();



    public void ajouterDepotAction (javafx.event.ActionEvent actionEvent){
        try {
            if (Location.getText().isEmpty() || Adresse.getText().isEmpty() || Limitstock.getText().isEmpty()) {
                // If any of the fields are empty, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("All fields are required");
                alert.showAndWait();
                return; // Exit the method to prevent further execution
            }

            int limitStock = Integer.parseInt(Limitstock.getText()); // Parse the limit stock value
            if (limitStock <= 0) {
                // If the limit stock is not positive, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Limit Stock");
                alert.setContentText("Limit Stock must be positive");
                alert.showAndWait();
                return; // Exit the method to prevent further execution
            }

            // If all validations pass, proceed with adding the depot
            sm.ajouter(new Depot(Location.getText(), Adresse.getText(), limitStock, limitStock));
            Location.clear();
            Adresse.clear();
            Limitstock.clear();
            refreshTable();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Depot added");
            alert.show();
        } catch (NumberFormatException e) {
            // Handle the case where Limitstock is not a valid integer
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Limit Stock");
            alert.setContentText("Limit Stock must be a valid integer");
            alert.showAndWait();
        } catch (SQLException | IOException e) {
            // Handle SQL exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SQL Exception");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

}
