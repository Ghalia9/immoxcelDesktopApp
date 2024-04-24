package Services;

import Entities.Supplier;
import Entities.Transaction;
import Utils.DataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServiceSupplier implements Iservice <Supplier> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Supplier supplier) throws SQLException {
        String req = "INSERT INTO `supplier`( `company_name`, `address`,`materials_s`,`phone_number`,`patent_ref`,`Image`) VALUES (?,?,?,?,?,?) ";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, supplier.getCompany_name());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getMaterails_s());
            ps.setInt(4, supplier.getPhone_number());
            ps.setString(5, supplier.getPatent_ref());
            ps.setString(6, supplier.getImage());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Supplier added successfully!");
            } else {
                System.out.println("Failed to add Supplier!");
                // You might want to throw an exception here or return false to indicate failure.
            }

        } catch (SQLException e) {
            // Properly handle the exception (e.g., log it, throw a custom exception).
            System.err.println("Error while adding Supplier : " + e.getMessage());
            throw e;
        }


    }

    @Override
    public void modifier(Supplier supplier) throws SQLException {
        String req = "UPDATE supplier SET company_name=?, address=?, materials_s=?, phone_number=?, patent_ref=?, Image=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, supplier.getCompany_name());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getMaterails_s());
            ps.setInt(4, supplier.getPhone_number());
            ps.setString(5, supplier.getPatent_ref());
            ps.setString(6, supplier.getImage());
            ps.setInt(7,supplier.getId_supp());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Supplier updated successfully!");
            } else {
                System.out.println("Failed to update Supplier!");
                // You might want to throw an exception here or return false to indicate failure.
            }

        } catch (SQLException e) {
            // Properly handle the exception (e.g., log it, throw a custom exception).
            System.err.println("Error while Updating Supplier : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `supplier` WHERE `id` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Supplier with ID " + id + " deleted successfully!");
            } else {
                System.out.println("No Spplier found with ID " + id);
                // Vous pouvez jeter une exception ici ou retourner false pour indiquer l'échec.
            }
        } catch (SQLException e) {
            // Gérer correctement l'exception (par exemple, la journaliser, jeter une exception personnalisée).
            System.err.println("Error while deleting Supplier: " + e.getMessage());
            throw e;
        }

    }

    @Override
    public void archiver(int id) throws SQLException {

    }

    @Override
    public Supplier getOneById(int id)  {
        Supplier supplier = null;
        String query = "SELECT * FROM supplier WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                // Retrieve values from ResultSet
                int supplierId = res.getInt("id");
                String companyName = res.getString("company_name");
                String address = res.getString("address");
                String materials = res.getString("materials_s");
                int phoneNumber = res.getInt("phone_number");
                String patentRef = res.getString("patent_ref");
                String image = res.getString("Image");
                // Create Supplier object with retrieved values
                supplier = new Supplier(supplierId, companyName, materials, address, phoneNumber, patentRef, image);
            }
            // Close resources
            res.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error while fetching Supplier by ID: " + e.getMessage());
            e.printStackTrace(); // This should ideally be logged instead of printed to console
        }
        return supplier;
    }

    @Override
    public Set<Supplier> getAll()  {
        Set<Supplier> sup = new HashSet<>();
        String req = " Select * from supplier ";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                int id = res.getInt("id");
                String companyName = res.getString("company_name");
                String address = res.getString("address");
                String materials_s = res.getString("materials_s");
                int phone_number = res.getInt("phone_number");
                String patent_ref = res.getString("patent_ref");
                String image= res.getString("Image");
                Supplier p= new Supplier(id,companyName,materials_s,address,phone_number,patent_ref,image );
                sup.add(p);
            }
            // Close resources to prevent memory leaks
            res.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sup;
    }
    public Set<Supplier> getName()  {
        Set<Supplier> sup = new HashSet<>();
        String req = " Select * from supplier ";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                int id = res.getInt("id");
                String companyName = res.getString("company_name");
                Supplier p= new Supplier(companyName,id );
                sup.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sup;
    }
    public Map<String, Integer> getSupplierNameAndIdMap() {
        Map<String, Integer> supplierMap = new HashMap<>();
        String req = "SELECT id, company_name FROM supplier";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                int id = res.getInt("id");
                String companyName = res.getString("company_name");
                supplierMap.put(companyName, id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return supplierMap;
    }
}
