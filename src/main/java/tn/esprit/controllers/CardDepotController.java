package tn.esprit.controllers;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
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
import javafx.stage.FileChooser;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Depot;
import tn.esprit.models.Materials;
import tn.esprit.models.User;
import tn.esprit.services.ServiceDepot;
import tn.esprit.utils.DataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDepotController {

    private User userConnected;



    Connection cnx = DataSource.getInstance().getCnx();
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
    Label Depotlocation;

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


    public List<Materials> getMaterialsForDepot(int depotId) throws SQLException {
        List<Materials> materials = new ArrayList<>();
        String query = "SELECT * FROM materials WHERE depot_id = ?";

             PreparedStatement statement = cnx.prepareStatement(query);
            statement.setInt(1, depotId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Assuming Material class has constructor Material(int id, String type, float unitPrice, int quantity)
                Materials material = new Materials(resultSet.getInt("id"),
                        resultSet.getString("type_material"),
                        resultSet.getFloat("unit_price"),
                        resultSet.getInt("quantity"));
                materials.add(material);
            }


        return materials;
    }
    public void generatePDF(MouseEvent mouseEvent) throws IOException, SQLException {
        List<Materials>materials=new ArrayList<>(getMaterialsForDepot(depotEntity.getId()));

        // Créer un sélecteur de fichiers
        FileChooser fileChooser = new FileChooser();

        // Définir le titre de la boîte de dialogue
        fileChooser.setTitle("Enregistrer le fichier PDF");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue pour choisir le fichier
        File selectedFile = fileChooser.showSaveDialog(new Stage());

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            // Utiliser le chemin sélectionné pour créer le fichier PDF
            String filePath = selectedFile.getAbsolutePath();
            // Créer un objet PdfDocument avec le chemin du fichier PDF de sortie
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(filePath));

            // Créer un objet Document
            Document document = new Document(pdfDocument);

            String imagePath = "C:\\Users\\MSI\\Desktop\\immoxcel-for-me\\src\\main\\resources\\logo-ct.png";


            float imageWidth = 100;
            float imageHeight = 100;

// Add the image with specified width and height
            Image img = new Image(ImageDataFactory.create("C:\\Users\\MSI\\Desktop\\immoxcel-for-me\\src\\main\\resources\\logo-ct.png"));
            img.scaleToFit(imageWidth, imageHeight);

// Set the position of the image
            img.setFixedPosition(25, PageSize.A4.getHeight() - 60);


// Add the image to the document
            document.add(img);

            // Ajouter des informations sur le dépôt au document
            document.setFont(PdfFontFactory.createFont());


            // Add title
            Paragraph title = new Paragraph("Depot Information").setFontSize(18).setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            LineSeparator line = new LineSeparator(new SolidLine());

            document.add(line);

            // Add depot information
            document.add(new Paragraph("Location: " + depotEntity.getLocation()));
            document.add(new Paragraph("Address: " + depotEntity.getAdresse()));
            document.add(new Paragraph("Limit Stock: " + depotEntity.getLimit_stock()));
            document.add(new Paragraph("Available Stock: " + depotEntity.getStock_available()));

            // Add materials table
            Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            table.addCell("Type");
            table.addCell("Unit Price");
            table.addCell("Quantity");

            for (Materials material : materials) {
                table.addCell(material.getTypematerials());
                table.addCell(String.valueOf(material.getUnitprice()));
                table.addCell(String.valueOf(material.getQuantity()));
            }

            document.add(table);
            // Fermer le document
            document.close();
        }
    }
}
