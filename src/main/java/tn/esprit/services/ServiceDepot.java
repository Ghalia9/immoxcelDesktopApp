package tn.esprit.services;

import tn.esprit.models.Depot;
import tn.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class ServiceDepot implements IService<Depot>{

    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Depot depot)throws SQLException {
        String req = "INSERT INTO `depot`(`location`, `adresse`,`limit_stock`,`quantity_available`) VALUES (?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1,depot.getLocation());
        ps.setString(2,depot.getAdresse());
        ps.setInt(3,depot.getLimit_stock());
        ps.setInt(4,depot.getStock_available());
        ps.executeUpdate();
        System.out.println("Depot added !");

    }

    @Override
    public void modifier(Depot depot) {
        try {

            String req = "UPDATE depot SET location=?, adresse=?, limit_stock=?, quantity_available=? WHERE id=?";
            PreparedStatement statement = cnx.prepareStatement(req);
            statement.setString(1, depot.getLocation());
            statement.setString(2, depot.getAdresse());
            statement.setInt(3, depot.getLimit_stock());
            statement.setInt(4, depot.getStock_available());
            statement.setInt(5, depot.getId());
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " rows updated");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception properly
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query= "DELETE FROM depot WHERE id=?";
        PreparedStatement statement= cnx.prepareStatement(query);
        statement.setInt(1,id);
        statement.executeUpdate();
    }

    @Override
    public Depot getOneById(int id) {
        return null;
    }

    @Override
    public Set<Depot> getAll() {
        return null;
    }
}
