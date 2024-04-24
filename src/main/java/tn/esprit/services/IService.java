package tn.esprit.services;

import tn.esprit.models.Employees;

import java.sql.SQLException;
import java.util.Set;

public interface IService <T> {
    //void ajouter(Employees employee);

    //void modifier(Employees employee);

    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(int id) throws SQLException;
    public T getOneById(int id);
    public Set<T> getAll();
}
