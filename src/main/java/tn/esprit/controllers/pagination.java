package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class pagination implements Initializable {

    @FXML
    private HBox cardLayout;

    @FXML
    private VBox leavesLayout;

    @FXML
    private Pagination pagination;

    private List<Leaves> pendingLeaves, oldLeaves;

    private final ServiceLeaves sl = new ServiceLeaves();
    private final ServiceEmployees se = new ServiceEmployees();
    private DisplayLeaves dl;

    private int leavesPerPage = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load data
        pendingLeaves = pendingLeaves();

        // Set page count based on the number of leaves
        pagination.setPageCount(calculatePageCount());

        // Set page factory to load data for each page
        pagination.setPageFactory(this::loadLeavesDataForPage);
    }

    private int calculatePageCount() {
        return (pendingLeaves.size() + leavesPerPage - 1) / leavesPerPage;
    }

    private Node loadLeavesDataForPage(int pageIndex) {
        int startIndex = pageIndex * leavesPerPage;
        int endIndex = Math.min(startIndex + leavesPerPage, pendingLeaves.size());

        VBox pageContent = new VBox();

        for (int i = startIndex; i < endIndex; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardLeaves.fxml"));
            try {
                HBox cardBox = loader.load();
                AfficherLeaves afficherLeaves = loader.getController();
                afficherLeaves.setCurrentLeave(pendingLeaves.get(i));
                afficherLeaves.setData(pendingLeaves.get(i));
                afficherLeaves.setCurrentEmployee(pendingLeaves.get(i).getEmployee());
                pageContent.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pageContent;
    }

    private List<Leaves> pendingLeaves() {
        Set<Leaves> allLeaves = sl.getAll();
        List<Leaves> pl = new ArrayList<>();
        for (Leaves leave : allLeaves) {
            if (leave.getStatus().equals("Pending")) {
                pl.add(leave);
            }
        }
        return pl;
    }

    private List<Leaves> oldLeaves() {
        Set<Leaves> allLeaves = sl.getAll();
        List<Leaves> oldLeaves = new ArrayList<>();
        for (Leaves leave : allLeaves) {
            if (!leave.getStatus().equals("Pending")) {
                oldLeaves.add(leave);
            }
        }
        return oldLeaves;
    }

    @FXML
    void navigateToEmployees(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayEmployees.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cardLayout.getScene().getWindow();
            stage.setScene(new Scene(root));
            DisplayEmployees employees = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toEmailOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HRMailer.fxml"));
            Parent root = loader.load();
            Mail mail = loader.getController();
            Stage mailstage = new Stage();
            mailstage.setScene(new Scene(root, 498, 400));
            mailstage.setTitle("Add Employee Form");
            mailstage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
