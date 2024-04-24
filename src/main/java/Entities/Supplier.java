package Entities;

import java.util.Objects;

public class Supplier {

    private int Id_supp;
    private String Company_name;
    private String Address;
    private String Materails_s;
    private int Phone_number;
    private String Patent_ref;



    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    private String Image;

    public Supplier() {

    }

    public Supplier(String company_name, String address, String materails_s, int phone_number, String patent_ref,String Image){
        this.Company_name = company_name;
        this.Address = address;
        this.Materails_s = materails_s;
        this.Phone_number = phone_number;
        this.Patent_ref = patent_ref;
        this.Image=Image;
    }

    public Supplier(String company_name,int id) {
        this.Company_name = company_name;
        this.Id_supp=id;
    }
    /*public Supplier(String image,int id ) {
        this.Image = image;
    }*/

    public Supplier(int id_supp, String company_name, String address, String materails_s, int phone_number, String patent_ref,String Image) {
        this.Id_supp = id_supp;
        this.Company_name = company_name;
        this.Address = address;
        this.Materails_s = materails_s;
        this.Phone_number = phone_number;
        this.Patent_ref = patent_ref;
        this.Image = Image;
    }

    public int getId_supp() {
        return Id_supp;
    }

    public void setId_supp(int id_supp) {
        Id_supp = id_supp;
    }

    public String getCompany_name() {
        return Company_name;
    }

    public void setCompany_name(String company_name) {
        Company_name = company_name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMaterails_s() {
        return Materails_s;
    }

    public void setMaterails_s(String materails_s) {
        this.Materails_s = materails_s;
    }

    public int getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.Phone_number = phone_number;
    }

    public String getPatent_ref() {
        return Patent_ref;
    }

    public void setPatent_ref(String patent_ref) {
        this.Patent_ref = patent_ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier that = (Supplier) o;
        return Id_supp == that.Id_supp && Phone_number == that.Phone_number  && Objects.equals(Company_name, that.Company_name) && Objects.equals(Materails_s, that.Materails_s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id_supp, Company_name, Materails_s, Address, Phone_number, Patent_ref);
    }


}
