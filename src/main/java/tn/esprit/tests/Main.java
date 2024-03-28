package tn.esprit.tests;

import tn.esprit.utils.DataSource;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection cnx = DataSource.getInstance().getCnx();

    }
}