package tn.esprit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tn.esprit.models.Depot;
import tn.esprit.services.ServiceDepot;
import tn.esprit.utils.DataSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddDepotController implements Initializable {
    Connection cnx = DataSource.getInstance().getCnx();

    @FXML
    private ComboBox<String> locationComboBox;


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


    public void ajouterDepotAction(ActionEvent actionEvent) {
        try {
            String selectedLocation = locationComboBox.getValue(); // Récupérer la localisation sélectionnée dans la liste déroulante

            if (selectedLocation == null || Adresse.getText().isEmpty() || Limitstock.getText().isEmpty()) {
                // Vérifier si une localisation a été sélectionnée et si les autres champs sont vides
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Location, address, and limit stock are required");
                alert.showAndWait();
                return;
            }

            int limitStock = Integer.parseInt(Limitstock.getText());
            if (limitStock <= 0) {
                // Vérifier si le stock limite est positif
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Limit Stock");
                alert.setContentText("Limit Stock must be positive");
                alert.showAndWait();
                return;
            }

            // Si toutes les validations passent, procédez à l'ajout du dépôt
            sm.ajouter(new Depot(selectedLocation, Adresse.getText(), limitStock, limitStock));
            Adresse.clear();
            Limitstock.clear();
            refreshTable();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Depot added");
            alert.show();
        } catch (NumberFormatException e) {
            // Gérer le cas où le stock limite n'est pas un entier valide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Limit Stock");
            alert.setContentText("Limit Stock must be a valid integer");
            alert.showAndWait();
        } catch (SQLException | IOException e) {
            // Gérer les exceptions SQL ou IO
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\MSI\\Desktop\\immoxcel-java\\immoxcelDesktopApp\\src\\main\\resources\\regionTunis.json")) {
            // Parse the JSON file
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;

            // Extract country names from JSON and add them to a list
            List<String> locationName = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String Region = (String) jsonObject.get("location");
                locationName.add(Region);
            }
            System.out.println("Country Names: " + locationName);

            // Populate the ComboBox with country names
            locationComboBox.getItems().addAll(locationName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
