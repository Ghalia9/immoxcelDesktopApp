package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Projects;

public class ProjectItem {
    @FXML
    private Label onGoingBudgetLabel;

    @FXML
    private Label projectFinishDateLabel;

    @FXML
    private Label projectNameLabel;

    @FXML
    private Label projectStartDateLabel;

    private Projects project;

    public void setData(Projects project){
        this.project=project;
        projectNameLabel.setText(project.getProject_name());
        projectStartDateLabel.setText(project.getDate_pred_start().toString());
        projectFinishDateLabel.setText(project.getDate_pred_finish().toString());
        onGoingBudgetLabel.setText(String.valueOf(project.getBudget()));
    }
}
