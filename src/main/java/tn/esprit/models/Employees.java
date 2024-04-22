package tn.esprit.models;

import tn.esprit.services.ServiceEmployees;

import java.sql.Blob;
import java.util.*;
import java.sql.Date;

public class Employees {
    private int id;
    private String empName;
    private String empLastName;
    private String empSex;
    private String empEmail;
    private String empAddress;
    private String empPhone;
    private String empFunction;
    private Date birthDate;
    private Date hireDate;
    private Date endContractDate;
    private String contractType;
    private int allowedLeaveDays;
    private Blob empCV;
    private int empTakenLeaves;
    private String empCin;
    private Leaves[] listofleaves;

    public Employees(String john, String doe, String m, String mail, String s, String string, String développeur, Date date, Date valueOf, Date value, String cdi, int i, String s1, String number){

    }

    public Employees(int id, String empName, String empLastName, String empSex, String empEmail, String empAddress, String empPhone, String empFunction, Date birthDate, Date hireDate, Date endContractDate, String contractType, int allowedLeaveDays, Blob empCV, int empTakenLeaves,String empCin) {
        this.id = id;
        this.empName = empName;
        this.empLastName = empLastName;
        this.empSex = empSex;
        this.empEmail = empEmail;
        this.empAddress = empAddress;
        this.empPhone = empPhone;
        this.empFunction = empFunction;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.endContractDate = endContractDate;
        this.contractType = contractType;
        this.allowedLeaveDays = allowedLeaveDays;
        this.empCV = empCV;
        this.empTakenLeaves = empTakenLeaves;
        this.empCin=empCin;
        listofleaves=new Leaves[allowedLeaveDays];
    }

    public Employees(String empName, String empLastName, String empSex, String empEmail, String empAddress, String empPhone, String empFunction, Date birthDate, Date hireDate, Date endContractDate, String contractType, int allowedLeaveDays, Blob empCV, int empTakenLeaves,String empCin) {
        this.empName = empName;
        this.empLastName = empLastName;
        this.empSex = empSex;
        this.empEmail = empEmail;
        this.empAddress = empAddress;
        this.empPhone = empPhone;
        this.empFunction = empFunction;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.endContractDate = endContractDate;
        this.contractType = contractType;
        this.allowedLeaveDays = allowedLeaveDays;
        this.empCV = empCV;
        this.empTakenLeaves = empTakenLeaves;
        this.empCin=empCin;
        listofleaves=new Leaves[allowedLeaveDays];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public String getEmpSex() {
        return empSex;
    }

    public void setEmpSex(String empSex) {
        this.empSex = empSex;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpFunction() {
        return empFunction;
    }

    public void setEmpFunction(String empFunction) {
        this.empFunction = empFunction;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getEndContractDate() {
        return endContractDate;
    }

    public void setEndContractDate(Date endContractDate) {
        this.endContractDate = endContractDate;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public int getAllowedLeaveDays() {
        return allowedLeaveDays;
    }

    public void setAllowedLeaveDays(int allowedLeaveDays) {
        this.allowedLeaveDays = allowedLeaveDays;
    }

    public Blob getEmpCV() {
        return empCV;
    }

    public void setEmpCV(Blob empCV) {
        this.empCV = empCV;
    }

    public int getEmpTakenLeaves() {
        return empTakenLeaves;
    }

    public void setEmpTakenLeaves(int empTakenLeaves) {
        this.empTakenLeaves = empTakenLeaves;
    }

    public String getEmpCin() {
        return empCin;
    }

    public void setEmpCin(String empCin) {
        this.empCin = empCin;
    }

    public Leaves[] getListofleaves() {
        return listofleaves;
    }

    public void setListofleaves(Leaves[] listofleaves) {
        this.listofleaves = listofleaves;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "id=" + id +
                ", empName='" + empName + '\'' +
                ", empLastName='" + empLastName + '\'' +
                ", empSex='" + empSex + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", empAddress='" + empAddress + '\'' +
                ", empPhone='" + empPhone + '\'' +
                ", empFunction='" + empFunction + '\'' +
                ", birthDate=" + birthDate +
                ", hireDate=" + hireDate +
                ", endContractDate=" + endContractDate +
                ", contractType='" + contractType + '\'' +
                ", allowedLeaveDays=" + allowedLeaveDays +
                ", empCV=" + empCV +
                ", empTakenLeaves=" + empTakenLeaves +
                ", empCin='" + empCin + '\'' +
                ", listofleaves=" + Arrays.toString(listofleaves) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employees employees = (Employees) o;
        return Objects.equals(empCin, employees.empCin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(empCin);
    }
    public boolean exists(Employees employee){
        final ServiceEmployees se = new ServiceEmployees();
        Set<Employees> setemployees= se.getAll();
        List<Employees> employeesList=new ArrayList<>(setemployees);
        for (int i=0;i<employeesList.size();i++) {
            if (employeesList.get(i).equals(employee))
                return true;
        }
        return false;
    }
}
