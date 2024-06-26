package tn.esprit.services;



import javafx.scene.control.Alert;
import tn.esprit.models.Capital;
import tn.esprit.models.Transaction;
import tn.esprit.models.Supplier;
import tn.esprit.utils.DataSource;

import java.sql.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ServiceTransaction implements IService<Transaction> {
    Connection cnx = DataSource.getInstance().getCnx();
    Alert alert;
    @Override
    public void ajouter(Transaction transaction)  {
        System.out.println(" i am here ");
        Capital currentCapital = retrieveCurrentCapitalFromDatabase();

        Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        transaction.setDate(sqlDate);

        if (currentCapital == null) {
            System.out.println("Error: Unable to retrieve current capital data from the database.");
            return;
        }
        float totalAmount = transaction.getQuantity() * transaction.getCost();
        String type = transaction.getType();
        if ("Salary".equals(type)){
            if(currentCapital.getSalary() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient salary.");

            } else {
                currentCapital.setSalary(currentCapital.getSalary() - totalAmount);
                currentCapital.setBig_capital(currentCapital.getBig_capital() - totalAmount);
                addTransaction(transaction,currentCapital,totalAmount);
            }
        } else if ("Expenses".equals(type)){
            if(currentCapital.getExepenses() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient expenses.");

            } else {
                currentCapital.setExepenses(currentCapital.getExepenses() - totalAmount);
                currentCapital.setBig_capital(currentCapital.getBig_capital() - totalAmount);
                addTransaction(transaction,currentCapital,totalAmount);
            }
        } else if ("Income".equals(type)){
            currentCapital.setProfits(currentCapital.getProfits() + totalAmount);

            System.out.println("  avant add statement profits equal to  "+ currentCapital.getProfits());
            double newValueProfits=currentCapital.getProfits() * (0.1);
            float insertProfitspart= (float)newValueProfits;
            System.out.println("la valeur de new "+newValueProfits);
            float exepenses= currentCapital.getExepenses()+insertProfitspart;
            float salary= currentCapital.getSalary()+insertProfitspart;

            currentCapital.setBig_capital(currentCapital.getBig_capital() + totalAmount);
            currentCapital.setExepenses(exepenses);
            currentCapital.setSalary(salary);
            addTransaction(transaction,currentCapital,totalAmount);
        }
    }
    @Override
    public void modifier(Transaction transaction) {
        int idTransactionBefore= transaction.getId();
        Transaction transac= getOneById(idTransactionBefore);
        float result , newone;
        float ancientTotalAmount= transac.getCost()* transac.getQuantity();
        Capital currentCapital = retrieveCurrentCapitalFromDatabase();
        if (currentCapital == null) {
            System.out.println("Error: Unable to retrieve current capital data from the database.");
            return;
        }
        float totalAmount = transaction.getQuantity() * transaction.getCost();
        String type = transaction.getType();
        float currentValue= totalAmount;
        if ("Salary".equals(type)){
            if(currentCapital.getSalary() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient salary.");
            } else if (currentCapital.getSalary() == totalAmount){
                System.out.println("nothing to change ");
                currentCapital.setSalary(currentCapital.getSalary());
            }
            else
            {
                result=ancientTotalAmount - totalAmount;
                newone=currentCapital.getSalary()-result;
                currentCapital.setSalary(newone);
                currentCapital.setBig_capital(currentCapital.getBig_capital() - totalAmount);
            }
        } else if ("Expenses".equals(type)){
            if(currentCapital.getExepenses() < totalAmount) {
                System.out.println("You cannot proceed. Insufficient expenses.");
                displayErrorAlert("Insufficient salary, you cannot proceed" );
            }
            else if (currentCapital.getExepenses() == totalAmount){
                System.out.println("nothing to change ");
                currentCapital.setExepenses(currentCapital.getExepenses());
            }
            else
            {
                result=ancientTotalAmount - totalAmount;
                newone=currentCapital.getExepenses()-result;
                currentCapital.setExepenses(newone);
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
                // Updating capital entity in the database
                addCapital(currentCapital);
            } else {
                System.out.println("Failed to update transaction!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void supprimer(int id)  {
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

            }
    }

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
        String query = "SELECT salary,expensess, profits,funds FROM capital WHERE id = 1";
        Capital capitalEntity = null;

        try (Statement statement = cnx.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                capitalEntity = new Capital();
                capitalEntity.setSalary(resultSet.getFloat("salary"));
                capitalEntity.setExepenses(resultSet.getFloat("expensess"));
                capitalEntity.setProfits(resultSet.getFloat("profits"));
                capitalEntity.setBig_capital(resultSet.getFloat("funds"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return capitalEntity;
    }
    public Set<Transaction> getAllArchived(){
        Set<Transaction> Trans = new HashSet<>();
        // dispaying only not archived data

        String req = " SELECT * FROM expenses WHERE archived=true";
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

    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void addTransaction(Transaction transaction, Capital currentCapital,float totalAmount){
        String insertTransactionQuery = "INSERT INTO expenses (Type,Date_e, quantity_e, Description, coast, totalamount, archived,supplier_id) VALUES (?,?, ?, ?, ?, ?, false,?)";
        try (PreparedStatement ps = cnx.prepareStatement(insertTransactionQuery)) {
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
                //updating the database
                addCapital(currentCapital);

            } else {
                System.out.println("Failed to add transaction!");
            }
        } catch (SQLException e) {
            System.err.println("Error while adding transaction: " + e.getMessage());
        }

    }
    private void addCapital(Capital capital){
        String updateCapitalQuery = "UPDATE capital SET salary=?, expensess=?, profits=?, big_capital=? WHERE id=1";
        try (PreparedStatement updateStatement = cnx.prepareStatement(updateCapitalQuery)) {
            updateStatement.setFloat(1, capital.getSalary());
            updateStatement.setFloat(2, capital.getExepenses());
            updateStatement.setFloat(3, capital.getProfits());
            updateStatement.setFloat(4, capital.getBig_capital());
            int rowsAffectedCapital = updateStatement.executeUpdate();
            if (rowsAffectedCapital <= 0) {
                System.out.println("Failed to update capital entity.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error while updating capital entity: " + e.getMessage());

        }

    }

}
