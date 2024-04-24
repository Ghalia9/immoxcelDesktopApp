package Entities;
import java.sql.Date;
import java.util.Objects;
public class Transaction {
    private int id;
    private String Type;
    private float Quantity;
    private float  Cost;
    private float  totalamount;
    private boolean  archived;
    private Date date;

    private Capital Capital_entity;

    private Supplier Supplier_entity;


    public Transaction( String type, String description, float quantity, float cost,Supplier supplier_entity) {

        Type = type;
        Quantity = quantity;
        Cost = cost;
        Supplier_entity = supplier_entity;
        Description = description;
    }

    public Transaction(int id, String type, String description, float quantity, float cost, Supplier supplier1) {
        this.id=id;
        Type = type;
        Quantity = quantity;
        Cost = cost;
        Supplier_entity = supplier1;
        Description = description;
    }

    public Supplier getSupplier_entity() {
        return Supplier_entity;
    }

    public void setSupplier_entity(Supplier supplier_entity) {
        Supplier_entity = supplier_entity;
    }

    public Capital getCapital_entity() {
        return Capital_entity;
    }

    public void setCapital_entity(Capital capital_entity) {
        Capital_entity = capital_entity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTotalamount(float totalamount) {
        this.totalamount = totalamount;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    private String Description;

    public float getTotalamount() {
        return totalamount;
    }

    public boolean isArchived() {
        return archived;
    }

    public Transaction(){

    }
    public Transaction(int id ,String type,String description,float Quantity, float cost){
        this.id=id;
        this.Type=type;
        this.Description=description;
        this.Quantity=Quantity;
        this.Cost=cost;
    }
    public Transaction(String type,String description,float Quantity, float cost){
        this.Type=type;
        this.Description=description;
        this.Quantity=Quantity;
        this.Cost=cost;
    }
    public Transaction(int id,Date date,String type ,String Description , float Quantity,float cost ,float totalAmount){
        this.id = id;
        this.Type=type;
        this.Quantity=Quantity;
        this.Cost=cost;
        this.Description=Description;
        this.totalamount=totalAmount;
        this.date=date;
    }

  /*  public Transaction(String Type, float Quantity,float cost ,String Description,boolean archived,float totalamount ,Date date) {
        this.Type=Type;
        this.Quantity=Quantity;
        this.Cost=cost;
        this.Description=Description;
        this.archived=archived;
        this.totalamount=totalamount;
        this.date=date;
    }*/


    public int getId() {
        return id;
    }

    public String getType() {
        return Type;
    }

    public float getQuantity() {
        return Quantity;
    }

    public float getCost() {
        return Cost;
    }

    public String getDescription() {
        return Description;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public void setCost(float cost) {
        Cost = cost;
    }

    public void setDescription(String description) {
        Description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Quantity == that.Quantity && Float.compare(Cost, that.Cost) == 0 && Objects.equals(Type, that.Type) && Objects.equals(Description, that.Description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Type, Quantity, Cost, Description);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                ", Type='" + Type + '\'' +
                ", Quantity=" + Quantity +
                ", Cost=" + Cost +
                ", Description='" + Description + '\'' +
                '}';
    }
}
