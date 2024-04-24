package tn.esprit.tests;

import tn.esprit.models.Employees;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceEmployees;
import tn.esprit.services.ServiceLeaves;
import tn.esprit.utils.DataSource;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Connection;
import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        Connection cnx = DataSource.getInstance().getCnx();
        ServiceEmployees se = new ServiceEmployees();
        Employees e= new Employees("John","Doe","M","john.doe@example.com","123 Main St, Anytown","99771066","Développeur",Date.valueOf("1990-01-01"),Date.valueOf("2020-05-01"), Date.valueOf("2023-05-01"),"CDI", 5,null,0,"03456700");
        se.ajouter(e);
        Leaves[] leave = new Leaves[3]; // Create an array of leaves with size 3
        for (int i = 0; i < leave.length; i++) {
            leave[i] = new Leaves(
                    "Annual Leave", // Type of leave
                    Date.valueOf("2024-04-10"), // Start date of the leave
                    Date.valueOf("2024-04-20"), // Finish date of the leave
                    "Pending",
                    "Taking annual leave for vacation", // Description of the leave
                    e);
        }
        e.setListofleaves(leave);
System.out.println(e);
        //se.supprimer(6);
        //System.out.println(sp.getAll());
        //Employees e1= new Employees("Johnyyyy","Doe","M","john.doe@example.com","123 Main St, Anytown","99771066","Développeur",Date.valueOf("1990-01-01"),Date.valueOf("2020-05-01"), Date.valueOf("2023-05-01"),"CDI", 5,null,0,"03456789");
    //   se.ajouter(e1);
        /*e1.setId(11);
        se.modifier(e1);
        System.out.println(se.getAll());
        System.out.println(e1.getId());
        //sp.supprimer(e.getId());
        System.out.println(se.getOneById(e1.getId()));
        System.out.println("leaves");
        ServiceLeaves sl=new ServiceLeaves();

        sl.ajouter(l1);*/
    }
}