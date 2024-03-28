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
        String req = "INSERT INTO `employees`(`nom`, `prenom`) VALUES (?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
          //  ps.setString(1,employee.getNom());
           // ps.setString(2,employee.getPrenom());
            ps.executeUpdate();
            System.out.println("Personne added !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Employees employee) {

    }

    @Override
    public void supprimer(int id) {

    }

    @Override
    public Employees getOneById(int id) {
        return null;
    }

    @Override
    public Set<Employees> getAll() {
        Set<Employees> employees = new HashSet<>();

        String req = "Select * from employees";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                int id = res.getInt("id");
                String nom = res.getString(2);
                String prenom = res.getString("prenom");
               // Employees p = new Employees(id,nom,prenom);
                Employees p = new Employees();
                employees.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employees;
    }
}
