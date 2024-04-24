package Services;

import Entities.Capital;
import Entities.Supplier;
import Entities.Transaction;
import Utils.DataSource;

import java.sql.*;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;

public class ServiceTransaction implements Iservice<Transaction> {
    Connection cnx = DataSource.getInstance().getCnx();
    @Override
    public void ajouter(Transaction transaction) throws SQLException {
        // Retrieve current capital data from the database
        Capital currentCapital = retrieveCurrentCapitalFromDatabase();
        Date utilDate = new Date();

        // Convert java.util.Date to java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());


        // Ensure currentCapital is not null before proceeding
        if (currentCapital == null) {
            System.out.println("Error: Unable to retrieve current capital data from the database.");
            return;
        }

        // Compare currentCapital data with the transaction details
        float totalAmount = transaction.getQuantity() * transaction.getCost();
        String type = transaction.getType();
        if ("Salary".equals(type)){
            if(currentCapital.getSalary() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient salary.");
                return;
            } else {
                currentCapital.setSalary(currentCapital.getSalary() - totalAmount);
                currentCapital.setBig_capital(currentCapital.getBig_capital() - totalAmount);
            }
        } else if ("Expenses".equals(type)){
            if(currentCapital.getExepenses() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient expenses.");
                return;
            } else {
                currentCapital.setExepenses(currentCapital.getExepenses() - totalAmount);
                currentCapital.setBig_capital(currentCapital.getBig_capital() - totalAmount);
            }
        } else {
            currentCapital.setProfits(currentCapital.getProfits() + totalAmount);
            currentCapital.setBig_capital(currentCapital.getBig_capital() + totalAmount);
        }


        transaction.setDate(sqlDate);
        // Insert the transaction into the database
        String insertTransactionQuery = "INSERT INTO expenses (Type,Date_e, quantity_e, Description, coast, totalamount, archived,supplier_id) VALUES (?,?, ?, ?, ?, ?, false,?)";
        try (PreparedStatement ps = cnx.prepareStatement(insertTransactionQuery)) {
//            System.out.println("the supplier inside Transaction add = "+transaction.getSupplier_entity().getCompany_name());
            ps.setString(1, transaction.getType());
            ps.setDate(2, transaction.getDate());
            ps.setFloat(3, transaction.getQuantity());
            ps.setString(4, transaction.getDescription());
            ps.setFloat(5, transaction.getCost());
            ps.setFloat(6, totalAmount);
           ps.setInt(7, transaction.getSupplier_entity().getId_supp());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction added successfully!");
            } else {
                System.out.println("Failed to add transaction!");
                // Update capital entity in the database
                String updateCapitalQuery = "UPDATE capital SET salary=?, expensess=?, profits=?, big_capital=? WHERE id=1";
                try (PreparedStatement updateStatement = cnx.prepareStatement(updateCapitalQuery)) {
                    updateStatement.setFloat(1, currentCapital.getSalary());
                    updateStatement.setFloat(2, currentCapital.getExepenses());
                    updateStatement.setFloat(3, currentCapital.getProfits());
                    updateStatement.setFloat(4, currentCapital.getBig_capital());
                    int rowsAffectedCapital = updateStatement.executeUpdate();
                    if (rowsAffectedCapital <= 0) {
                        System.out.println("Failed to update capital entity.");
                        return;
                    }
                } catch (SQLException e) {
                    System.err.println("Error while updating capital entity: " + e.getMessage());
                    throw e;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while adding transaction: " + e.getMessage());
            throw e;
        }
    }
    @Override
    public void modifier(Transaction transaction) throws SQLException {
        // Retrieve current capital data from the database
        Capital currentCapital = retrieveCurrentCapitalFromDatabase();
        // Ensure currentCapital is not null before proceeding
        if (currentCapital == null) {
            System.out.println("Error: Unable to retrieve current capital data from the database.");
            return;
        }
        // Compare currentCapital data with the transaction details
        float totalAmount = transaction.getQuantity() * transaction.getCost();
        String type = transaction.getType();
        if ("Salary".equals(type)){
            if(currentCapital.getSalary() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient salary.");
                return;
            } else {
                currentCapital.setSalary(currentCapital.getSalary() - totalAmount);
                currentCapital.setBig_capital(currentCapital.getBig_capital() - totalAmount);
            }
        } else if ("Expenses".equals(type)){
            if(currentCapital.getExepenses() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient expenses.");
                return;
            } else {
                currentCapital.setExepenses(currentCapital.getExepenses() - totalAmount);
                currentCapital.setBig_capital(currentCapital.getBig_capital() - totalAmount);
            }
        } else {
            currentCapital.setProfits(currentCapital.getProfits() + totalAmount);
            currentCapital.setBig_capital(currentCapital.getBig_capital() + totalAmount);
        }

        // querry for the updating
        int idsupplier= transaction.getSupplier_entity().getId_supp();
        String req = "UPDATE  expenses  SET type=? , quantity_e=?, Description=?, coast=?, totalamount=? ,supplier_id=?, archived=false  WHERE id=?";
        System.out.println("the id = "+ transaction.getId());
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            System.out.println(" the type is = "+transaction.getType());
            ps.setString(1, transaction.getType());
            ps.setFloat(2, transaction.getQuantity());
            ps.setString(3, transaction.getDescription());
            ps.setFloat(4, transaction.getCost());
            ps.setFloat(5, totalAmount);
            ps.setInt(6,idsupplier);
            ps.setInt(7, transaction.getId());
            System.out.println(" the type is = "+transaction.getType());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction updated successfully!");
                // Update capital entity in the database
                String updateCapitalQuery = "UPDATE capital SET salary=?, expensess=?, profits=?, big_capital=? WHERE id=1";
                try (PreparedStatement updateStatement = cnx.prepareStatement(updateCapitalQuery)) {
                    updateStatement.setFloat(1, currentCapital.getSalary());
                    updateStatement.setFloat(2, currentCapital.getExepenses());
                    updateStatement.setFloat(3, currentCapital.getProfits());
                    updateStatement.setFloat(4, currentCapital.getBig_capital());
                    int rowsAffectedCapital = updateStatement.executeUpdate();
                    if (rowsAffectedCapital <= 0) {
                        System.out.println("Failed to update capital entity.");
                        return;
                    }
                } catch (SQLException e) {
                    System.err.println("Error while updating capital entity: " + e.getMessage());
                    throw e;
                }
            } else {
                System.out.println("Failed to update transaction!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public void supprimer(int id) throws SQLException {
            String req = "DELETE FROM `expenses` WHERE `id` = ?";
            try (PreparedStatement ps = cnx.prepareStatement(req)) {
                ps.setInt(1, id);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Transaction with ID " + id + " deleted successfully!");
                } else {
                    System.out.println("No transaction found with ID " + id);
                    // Vous pouvez jeter une exception ici ou retourner false pour indiquer l'échec.
                }
            } catch (SQLException e) {
                // Gérer correctement l'exception (par exemple, la journaliser, jeter une exception personnalisée).
                System.err.println("Error while deleting transaction: " + e.getMessage());
                throw e;
            }
    }

    @Override
    public void archiver(int id){
        String req = "UPDATE `expenses` SET `archived` = true WHERE `id` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction with ID " + id + " archived successfully!");
            } else {
                System.out.println("No transaction found with ID " + id);
                // Vous pouvez jeter une exception ici ou retourner false pour indiquer l'échec.
            }
        } catch (SQLException e) {
            // Gérer correctement l'exception (par exemple, la journaliser, jeter une exception personnalisée).
            e.printStackTrace();
        }

    }

    @Override
    public Transaction getOneById(int id) {
        Transaction transaction = null;
        String query = "SELECT * FROM expenses WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                // Retrieve values from ResultSet
                int transactionId = res.getInt("id");
                String type = res.getString("type");
                float quantity = res.getFloat("quantity_e");
                float cost = res.getFloat("coast");
                String description = res.getString("Description");

                // Create Transaction object with retrieved values
                transaction = new Transaction(transactionId, type, description, quantity, cost);
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching transaction by ID: " + e.getMessage());

        }
        return transaction;
}

    public Supplier getOneByIdSupplier(int id) {
        Supplier supplier = null;
        String query = "SELECT * FROM supplier WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                // Retrieve values from ResultSet
                int Id = res.getInt("id");
                String companyName = res.getString("company_name");
                String address = res.getString("address");
                String materials = res.getString("materials_s");
                int phone = res.getInt("phone_number");
                String patentRef = res.getString("patent_ref");
                String Image = res.getString("Image");
                // Create Transaction object with retrieved values
                supplier = new Supplier( Id, companyName,address, materials,phone,patentRef,Image) ;

            }
        } catch (SQLException e) {
            System.err.println("Error while fetching transaction by ID: " + e.getMessage());

        }

        return supplier;

    }


    @Override
    public Set<Transaction> getAll()     {
        Set<Transaction> Trans = new HashSet<>();
        // dispaying only not archived data

        String req = " SELECT * FROM expenses WHERE archived=false";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                int id = res.getInt("id");
                java.sql.Date date = res.getDate("date_e");
                String type = res.getString("type");
                float Quantity = res.getFloat("quantity_e");
                float cost = res.getFloat("coast");
                String description  = res.getString("Description");
                float totalAmount = res.getFloat("totalamount");
                Transaction transaction = new Transaction (id,date,type,description,Quantity,cost,totalAmount);
                Trans.add(transaction);
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return Trans;
    }

    // Method to retrieve current capital data from the database
    public Capital retrieveCurrentCapitalFromDatabase() {
        String query = "SELECT salary, expensess, profits, big_capital FROM capital WHERE id = 1";
        Capital capitalEntity = null;

        try (Statement statement = cnx.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                capitalEntity = new Capital();
                capitalEntity.setSalary(resultSet.getFloat("salary"));
                capitalEntity.setExepenses(resultSet.getFloat("expensess"));
                capitalEntity.setProfits(resultSet.getFloat("profits"));
                capitalEntity.setBig_capital(resultSet.getFloat("big_capital"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return capitalEntity;
    }

}
