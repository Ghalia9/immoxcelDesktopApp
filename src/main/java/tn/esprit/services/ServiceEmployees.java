package tn.esprit.services;

import tn.esprit.models.Employees;
import tn.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceEmployees implements IService<Employees> {

    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Employees employee) {
        /*String req = "INSERT INTO `personne`(`nom`, `prenom`) VALUES ('"+personne.getNom()+"','"+personne.getPrenom()+"')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Personne added !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/
        String req = "INSERT INTO `employees`(`emp_name`, `emp_last_name`, `emp_sex`, `emp_email`, `emp_address`, `emp_phone`, `emp_function`, `birth_date`, `hire_date`, `end_contract_date`, `contract_type`, `allowed_leave_days`, `empcv`, `emp_taken_leaves`, `emp_cin`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1,employee.getEmpName());
            ps.setString(2,employee.getEmpLastName());
            ps.setString(3,employee.getEmpSex());
            ps.setString(4,employee.getEmpEmail());
            ps.setString(5,employee.getEmpAddress());
            ps.setString(6,employee.getEmpPhone());
            ps.setString(7,employee.getEmpFunction());
            ps.setDate(8,employee.getBirthDate());
            ps.setDate(9,employee.getHireDate());
            ps.setDate(10,employee.getEndContractDate());
            ps.setString(11,employee.getContractType());
            ps.setInt(12,employee.getAllowedLeaveDays());
            ps.setBlob(13,employee.getEmpCV());
            ps.setInt(14,employee.getEmpTakenLeaves());
            ps.setString(15,employee.getEmpCin());

            ps.executeUpdate();
            System.out.println("Personne added !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Employees employee) {
        String req = "UPDATE `employees` SET `emp_name`=?, `emp_last_name`=?, `emp_sex`=?, `emp_email`=?, `emp_address`=?, `emp_phone`=?, `emp_function`=?, `birth_date`=?, `hire_date`=?, `end_contract_date`=?, `contract_type`=?, `allowed_leave_days`=?, `empcv`=?, `emp_taken_leaves`=?, `emp_cin`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, employee.getEmpName());
            ps.setString(2, employee.getEmpLastName());
            ps.setString(3, employee.getEmpSex());
            ps.setString(4, employee.getEmpEmail());
            ps.setString(5, employee.getEmpAddress());
            ps.setString(6, employee.getEmpPhone());
            ps.setString(7, employee.getEmpFunction());
            ps.setDate(8, employee.getBirthDate());
            ps.setDate(9, employee.getHireDate());
            ps.setDate(10, employee.getEndContractDate());
            ps.setString(11, employee.getContractType());
            ps.setInt(12, employee.getAllowedLeaveDays());
            ps.setBlob(13, employee.getEmpCV());
            ps.setInt(14, employee.getEmpTakenLeaves());
            ps.setString(15, employee.getEmpCin());
            ps.setInt(16, employee.getId());

            ps.executeUpdate();
            System.out.println("Employee updated !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM `employees` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("Employee deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Employees getOneById(int id) {
        String req = "SELECT * FROM `employees` WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                Employees employee = new Employees(
                        res.getInt("id"),
                        res.getString("emp_name"),
                        res.getString("emp_last_name"),
                        res.getString("emp_sex"),
                        res.getString("emp_email"),
                        res.getString("emp_address"),
                        res.getString("emp_phone"),
                        res.getString("emp_function"),
                        res.getDate("birth_date"),
                        res.getDate("hire_date"),
                        res.getDate("end_contract_date"),
                        res.getString("contract_type"),
                        res.getInt("allowed_leave_days"),
                        res.getBlob("empcv"),
                        res.getInt("emp_taken_leaves"),
                        res.getString("emp_cin")
                );
                return employee;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;    }

    @Override
    public Set<Employees> getAll() {
        Set<Employees> employeesSet = new HashSet<>();

        String req = "Select * from employees";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                int id = res.getInt("id");
                String empName = res.getString("emp_name");
                String empLastName = res.getString("emp_last_name");
                String empSex = res.getString("emp_sex");
                String empEmail = res.getString("emp_email");
                String empAddress = res.getString("emp_address");
                String empPhone = res.getString("emp_phone");
                String empFunction = res.getString("emp_function");
                Date birthDate = res.getDate("birth_date");
                Date hireDate = res.getDate("hire_date");
                Date endContractDate = res.getDate("end_contract_date");
                String contractType = res.getString("contract_type");
                int allowedLeaveDays = res.getInt("allowed_leave_days");
                Blob empcv = res.getBlob("empcv");
                int empTakenLeaves = res.getInt("emp_taken_leaves");
                String empCin = res.getString("emp_cin");
                Employees employee = new Employees(id, empName, empLastName, empSex, empEmail, empAddress, empPhone, empFunction, birthDate, hireDate, endContractDate, contractType, allowedLeaveDays, empcv, empTakenLeaves, empCin);
                if(employeesSet.add(employee))
                    System.out.println("1");
                else System.out.println("0");
            }
            System.out.println(employeesSet.size());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employeesSet;
    }
}
