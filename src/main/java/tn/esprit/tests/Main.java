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
        Employees e= new Employees("John","Doe","M","john.doe@example.com","123 Main St, Anytown","99771066","Développeur",Date.valueOf("1990-01-01"),Date.valueOf("2020-05-01"), Date.valueOf("2023-05-01"),"CDI", 5,null,0,"03456789");
        //sp.ajouter(e);
        //System.out.println(sp.getAll());
        Employees e1= new Employees("Johnyyyy","Doe","M","john.doe@example.com","123 Main St, Anytown","99771066","Développeur",Date.valueOf("1990-01-01"),Date.valueOf("2020-05-01"), Date.valueOf("2023-05-01"),"CDI", 5,null,0,"03456789");
        e1.setId(11);
        se.modifier(e1);
        System.out.println(se.getAll());
        System.out.println(e1.getId());
        //sp.supprimer(e.getId());
        System.out.println(se.getOneById(e1.getId()));
        System.out.println("leaves");
        ServiceLeaves sl=new ServiceLeaves();
       /* Leaves l1= new Leaves(
                "Annual Leave", // Type of leave
                Date.valueOf("2024-04-10"), // Start date of the leave
                Date.valueOf("2024-04-20"), // Finish date of the leave
                "Taking annual leave for vacation", // Description of the leave
                3
        );
        sl.ajouter(l1);*/
    }
}