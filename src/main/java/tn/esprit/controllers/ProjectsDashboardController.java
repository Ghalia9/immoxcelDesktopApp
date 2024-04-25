package tn.esprit.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.models.Projects;
import tn.esprit.services.ServiceProjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProjectsDashboardController {

    @FXML
    private Button addProjectButton;

    @FXML
    private GridPane grid;

    @FXML
    private TextField search_field;

    @FXML
    private AnchorPane side_bar;

    @FXML
    private Text username;

    private final ServiceProjects serviceProjects = new ServiceProjects();

    private final BooleanProperty projectAdded = new SimpleBooleanProperty(false);

    public void initialize() {
        projectAdded.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loadProjects();
                projectAdded.set(false); // Reset to false
            }
        });

        // Set event handler for addProjectButton to open AddProject.fxml
        addProjectButton.setOnAction(event -> {
            try {
                openAddProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Load projects initially
        loadProjects();
    }
    public void refreshProjects() {
        loadProjects();
    }

    public void loadProjects() {
        grid.getChildren().clear(); // Clear existing projects

        Set<Projects> projectsSet = serviceProjects.getAll();
        List<Projects> projectsList = new ArrayList<>(projectsSet);

        for (int i = 0; i < projectsList.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProjectItem.fxml"));
                AnchorPane anchorPane = loader.load();

                ProjectItemController projectItem = loader.getController();
                projectItem.setData(projectsList.get(i));

                projectItem.setProjectsDashboardController(this); //This is for the Delete button to get the current Dashboard Controller to refresh


                int finalI = i;
                anchorPane.setOnMouseClicked(event -> {
                    try {
                        openProjectDetails(projectsList.get(finalI));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                if (i % 3 == 0) {
                    grid.addRow(i / 3);
                }

                grid.add(anchorPane, i % 3, i / 3);


                GridPane.setMargin(anchorPane, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openProjectDetails(Projects project) throws IOException {
        System.out.println(project);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProjectDetails.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            ProjectDetailsController projectDetails = fxmlLoader.getController();
            projectDetails.setProjectsDashboardController(this);
            projectDetails.setData(project);

            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void openAddProject() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddProject.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            // Pass projectAdded property to AddProjectController
            AddProjectController addProjectController = fxmlLoader.getController();
            addProjectController.setProjectAddedProperty(projectAdded);

            stage.setOnHidden(event -> {
                projectAdded.set(true); // When AddProject window is closed, set projectAdded to true
            });

            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
