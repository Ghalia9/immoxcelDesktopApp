package tn.esprit.services;

import tn.esprit.models.Employees;

import java.sql.SQLException;
import java.util.Set;

public interface IService <T> {
    public void ajouter(T t);
    public void modifier(T t);
    public void supprimer(int id);
    public T getOneById(int id);
    public Set<T> getAll();
}
