package tn.esprit.services;

import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceLeaves implements IService<Leaves> {
    Connection cnx = DataSource.getInstance().getCnx();
    final ServiceEmployees se = new ServiceEmployees();


    @Override
    public void ajouter(Leaves leave) {
        String req = "INSERT INTO `leaves`(`leave_type`, `start_date`, `finish_date`, `status`, `leave_description`,`employee_id`) VALUES (?, ?, ?, ?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, leave.getLeaveType());
            ps.setDate(2, leave.getStartDate());
            ps.setDate(3, leave.getFinishDate());
            ps.setString(4, leave.getStatus());
            ps.setString(5, leave.getLeaveDescription());
            ps.setInt(6,leave.getEmployee().getId());


            ps.executeUpdate();
            System.out.println("Leave added !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Leaves leave) {
        String req = "UPDATE `leaves` SET `leave_type`=?, `start_date`=?, `finish_date`=?, `status`=?, `leave_description`=?, `employee_id`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, leave.getLeaveType());
            ps.setDate(2, leave.getStartDate());
            ps.setDate(3, leave.getFinishDate());
            ps.setString(4, leave.getStatus());
            ps.setString(5, leave.getLeaveDescription());
            ps.setInt(6,leave.getEmployee().getId());
            ps.setInt(7, leave.getId());

            ps.executeUpdate();
            System.out.println("Leave updated !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM `leaves` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("Leave deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Leaves getOneById(int id) {
        String req = "SELECT * FROM `leaves` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                int employee_id=res.getInt("employee_id");
                Employees employee = se.getOneById(employee_id);
                Leaves leave = new Leaves(
                        res.getInt("id"),
                        res.getString("leave_type"),
                        res.getDate("start_date"),
                        res.getDate("finish_date"),
                        res.getString("status"),
                        res.getString("leave_description"),
                        employee
                );
                return leave;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Set<Leaves> getAll() {
        Set<Leaves> leavesSet = new HashSet<>();

        String req = "Select * from leaves";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                int employee_id=res.getInt("employee_id");
                Employees employee = se.getOneById(employee_id);
                Leaves leave = new Leaves(
                        res.getInt("id"),
                        res.getString("leave_type"),
                        res.getDate("start_date"),
                        res.getDate("finish_date"),
                        res.getString("status"),
                        res.getString("leave_description"),
                        employee
                );
                leavesSet.add(leave);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return leavesSet;
    }
    public Set<Leaves> getLeavesByEmployee(Employees employee){
        Set<Leaves> leavesSet = new HashSet<>();
        String req = "SELECT * FROM `leaves` WHERE `employee_id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, employee.getId());
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                //int employee_id=res.getInt("employee_id");
               // Employees employee = se.getOneById(employee_id);
                Leaves leave = new Leaves(
                        res.getInt("id"),
                        res.getString("leave_type"),
                        res.getDate("start_date"),
                        res.getDate("finish_date"),
                        res.getString("status"),
                        res.getString("leave_description"),
                        employee
                );
                leavesSet.add(leave);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return leavesSet;
    }
}
