package tn.esprit.models;

public class Materials {
    private int id;
    private String typematerials;
    private float unitprice;
    private int quantity;

    private int depot_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypematerials() {
        return typematerials;
    }

    public void setTypematerials(String typematerials) {
        this.typematerials = typematerials;
    }

    public float getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(float unitprice) {
        this.unitprice = unitprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDepot_id() {
        return depot_id;
    }

    public void setDepot_id(int depot_id) {
        this.depot_id = depot_id;
    }

    public Materials(int id, String typematerials, float unitprice, int quantity) {
        this.id = id;
        this.typematerials = typematerials;
        this.unitprice = unitprice;
        this.quantity = quantity;

    }

    public Materials(String typematerials,float unitprice,int quantity,int depot) {
        this.typematerials = typematerials;
        this.unitprice = unitprice;
        this.quantity = quantity;
        this.depot_id=depot;
    }

    public Materials(int id, String typematerials, float unitprice, int quantity,int depot) {
        this.id = id;
        this.typematerials = typematerials;
        this.unitprice = unitprice;
        this.quantity = quantity;
        this.depot_id=depot;

    }
}
