package tn.esprit.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Employees;
import tn.esprit.models.Projects;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceProjects;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    Text username;


    @FXML
    private Button sort;

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
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the list of employees dynamically
            updateProjectsList(newValue);
        });
    }
    public void refreshProjects() {loadProjects();
    }

    @FXML
    void sortOnClick(ActionEvent event) {
        // Get all projects and sort them by deadline
        grid.getChildren().clear(); // Clear existing projects

        List<Projects> allProjects = new ArrayList<>(serviceProjects.getAll());
        List<Projects> sortedProjects = allProjects.stream()
                .sorted(Comparator.comparing(Projects::getDate_pred_finish))
                .collect(Collectors.toList());

        for (int i = 0; i < sortedProjects.size(); i++) {
            Projects project = sortedProjects.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProjectItem.fxml"));
                AnchorPane anchorPane = loader.load();

                ProjectItemController projectItem = loader.getController();
                projectItem.setData(project);
                projectItem.setProjectsDashboardController(this); //This is for the Delete button to get the current Dashboard Controller to refresh


                int finalI = i;
                anchorPane.setOnMouseClicked(mouseEvent -> {
                    try {
                        openProjectDetails(sortedProjects.get(finalI));
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
    private void updateProjectsList(String searchQuery) {
        List<Projects> allProjects = new ArrayList<>(serviceProjects.getAll());

        List<Projects> filteredProjects = allProjects.stream()
                .filter(project -> matchesSearchQuery(project, searchQuery))
                .collect(Collectors.toList());

        // Clear existing UI
        grid.getChildren().clear();

        // Display filtered projects or show "No projects found" text
        if (filteredProjects.isEmpty()) {
            showNoProjectsAlert();
        } else {
            for (int i = 0; i < filteredProjects.size(); i++) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProjectItem.fxml"));
                    AnchorPane anchorPane = loader.load();

                    ProjectItemController projectItem = loader.getController();
                    projectItem.setData(filteredProjects.get(i));
                    projectItem.setProjectsDashboardController(this); //This is for the Delete button to get the current Dashboard Controller to refresh


                    int finalI = i;
                    anchorPane.setOnMouseClicked(event -> {
                        try {
                            openProjectDetails(filteredProjects.get(finalI));
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
    }
    // Method to check if an employee matches the search query
    private boolean matchesSearchQuery(Projects project, String searchQuery) {
        // You can customize this method based on your search requirements
        // For simplicity, this example checks if the project's title or description contains the search query
        String title = project.getProject_name();
        return title.toLowerCase().contains(searchQuery.toLowerCase());
    }

    private void showNoProjectsAlert() {
        grid.getChildren().add(new Label("No projects found."));
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
            stage.setTitle("Project Details");
            stage.setScene(new Scene(root1));

            ProjectDetailsController projectDetails = fxmlLoader.getController();
            projectDetails.setProjectsDashboardController(this,loginController,userConnected);
            projectDetails.setData(project);
            projectDetails.setDashboard(dashboard);
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

    //Selim

    @FXML
    public Pane paneToChange;

    public int verifyUpdateFrom=0;

    Connection connection = DataSource.getInstance().getCnx();


    private LoginController loginController;

    public User userConnected;

    public void setLoginController(LoginController login, User user)
    {
        this.loginController=login;
        this.userConnected = user;
        username.setText(userConnected.getUsername());
    }
    public void logout(ActionEvent actionEvent) throws IOException {
        userConnected=null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void showSelfUpdate(ActionEvent actionEvent) throws SQLException, IOException {
        String query = "SELECT id FROM user WHERE username=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,username.getText());
        ResultSet rst=statement.executeQuery();
        if(rst.next())
        {
            verifyUpdateFrom=2;
            int id=rst.getInt("id");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            UpdateUserController updateUser = loader.getController();
            updateUser.setProjectController(this,id,username.getText());
            updateUser.initializeFields();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }
    }

    private ServiceEmployees se=new ServiceEmployees();
    public void ShowPersonalInformation(ActionEvent actionEvent) throws IOException {
        FXMLLoader HrLoader = new FXMLLoader(getClass().getResource("/DisplayEmployees.fxml"));
        Parent HrRoot = HrLoader.load();
        //HRDashboard HrController = HrLoader.getController();
        DisplayEmployees HrController = HrLoader.getController();


        HrController.setLoginController(loginController,userConnected);
        Employees employeeInfo=se.getOneById(userConnected.getEmp_id());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEmployee.fxml"));
        Parent Details = loader.load();
        DetailsEmployee employeedetails=loader.getController();
        employeedetails.setdashbord(HrController);
        Stage detailsEmpStage = new Stage();
        detailsEmpStage.initStyle(StageStyle.DECORATED);
        detailsEmpStage.setScene(new Scene(Details, 638, 574));
        detailsEmpStage.setTitle("Employee Details");
        employeedetails.setDetailsData(employeeInfo);
        employeedetails.setCurrentEmployee(employeeInfo);
        detailsEmpStage.showAndWait();
    }

    private DashboardController dashboard;
    public void setDashboard(DashboardController dashboardController) {
        this.dashboard=dashboardController;
    }


    public void ShowCalendar(ActionEvent actionEvent) throws IOException {
        FXMLLoader ProjectLoader = new FXMLLoader(getClass().getResource("/GoogleCalendarEvents.fxml"));
        Parent ProjectRoot = ProjectLoader.load();
        GoogleCalendarEventsController ProjectController = ProjectLoader.getController();
        ProjectController.setLoginController(loginController, userConnected);
        this.paneToChange.getChildren().setAll(ProjectController.paneToChange.getChildren());
    }

    public void ShowProjects(ActionEvent actionEvent) throws IOException {
        FXMLLoader ProjectLoader = new FXMLLoader(getClass().getResource("/ShowProjects.fxml"));
        Parent ProjectRoot = ProjectLoader.load();
        ProjectsDashboardController ProjectController = ProjectLoader.getController();
        ProjectController.setLoginController(loginController, userConnected);

       this.paneToChange.getChildren().setAll(ProjectController.paneToChange.getChildren());
    }
}
