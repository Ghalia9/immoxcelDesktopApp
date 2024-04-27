package tn.esprit.models;

public class Depot {
    private int id ;
    private String location;
    private String adresse ;
    private int limit_stock;
    private int stock_available;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getLimit_stock() {
        return limit_stock;
    }

    public void setLimit_stock(int limit_stock) {
        this.limit_stock = limit_stock;
    }

    public int getStock_available() {
        return stock_available;
    }

    public void setStock_available(int stock_available) {
        this.stock_available = stock_available;
    }
    public Depot(int id,String location,String adresse,int limit_stock,int stock_available) {
        this.id = id;
        this.location = location;
        this.adresse = adresse;
        this.limit_stock = limit_stock;
        this.stock_available = stock_available;
    }

    public Depot(String location,String adresse,int limit_stock,int stock_available) {
        this.location = location;
        this.adresse = adresse;
        this.limit_stock = limit_stock;
        this.stock_available = stock_available;
    }
}
