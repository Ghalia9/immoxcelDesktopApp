package tn.esprit.services;

import tn.esprit.models.Materials;
import tn.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceMaterials implements IService<Materials> {

    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Materials materials)
    {
        System.out.println(materials.getDepot_id());
        String req = "INSERT INTO `materials`(`depot_id`, `type_material`,`unit_price`,`quantity`) VALUES (?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1,materials.getDepot_id());
            ps.setString(2,materials.getTypematerials());
            ps.setFloat(3,materials.getUnitprice());
            ps.setInt(4,materials.getQuantity());
            ps.executeUpdate();
            System.out.println("Materials added !");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void modifier(Materials materials) {
        try {

            String req = "UPDATE materials SET  depot_id=?,type_material=?,unit_price=?, quantity=? WHERE id=?";
            PreparedStatement statement = cnx.prepareStatement(req);
            statement.setInt(1, materials.getDepot_id());
            statement.setString(2, materials.getTypematerials());
            statement.setFloat(3, materials.getUnitprice());
            statement.setInt(4, materials.getQuantity());
            statement.setInt(5,materials.getId());
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " rows updated");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception properly
        }

    }

    @Override
    public void supprimer(int id) {
        String query= "DELETE FROM materials WHERE id=?";
        PreparedStatement statement= null;
        try {
            statement = cnx.prepareStatement(query);
            statement.setInt(1,id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Materials getOneById(int id) {
        return null;
    }

    @Override
    public Set<Materials> getAll() {
        return null;
    }
}
