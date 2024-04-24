package tn.esprit.services;

import tn.esprit.models.Employees;

import java.util.Set;
import java.sql.SQLException;
public interface IService <T> {
   /* void ajouter(Employees employee);

    void modifier(Employees employee);*/

    public void ajouter(T t)throws SQLException;
    public void modifier(T t);
    public void supprimer(int id) throws SQLException;
    public T getOneById(int id);
    public Set<T> getAll();
}
