package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Depot;
import tn.esprit.models.User;
import tn.esprit.services.ServiceDepot;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDepotController {

    private User userConnected;



    @FXML
    private ImageView addMaterial;

    @FXML
    private HBox addMaterials;

    @FXML
    private Label address;

    @FXML
    private HBox deleteDepot;

    @FXML
    private Label limitStock;

    @FXML
    private Label Depotlocation;

    @FXML
    private Label quantityAvailable;

    @FXML
    private HBox showMaterials;

    @FXML
    private HBox updateDepot;

    @FXML
    private Label image;

    private ShowDepotController depot;

    public Depot depotEntity;

    private LoginController loginController;

    private ServiceDepot sd= new ServiceDepot();

    public void initDepot(ShowDepotController depotController ,Depot d)
    {
        this.depot=depotController;
        this.depotEntity=d;


    }

    public void setData(Depot depot)
    {

        image.setText(String.valueOf(depot.getId()));
        image.setDisable(true);
        image.setVisible(false);
        Depotlocation.setText(depot.getLocation());
        address.setText(depot.getAdresse());
        limitStock.setText(String.valueOf(depot.getLimit_stock()));
        quantityAvailable.setText(String.valueOf(depot.getStock_available()));
    }

    public void deleteDepot(MouseEvent mouseEvent) throws SQLException, IOException {
        if(Integer.parseInt(quantityAvailable.getText())!=Integer.parseInt(limitStock.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Depot have Materials");
            alert.showAndWait();
        }
        else {
            sd.supprimer(Integer.parseInt(image.getText()));
            depot.depotToUpdate.clear();
            depot.depots().clear();
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
    }

    public void showAddMaterials(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddMaterials.fxml"));
        Parent parent=loader.load();
        AddMaterialsController addMaterials = loader.getController();
        addMaterials.initDepot(depot,Integer.parseInt(image.getText()));
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    public void showMaterials(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowMaterials.fxml"));
        Parent parent=loader.load();

        ShowMaterialsController ShowMaterials = loader.getController();

        ShowMaterials.initDepot(depot,depotEntity.getId(),this);
        ShowMaterials.initData();
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
    public void ShowUpdateDepot(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateDepot.fxml"));
        Parent parent=loader.load();
        UpdateDepotController UpdateDepot = loader.getController();
        UpdateDepot.initupdatedepot(depot,depotEntity);
        UpdateDepot.setdata();
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
