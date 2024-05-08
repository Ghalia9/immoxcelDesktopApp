package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Depot;
import tn.esprit.models.Materials;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowMaterialsController{


    private int id_depot ;

    private CardDepotController depotCard;
    private ShowDepotController depotController;
    Connection cnx = DataSource.getInstance().getCnx();

    public List<Materials> materialsToUpdate= new ArrayList<>();

    @FXML
    public VBox materialsLayout;

    @FXML
    private HBox root;

    public void initDepot(ShowDepotController depot, int i,CardDepotController card) {
        id_depot=i;
        this.depotCard=card;
        depotController=depot;
    }
    public void initData() throws SQLException, IOException {loadData();}


    public List<Materials> materials() throws SQLException {
        List<Materials> ls = new ArrayList<>();
        String req="SELECT * FROM materials WHERE  depot_id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1,depotCard.depotEntity.getId());
        ResultSet rst=ps.executeQuery();
        while (rst.next())
        {
            int id = rst.getInt("id");
            int depotId = rst.getInt("depot_id");
            String typematerial = rst.getString("type_material");
            float unitprice = rst.getFloat("unit_price");
            int quantity = rst.getInt("quantity");
            Materials materials=new Materials(id,typematerial,unitprice,quantity,depotId);
            ls.add(materials);
        }
        materialsToUpdate=ls;

        return ls;
    }

    public void loadData() throws SQLException, IOException {

        List<Materials> materials = new ArrayList<>(materials());
        for (int i = 0; i < materials.size(); i++) {

            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/CardMaterials.fxml"));
            HBox hBox = fxmlLoader.load();

            CardMaterialsController cdc = fxmlLoader.getController();
            cdc.initMaterials(this);
            cdc.initDepot(depotController,depotCard.depotEntity);
            hBox.getProperties().put("controller", cdc);
            cdc.setData(materials.get(i));
            materialsLayout.getChildren().add(hBox);

        }
    }


}
