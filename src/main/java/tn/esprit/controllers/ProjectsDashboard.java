package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import tn.esprit.models.Employees;
import tn.esprit.models.Projects;
import tn.esprit.services.ServiceProjects;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class ProjectsDashboard implements Initializable {
    @FXML
    private Button dashboard;

    @FXML
    private Button dashboard1;

    @FXML
    private Button dashboard11;

    @FXML
    private GridPane grid;

    @FXML
    private Pane innerPane;

    @FXML
    private MenuButton inventory;

    @FXML
    private Button logout_btn;

    @FXML
    private MenuButton projects;

    @FXML
    private HBox root;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField search_field;

    @FXML
    private AnchorPane side_bar;

    @FXML
    private MenuButton supplier;

    @FXML
    private MenuButton transactions;

    @FXML
    private Text username;
    private final ServiceProjects serviceProjects = new ServiceProjects();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Set<Projects> projectsSet = serviceProjects.getAll();
        System.out.println(projectsSet.size());
        List<Projects> projectsList = new ArrayList<>(projectsSet);
        System.out.println(projectsList.get(0).toString());
        int column = 0 ;
        int row = 1;
        try {
            for (int i = 0; i < projectsSet.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProjectItem.fxml"));
                AnchorPane anchorPane = loader.load();

                ProjectItem projectItem = loader.getController();
                projectItem.setData(projectsList.get(i));

                if (column==3) {
                    column =0;
                    row++;
                }

                grid.add(anchorPane,column++,row);
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane,new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
