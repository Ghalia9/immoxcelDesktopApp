package tn.esprit.services;

import tn.esprit.models.Projects;
import tn.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceProjects implements IService<Projects>{

        Connection cnx = DataSource.getInstance().getCnx();
        @Override
        public void ajouter(Projects project) {
            String req = "INSERT INTO `project` (`project_name`, `date_pred_start`, `date_pred_finish`, `date_completion`, `budget`, `actual_cost`) VALUES (?,?,?,?,?,?)";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                System.out.println("After statement");
                ps.setString(1,project.getProject_name());
                ps.setDate(2,project.getDate_pred_start());
                ps.setDate(3,project.getDate_pred_finish());
                ps.setDate(4,project.getDate_completion());
                ps.setFloat(5,project.getBudget());
                ps.setFloat(6,project.getActual_cost());
                ps.executeUpdate();
                System.out.println("Project added !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void modifier(Projects project) {
            String req = "UPDATE `project` SET `project_name`=?, `date_pred_start`=?, `date_pred_finish`=?, `date_completion`=?, `budget`=?, `actual_cost`=? WHERE `id`=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setString(1, project.getProject_name());
                ps.setDate(2, project.getDate_pred_start());
                ps.setDate(3, project.getDate_pred_finish());
                ps.setDate(4, project.getDate_completion());
                ps.setFloat(5, project.getBudget());
                ps.setFloat(6, project.getActual_cost());
                ps.setInt(7, project.getId()); // Assuming there's an ID field in Projects class
                ps.executeUpdate();
                System.out.println("Project updated !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void supprimer(int id) {
            String req = "DELETE FROM `project` WHERE `id`=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, id);
                ps.executeUpdate();
                System.out.println("Project deleted !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public Projects getOneById(int id) {
            String req = "SELECT * FROM `project` WHERE `id`=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Projects project = new Projects();
                    project.setId(rs.getInt("id"));
                    project.setProject_name(rs.getString("project_name"));
                    project.setDate_pred_start(rs.getDate("date_pred_start"));
                    project.setDate_pred_finish(rs.getDate("date_pred_finish"));
                    project.setDate_completion(rs.getDate("date_completion"));
                    project.setBudget(rs.getFloat("budget"));
                    project.setActual_cost(rs.getFloat("actual_cost"));
                    return project;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        public Set<Projects> getAll() {
            Set<Projects> projectsSet = new HashSet<>();
            String req = "SELECT * FROM `project`";
            try {
                Statement st = cnx.createStatement();
                ResultSet res = st.executeQuery(req);
                while (res.next()) {
                    Projects project = new Projects();
                    project.setId(res.getInt("id"));
                    project.setProject_name(res.getString("project_name"));
                    project.setDate_pred_start(res.getDate("date_pred_start"));
                    project.setDate_pred_finish(res.getDate("date_pred_finish"));
                    project.setDate_completion(res.getDate("date_completion"));
                    project.setBudget(res.getFloat("budget"));
                    project.setActual_cost(res.getFloat("actual_cost"));
                    projectsSet.add(project);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return projectsSet;
        }
}
