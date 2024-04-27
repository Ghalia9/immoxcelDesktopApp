package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import tn.esprit.models.Depot;
import tn.esprit.models.Materials;
import tn.esprit.services.ServiceDepot;
import tn.esprit.services.ServiceMaterials;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

public class AddMaterialsController {
    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private TextField Typematerials ;

    @FXML
    private TextField Unitprice;

    @FXML
    private TextField Quantity;
    private final ServiceMaterials sm = new ServiceMaterials();

    private final ServiceDepot sd = new ServiceDepot();


    private ShowDepotController depotController;

    private int idDepot;




    public void initDepot(ShowDepotController depot,int id)
    {

        this.depotController=depot;
        this.idDepot=id;

    }

    private void refreshTable() throws SQLException, IOException {
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

   public void ajouterMaterialsAction (javafx.event.ActionEvent actionEvent){
       try {
           String unitPriceText = Unitprice.getText();
           String quantityText = Quantity.getText();
           String typeMaterialsText = Typematerials.getText();

           if (unitPriceText.isEmpty() || quantityText.isEmpty() || typeMaterialsText.isEmpty()) {
               // If any of the fields are empty, show an error message
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setContentText("All fields are required");
               alert.showAndWait();
               return; // Exit the method to prevent further execution
           }

           float unitPrice = Float.parseFloat(unitPriceText);
           int quantity = Integer.parseInt(quantityText);

           String query = "SELECT * FROM depot WHERE id=?";
           PreparedStatement statement = cnx.prepareStatement(query);
           statement.setInt(1, idDepot);
           ResultSet rst = statement.executeQuery();
           if (rst.next()) {
               int quantityFromDepot = rst.getInt("quantity_available");

               if (unitPrice > 0 && quantity > 0 && quantity <= quantityFromDepot) {
                   sm.ajouter(new Materials(typeMaterialsText, unitPrice, quantity, rst.getInt("id")));

                   int quantityToUpdate = quantityFromDepot - quantity;
                   sd.modifier(new Depot(rst.getInt("id"), rst.getString("location"), rst.getString("adresse"), rst.getInt("limit_stock"), quantityToUpdate));
                   refreshTable();
                   Alert alert = new Alert(Alert.AlertType.INFORMATION);
                   alert.setTitle("Success");
                   alert.setContentText("Materials added");
                   alert.show();
               } else if (unitPrice <= 0) {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Unit Price");
                   alert.setContentText("Unit Price must be positive");
                   alert.showAndWait();
               } else if (quantity <= 0) {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Quantity");
                   alert.setContentText("Quantity must be positive");
                   alert.showAndWait();
               } else if (quantity > quantityFromDepot) {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Quantity");
                   alert.setContentText("Quantity is over the Quantity available of the Depot");
                   alert.showAndWait();
               }
           }
       } catch (NumberFormatException e) {
           // Handle the case where Unitprice or Quantity is not a valid number
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Input Error");
           alert.setContentText("Please enter valid numbers for Unit Price and Quantity");
           alert.showAndWait();
       } catch (SQLException | IOException e) {
           // Handle SQL exceptions
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("SQL Exception");
           alert.setContentText(e.getMessage());
           alert.showAndWait();
           System.out.println(e.getMessage());
       }

    }

}
