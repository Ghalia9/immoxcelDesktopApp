package tn.esprit.controllers;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import tn.esprit.models.Depot;
import tn.esprit.services.ServiceDepot;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateDepotController {
    @FXML
    private TextField AdresseUpdate;

    @FXML
    private TextField LimitstockUpdate;

    @FXML
    private TextField LocationUpdate;

    private ShowDepotController depotController;

    private Depot depot ;
    private ServiceDepot sd= new ServiceDepot();

    public void initupdatedepot (ShowDepotController controller, Depot depotupdate){
        this.depotController=controller;
        this.depot=depotupdate;
    }

    public void setdata(){
        this.LocationUpdate.setText(depot.getLocation());
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
        if(depot.getStock_available()==depot.getLimit_stock()){
           depot.setLocation(LocationUpdate.getText());
           depot.setAdresse(AdresseUpdate.getText());
           depot.setLimit_stock(Integer.parseInt(LimitstockUpdate.getText()));
           depot.setStock_available(Integer.parseInt(LimitstockUpdate.getText()));
           sd.modifier(depot);
           refreshTable();

        }else {
            int materialsQuantity=depot.getLimit_stock()-depot.getStock_available();
            if(Integer.parseInt(LimitstockUpdate.getText())<materialsQuantity){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Limit Stock must be greater or equal to the materials quantity");
                alert.showAndWait();
            }else {
                int quantityToadd=Integer.parseInt(LimitstockUpdate.getText())-depot.getLimit_stock();
                depot.setLocation(LocationUpdate.getText());
                depot.setAdresse(AdresseUpdate.getText());
                depot.setLimit_stock(Integer.parseInt(LimitstockUpdate.getText()));
                depot.setStock_available(depot.getStock_available()+quantityToadd);
                sd.modifier(depot);
                refreshTable();
            }


        }
    }
}
