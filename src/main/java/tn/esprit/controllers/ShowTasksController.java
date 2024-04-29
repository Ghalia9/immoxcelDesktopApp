package tn.esprit.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import tn.esprit.models.Projects;
import tn.esprit.models.Tasks;
import tn.esprit.services.ServiceProjects;
import tn.esprit.services.ServiceTasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShowTasksController {
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
    private Text username;

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
            projectsDashboardController.refreshProjects(); // Refresh projects when navigating back

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(Projects project) {
        this.project=project;
    }
}
