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
        if (materials.getQuantity()==Integer.parseInt(QuantityUpdate.getText())){
            materials.setTypematerials(TypematerialsUpdate.getText());
            materials.setUnitprice(Float.parseFloat(UnitpriceUpdate.getText()));
            materials.setQuantity(Integer.parseInt(QuantityUpdate.getText()));
            sm.modifier(materials);
            refreshTableMaterials();
            refreshTableDepot();
        }else{
            if (Integer.parseInt(QuantityUpdate.getText())<=depot.getStock_available()){


                if (Integer.parseInt(QuantityUpdate.getText())<materials.getQuantity()){

                    materials.setTypematerials(TypematerialsUpdate.getText());
                    materials.setUnitprice(Float.parseFloat(UnitpriceUpdate.getText()));
                    int diff= materials.getQuantity()-Integer.parseInt(QuantityUpdate.getText());
                    materials.setQuantity(Integer.parseInt(QuantityUpdate.getText()));
                    depot.setStock_available(depot.getStock_available()+diff);
                    sm.modifier(materials);
                    sd.modifier(depot);
                    refreshTableMaterials();
                    refreshTableDepot();

                }else {

                    materials.setTypematerials(TypematerialsUpdate.getText());
                    materials.setUnitprice(Float.parseFloat(UnitpriceUpdate.getText()));
                    int diff=Integer.parseInt(QuantityUpdate.getText())-materials.getQuantity();
                    materials.setQuantity(Integer.parseInt(QuantityUpdate.getText()));
                    depot.setStock_available(depot.getStock_available()-diff);
                    sm.modifier(materials);
                    sd.modifier(depot);
                    refreshTableMaterials();
                    refreshTableDepot();


                }

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Quantity must be equal or lower than Quantity Available");
                alert.showAndWait();
            }
        }
    }
}
