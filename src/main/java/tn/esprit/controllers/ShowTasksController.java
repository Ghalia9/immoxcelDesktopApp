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
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.Projects;
import tn.esprit.models.Tasks;
import tn.esprit.models.User;
import tn.esprit.services.ServiceProjects;
import tn.esprit.services.ServiceTasks;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShowTasksController {
    LoginController loginController;
    User userConnected;

    @FXML
    public Text username;
    @FXML
    private Button addTaslkButton;

    @FXML
    private Button dashboard;

    @FXML
    private Button dashboard1;

    @FXML
    private Button dashboard11;

    @FXML
    private GridPane gridDoing;

    @FXML
    private GridPane gridDone;

    @FXML
    private GridPane gridToDo;

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
    private ScrollPane scroll1;

    @FXML
    private ScrollPane scroll2;

    @FXML
    private TextField search_field;

    @FXML
    private AnchorPane side_bar;

    @FXML
    private MenuButton supplier;

    @FXML
    private MenuButton transactions;



    @FXML
    private Button navigateBackbutton;
    private final ServiceTasks serviceTasks = new ServiceTasks();
private Projects project;

    private final BooleanProperty taskAdded = new SimpleBooleanProperty(false);
    public void refreshTasks() {
        loadTasks();
    }
    public void initialize() {
        taskAdded.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loadTasks();
                taskAdded.set(false); // Reset to false
            }
        });

        // Set event handler for addProjectButton to open AddProject.fxml
        addTaslkButton.setOnAction(event -> {
            try {
                openAddTask();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // Load tasks initially
        //loadTasks();
    }

    private void loadTasks() {
        // Clear existing tasks from all grids
        gridToDo.getChildren().clear();
        gridDoing.getChildren().clear();
        gridDone.getChildren().clear();

        // Retrieve tasks from the database
        Set<Tasks> tasksList = serviceTasks.getTasksByProjectId(project.getId());

        for (Tasks task : tasksList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TaskItem.fxml"));
                AnchorPane taskItem = loader.load();

                TaskItemController taskItemController = loader.getController();
                taskItemController.setTask(task);

                taskItemController.setShowTasksController(this); //This is for the Delete button to get the current Dashboard Controller to refresh

                taskItem.setOnMouseClicked(event -> {
                    try {
                        openTaskDetails(task);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                Insets insets = new Insets(10); // Customize the insets as needed
                GridPane.setMargin(taskItem, insets);



                // Check task status and add to the appropriate grid
                switch (task.getStatus().toUpperCase()) {
                    case "TO DO":
                        gridToDo.add(taskItem, 0, gridToDo.getChildren().size());
                        break;
                    case "DOING":
                        gridDoing.add(taskItem, 0, gridDoing.getChildren().size());
                        break;
                    case "DONE":
                        gridDone.add(taskItem, 0, gridDone.getChildren().size());
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void openAddTask() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddTask.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));

            // Pass projectAdded property to AddProjectController
            AddTaskController addTaskController = fxmlLoader.getController();
            addTaskController.setTaskAddedProperty(taskAdded);
            addTaskController.setProject(project);
            stage.setOnHidden(event -> {
                taskAdded.set(true); // When AddProject window is closed, set projectAdded to true
            });

            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private void openTaskDetails(Tasks task) throws IOException {
        System.out.println(task);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TaskDetails.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            TaskDetailsController taskDetailsController = fxmlLoader.getController();
            taskDetailsController.setShowTasksController(this);
            taskDetailsController.setData(task);

            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void navigateBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ShowProjects.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stage = (Stage) navigateBackbutton.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root1));

            ProjectsDashboardController projectsDashboardController = fxmlLoader.getController();
            projectsDashboardController.setLoginController(loginController,userConnected);
            projectsDashboardController.refreshProjects(); // Refresh projects when navigating back

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(Projects project, LoginController login, User user) {
        this.project=project;
        this.loginController=login;
        this.userConnected=user;
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
    int verifyUpdateFrom=0;
    Connection connection = DataSource.getInstance().getCnx();
    public void showSelfUpdate(ActionEvent actionEvent) throws SQLException, IOException {
        String query = "SELECT id FROM user WHERE username=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username.getText());
        ResultSet rst = statement.executeQuery();
        if (rst.next()) {
            verifyUpdateFrom = 2;
            int id = rst.getInt("id");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            UpdateUserController updateUser = loader.getController();
            updateUser.setProjectTasksController(this, id, username.getText());
            updateUser.initializeFields();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }
    }
}
