package tn.esprit.tests;

import tn.esprit.models.Projects;
import tn.esprit.services.ServiceProjects;
import tn.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Connection cnx = DataSource.getInstance().getCnx();
        ServiceProjects test = new ServiceProjects();


        //Projects bissmilleh = new Projects("bissmelleh", Date.valueOf("2023-02-22"), Date.valueOf("2023-02-22"), Date.valueOf("2023-02-22"),223.11f, 11112.32f);
        //test.ajouter(bissmilleh);
        //test.supprimer(32);
        System.out.println(test.getAll());
    }
}