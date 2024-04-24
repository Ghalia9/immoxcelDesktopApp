package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Depot;
import tn.esprit.models.Materials;
import tn.esprit.services.ServiceDepot;
import tn.esprit.services.ServiceMaterials;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardMaterialsController {
    @FXML
    private HBox deleteMaterials;

    @FXML
    private Label fquantity;

    @FXML
    private Label image;

    @FXML
    private HBox showMaterials;

    @FXML
    private Label typematerials;

    @FXML
    private Label unitprice;

    @FXML
    private HBox updateMaterials;

    private ShowMaterialsController materialsController;

    private ServiceMaterials sd = new ServiceMaterials();
    private Depot depot;
    private Materials material;

    ShowDepotController depotController;

    private  ServiceDepot s=new ServiceDepot();

    public void initDepot(ShowDepotController depotC,Depot depot) {
        this.depot=depot;
         this.depotController= depotC;
    }

    public void setData(Materials materials) {
        this.material=materials;
        image.setText(String.valueOf(materials.getId()));
        image.setDisable(true);
        image.setVisible(false);
        typematerials.setText(materials.getTypematerials());
        unitprice.setText(String.valueOf(materials.getUnitprice()));
        fquantity.setText(String.valueOf(materials.getQuantity()));

    }

    public void initMaterials(ShowMaterialsController showMaterialsController) {
        this.materialsController = showMaterialsController;
    }

    public void deleteMaterials(MouseEvent mouseEvent) throws SQLException, IOException {
        depot.setStock_available(depot.getStock_available()+Integer.parseInt(fquantity.getText()));
        sd.supprimer(Integer.parseInt(image.getText()));
        s.modifier(depot);
        depotController.depotToUpdate.clear();
        depotController.depots().clear();
        materialsController.materialsToUpdate.clear();
        materialsController.materials().clear();
        ObservableList<Node> children = materialsController.materialsLayout.getChildren();
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof HBox && child.getProperties().get("controller") != null && child.getProperties().get("controller") instanceof CardMaterialsController) {
                nodesToRemove.add(child);
            }
        }

        ObservableList<Node> childrenDepot = depotController.depotLayout.getChildren();
        List<Node> nodesDepotToRemove = new ArrayList<>();
        for (Node child : childrenDepot) {
            if (child instanceof HBox && child.getProperties().get("controller") != null && child.getProperties().get("controller") instanceof CardDepotController) {
                nodesDepotToRemove.add(child);
            }
        }
        depotController.depotLayout.getChildren().removeAll(nodesDepotToRemove);
        depotController.loadData();
        materialsController.materialsLayout.getChildren().removeAll(nodesToRemove);
        materialsController.loadData();
    }

    public void ShowUpdateMaterials(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateMaterials.fxml"));
        Parent parent=loader.load();
        UpdateMaterialsController UpdateMaterials = loader.getController();
        UpdateMaterials.initupdatematerials(materialsController,material,depotController,depot);
        UpdateMaterials.setData();
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }


}
