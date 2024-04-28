package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.models.Projects;
import tn.esprit.models.Tasks;

import java.io.IOException;
import java.sql.Date;

public class TaskDetailsController {

    @FXML
    private Label completionDateLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Button editButton;

    @FXML
    private Text taskDescriptionText;

    @FXML
    private Label taskTitlelabel;

    private Tasks task;

    private ShowTasksController showTasksController;

    public void setShowTasksController(ShowTasksController controller) {
        this.showTasksController = controller;
    }


    public void setData(Tasks task){
        this.task=task;
        taskTitlelabel.setText(task.getTitle());
        taskDescriptionText.setText(task.getDescription());
        dueDateLabel.setText(formatDate(task.getDeadline()));
        completionDateLabel.setText(formatDate(task.getCompletion_date()));
    }
@FXML
    private void handleEditTask() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EditTask.fxml"));
            Parent parent = fxmlLoader.load();

            EditTaskController editTaskController = fxmlLoader.getController();
            editTaskController.setShowTasksController(showTasksController);
            editTaskController.setData(task);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Project");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            editButton.getScene().getWindow().hide();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String formatDate(Date date) {
        return date != null ? date.toString() : "Not set yet";
    }
}

