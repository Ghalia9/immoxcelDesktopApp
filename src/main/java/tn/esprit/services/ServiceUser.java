package tn.esprit.services;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import javafx.collections.FXCollections;
import tn.esprit.models.Projects;
import tn.esprit.models.User;
import tn.esprit.utils.DataSource;

public class ServiceUser implements IService<User>{
    Connection connection = DataSource.getInstance().getCnx();


    @Override
    public void ajouter(User user) {

            String req="INSERT INTO `user` (`employee_id`,`username`,`password`,`email`,`is_verified`,`reset_token`) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(req);

        statement.setInt(1, user.getEmp_id());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setInt(5, 0);
            statement.setString(6, "");
            statement.executeUpdate();
            System.out.println("user added");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }

    @Override
    public void modifier(User user) {
        try {
            System.out.println(user.getUsername());
            String req = "UPDATE user SET username=?, email=?, password=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " rows updated");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception properly
        }
    }

    @Override
    public void supprimer(int id)  {
        try {
            String query = "DELETE FROM user where id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getOneById(int id) {
        return null;
    }

    @Override
    public Set<User> getAll() {
        return null;
    }
}
