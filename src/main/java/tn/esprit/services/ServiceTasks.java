package tn.esprit.services;

import tn.esprit.models.Tasks;
import tn.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ServiceTasks implements IService<Tasks> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Tasks tasks) {
        String req = "INSERT INTO `task` (`task_title`, `task_description`, `task_deadline`, `task_status`, `task_completion_date`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, tasks.getTitle());
            ps.setString(2, tasks.getDescription());
            ps.setDate(3, tasks.getDeadline());
            ps.setString(4, "To Do");
            ps.setDate(5, tasks.getCompletion_date());

            ps.executeUpdate();
            System.out.println("Task added!");
        } catch (SQLException e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Tasks tasks) {
        String req = "UPDATE `task` SET `task_title`=?, `task_description`=?, `task_deadline`=?, `task_status`=?, `task_completion_date`=? WHERE `id`=?";

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, tasks.getTitle());
            ps.setString(2, tasks.getDescription());
            ps.setDate(3, tasks.getDeadline());
            ps.setString(4, tasks.getStatus());
            ps.setDate(5, tasks.getCompletion_date());
            ps.setInt(6, tasks.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task with ID " + tasks.getId() + " updated successfully!");
            } else {
                System.out.println("Task with ID " + tasks.getId() + " not found or update failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM `task` WHERE `id`=?";

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task with ID " + id + " deleted successfully!");
            } else {
                System.out.println("Task with ID " + id + " not found or delete failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }

    @Override
    public Tasks getOneById(int id) {
        String req = "SELECT * FROM `task` WHERE `id`=?";
        Tasks task = null;

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                task = new Tasks(
                        resultSet.getInt("id"),
                        resultSet.getString("task_title"),
                        resultSet.getString("task_description"),
                        resultSet.getDate("task_deadline"),
                        resultSet.getString("task_status"),
                        resultSet.getDate("task_completion_date")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting task by ID: " + e.getMessage());
        }

        return task;
    }

    @Override
    public Set<Tasks> getAll() {
        String req = "SELECT * FROM `task`";
        Set<Tasks> tasksSet = new HashSet<>();

        try {
            PreparedStatement ps = cnx.prepareStatement(req);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Tasks task = new Tasks(
                        resultSet.getInt("id"),
                        resultSet.getString("task_title"),
                        resultSet.getString("task_description"),
                        resultSet.getDate("task_deadline"),
                        resultSet.getString("task_status"),
                        resultSet.getDate("task_completion_date")
                );
                tasksSet.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all tasks: " + e.getMessage());
        }

        return tasksSet;
    }
}
