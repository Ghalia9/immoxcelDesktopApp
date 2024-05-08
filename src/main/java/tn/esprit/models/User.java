package tn.esprit.models;

import tn.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String Password;
    private String Username;
    private String Email;
    private int emp_id;

    private String FullName;
    private String role;

    public User() {

    }

    public User(int id,String password,String Username,String Email,int emp_id) {
        this.id=id;
        this.Password = password;
        this.Username = Username;
        this.Email = Email;
        this.emp_id = emp_id;
    }

    public User(String password,String Username,String Email,int emp_id) {
        this.Password = password;
        this.Username = Username;
        this.Email = Email;
        this.emp_id = emp_id;
    }

    public User(String Username,String role,String FullNAme) {
        this.Username = Username;
        this.role=role;
        this.FullName=FullNAme;
    }

    public int getId(){return id;}
    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return Username;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setRole(String role){ this.role=role;}

    public String getRole(){ return this.role;}
    public void setFullName(String name){ this.FullName=name;}

    public String getFullName(){ return this.FullName;}

}
