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
            if (Integer.parseInt(Limitstock.getText())>0  &&  Location.getText().length()>0  &&  Adresse.getText().length()>0) {
                sm.ajouter(new Depot(Location.getText(),Adresse.getText(), Integer.parseInt(Limitstock.getText()), Integer.parseInt(Limitstock.getText())));
                Location.clear();
                Adresse.clear();
                Limitstock.clear();
                refreshTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Depot added");
                alert.show();
            } else if (Integer.parseInt(Limitstock.getText())<0 || Limitstock.getText().length()==0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Limit Stock");
                alert.setContentText("Limit Stock must be positive");
                alert.showAndWait();
            }else if (Location.getText().length()==0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Location");
                alert.setContentText("Location can't be blank");
                alert.showAndWait();
            }else if (Adresse.getText().length()==0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adresse");
                alert.setContentText("Adresse can't be blank");
                alert.showAndWait();
            }
        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SQL Exception");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

}
