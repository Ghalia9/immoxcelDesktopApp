package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import tn.esprit.models.Depot;
import tn.esprit.models.Materials;
import tn.esprit.services.ServiceDepot;
import tn.esprit.services.ServiceMaterials;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateMaterialsController {
    @FXML
    private TextField QuantityUpdate;

    @FXML
    private TextField TypematerialsUpdate;

    @FXML
    private TextField UnitpriceUpdate;

    private ShowMaterialsController materialsController;

    private ShowDepotController depotController;

    private Materials materials;
    private Depot depot;
    private ServiceDepot sd= new ServiceDepot();
    private ServiceMaterials sm = new ServiceMaterials();
    public void initupdatematerials(ShowMaterialsController updatecontroller,Materials updatematerials,ShowDepotController Sdepot,Depot depots){
        this.materialsController=updatecontroller;
        this.depotController=Sdepot;
        this.materials=updatematerials;
        this.depot=depots;
    }
    public void setData(){
        this.TypematerialsUpdate.setText(materials.getTypematerials());
        this.UnitpriceUpdate.setText(String.valueOf(materials.getUnitprice()));
        this.QuantityUpdate.setText(String.valueOf(materials.getQuantity()));

    }
    private void refreshTableMaterials() throws SQLException, IOException {
        materialsController.materialsToUpdate.clear();
        materialsController.materials().clear();
        ObservableList<Node> children = materialsController.materialsLayout.getChildren();
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof HBox && child.getProperties().get("controller") != null && child.getProperties().get("controller") instanceof CardMaterialsController) {
                nodesToRemove.add(child);
            }
        }
        materialsController.materialsLayout.getChildren().removeAll(nodesToRemove);
        materialsController.loadData();
    }
    private void refreshTableDepot() throws SQLException, IOException {
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

    public void UpdateMaterials(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            String typematerialsUpdateText = TypematerialsUpdate.getText();
            String quantityUpdateText = QuantityUpdate.getText();
            String unitPriceUpdateText = UnitpriceUpdate.getText();

            if (typematerialsUpdateText.isEmpty() || quantityUpdateText.isEmpty() || unitPriceUpdateText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Type Materials, Quantity, and Unit Price must not be empty");
                alert.showAndWait();
                return;
            }

            int quantityUpdate = Integer.parseInt(quantityUpdateText);
            float unitPriceUpdate = Float.parseFloat(unitPriceUpdateText);

            if (quantityUpdate < 0 || unitPriceUpdate < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Quantity and Unit Price must be non-negative");
                alert.showAndWait();
                return;
            }

            if (materials.getQuantity() == quantityUpdate) {
                materials.setTypematerials(typematerialsUpdateText);
                materials.setUnitprice(unitPriceUpdate);
                materials.setQuantity(quantityUpdate);
                sm.modifier(materials);
                refreshTableMaterials();
                refreshTableDepot();
            } else {
                if (quantityUpdate <= depot.getStock_available()) {
                    if (quantityUpdate < materials.getQuantity()) {
                        materials.setTypematerials(typematerialsUpdateText);
                        materials.setUnitprice(unitPriceUpdate);
                        int diff = materials.getQuantity() - quantityUpdate;
                        materials.setQuantity(quantityUpdate);
                        depot.setStock_available(depot.getStock_available() + diff);
                        sm.modifier(materials);
                        sd.modifier(depot);
                        refreshTableMaterials();
                        refreshTableDepot();
                    } else {
                        materials.setTypematerials(typematerialsUpdateText);
                        materials.setUnitprice(unitPriceUpdate);
                        int diff = quantityUpdate - materials.getQuantity();
                        materials.setQuantity(quantityUpdate);
                        depot.setStock_available(depot.getStock_available() - diff);
                        sm.modifier(materials);
                        sd.modifier(depot);
                        refreshTableMaterials();
                        refreshTableDepot();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Quantity must be equal or lower than Quantity Available");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter valid numbers for Quantity and Unit Price");
            alert.showAndWait();
        }
    }
}
