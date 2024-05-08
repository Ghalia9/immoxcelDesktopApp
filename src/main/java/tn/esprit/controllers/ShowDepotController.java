package tn.esprit.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Depot;
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

public class ShowDepotController implements Initializable {
    Connection cnx = DataSource.getInstance().getCnx();

    public List<Depot> depotToUpdate= new ArrayList<>();
    @FXML
    public VBox depotLayout;

    @FXML
    private TableView<ObservableList<String>> usersTable;

    public List<Depot> depots() throws SQLException {
        List<Depot> ls = new ArrayList<>();
        String req="SELECT id,location,adresse,limit_stock,quantity_available FROM Depot";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rst=ps.executeQuery();
        while (rst.next())
        {
            int id = rst.getInt("id");
            String location = rst.getString("location");
            String adresse = rst.getString("adresse");
            int limit = rst.getInt("limit_stock");
            int available = rst.getInt("quantity_available");
            Depot depot=new Depot(id,location,adresse,limit,available);
            ls.add(depot);
        }
        depotToUpdate=ls;
        return ls;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadData() throws SQLException, IOException {
        List<Depot> depots = new ArrayList<>(depots());
        for (int i=0; i<depots.size();i++)
        {

            FXMLLoader fxmlLoader=new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/CardDepot.fxml"));
            HBox hBox = fxmlLoader.load();

            CardDepotController cdc= fxmlLoader.getController();
            cdc.initDepot(this,depots.get(i));
            hBox.getProperties().put("controller",cdc);
            cdc.setData(depots.get(i));
            depotLayout.getChildren().add(hBox);

        }

    }
    public void showAddDepot(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDepot.fxml"));
        Parent parent=loader.load();
        AddDepotController addDepot = loader.getController();
        addDepot.initDepot(this);
        Scene scene=new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
