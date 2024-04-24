package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCardController {
    Connection connection = DataSource.getInstance().getCnx();

    DashboardController dashboardController;
    private final ServiceUser su = new ServiceUser();
    @FXML
    private Label FullName;

    @FXML
    private HBox deleteUser;

    @FXML
    private Label function;

    @FXML
    private HBox updateUser;

    @FXML
    public Label username;

    public void SetData(User user)
    {
        username.setText(user.getUsername());
        function.setText(user.getRole());
        FullName.setText(user.getFullName());
    }

    public void initDashboard(DashboardController dashbord)
    {
        this.dashboardController=dashbord;
    }

    public void update(MouseEvent mouseEvent) throws SQLException, IOException {
        String query = "SELECT id FROM user WHERE username=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,username.getText());
        ResultSet rst=statement.executeQuery();
        if(rst.next())
        {

            int id=rst.getInt("id");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent parent = loader.load();

            // Set the instance of DashboardController to AddAccountController
            UpdateUserController updateUser = loader.getController();
            updateUser.setDashboardController(dashboardController,id,username.getText());
            updateUser.initializeFields();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }
    }

    public void delete(MouseEvent mouseEvent) throws SQLException, IOException {
        String query = "SELECT id FROM user WHERE username=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,username.getText());
        ResultSet rst=statement.executeQuery();
        if(rst.next())
        {
            int id=rst.getInt("id");
            su.supprimer(id);
            dashboardController.users.clear();
            dashboardController.users();
            ObservableList<Node> children = dashboardController.UsersLayout.getChildren();
            List<Node> nodesToRemove = new ArrayList<>();
            for (Node child : children) {
                if (child instanceof HBox && child.getProperties().get("controller") != null && child.getProperties().get("controller") instanceof UserCardController) {
                    nodesToRemove.add(child);
                }
            }
            dashboardController.UsersLayout.getChildren().removeAll(nodesToRemove);

            dashboardController.initData();
           // loadUsers();
          //  users_nb.setText(get_dashboard_stats("user"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("User deleted");
            alert.showAndWait();

        }
    }
}
