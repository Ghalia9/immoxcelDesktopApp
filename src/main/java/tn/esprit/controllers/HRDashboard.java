package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import tn.esprit.models.Leaves;
import tn.esprit.services.ServiceLeaves;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class HRDashboard implements Initializable {
    @FXML
    private HBox cardLayout;
    private List<Leaves> pendingLeaves;

    private final ServiceLeaves sl= new ServiceLeaves();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pendingLeaves=new ArrayList<>(pendingLeaves());
        try{
            for (int i=0;i<pendingLeaves.size();i++){
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/CardLeaves.fxml"));
                HBox cardBox=loader.load();
                AfficherLeaves afficherLeaves=loader.getController();
                afficherLeaves.setData(pendingLeaves.get(i));
                cardLayout.getChildren().add(cardBox);
            }

        }catch (IOException e){
            System.out.println("aaaaaaaaaaaaaaaaaa");
            e.printStackTrace();
        }
    }
    private List<Leaves> pendingLeaves(){
        Set<Leaves> allLeaves=sl.getAll();
        List<Leaves> pl=new ArrayList<>();
        for (Leaves leave : allLeaves) {
            System.out.println(leave);
            //leave.setStatus("Approved");
            if (leave.getStatus().equals("Pending")) {
                pl.add(leave);
            }
        }
        System.out.println("list listinggg");
        System.out.println(allLeaves);
        System.out.println(pl);
        return pl;
    }
}
