package tn.esprit.models;

import java.util.Collections;
import java.sql.Date;
import java.util.Objects;

public class Projects {
    private int id;
    private String project_name;
    private Date date_pred_start;
    private Date date_pred_finish;
    private Date date_completion;
    private float budget;
    private float actual_cost;

    private Tasks[] listoftasks;

    public Projects(String project_name, Date date_pred_start, Date date_pred_finish, float budget) {
        this.project_name = project_name;
        this.date_pred_start = date_pred_start;
        this.date_pred_finish = date_pred_finish;
        this.budget = budget;
    }

    public Projects(int id, String project_name, Date date_pred_start, Date date_pred_finish, Date date_completion, float budget, float actual_cost) {
        this.id = id;
        this.project_name = project_name;
        this.date_pred_start = date_pred_start;
        this.date_pred_finish = date_pred_finish;
        this.date_completion = date_completion;
        this.budget = budget;
        this.actual_cost = actual_cost;
    }

    public int getId() {
        return id;
    }

    public String getProject_name() {
        return project_name;
    }

    public Date getDate_pred_start() {
        return date_pred_start;
    }

    public Date getDate_pred_finish() {
        return date_pred_finish;
    }

    public Date getDate_completion() {
        return date_completion;
    }

    public float getBudget() {
        return budget;
    }

    public float getActual_cost() {
        return actual_cost;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setDate_pred_start(Date date_pred_start) {
        this.date_pred_start = date_pred_start;
    }

    public void setDate_pred_finish(Date date_pred_finish) {
        this.date_pred_finish = date_pred_finish;
    }

    public void setDate_completion(Date date_completion) {
        this.date_completion = date_completion;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public Tasks[] getListoftasks() {
        return listoftasks;
    }

    public void setListoftasks(Tasks[] listoftasks) {
        this.listoftasks = listoftasks;
    }

    public void setActual_cost(float actual_cost) {
        this.actual_cost = actual_cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projects projects = (Projects) o;
        return Objects.equals(project_name, projects.project_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project_name);
    }

    @Override
    public String toString() {
        return "Projects{" +
                ", project_name='" + project_name + '\'' +
                ", date_pred_start=" + date_pred_start +
                ", date_pred_finish=" + date_pred_finish +
                ", date_completion=" + date_completion +
                ", budget=" + budget +
                ", actual_cost=" + actual_cost +
                ", listoftasks=" + listoftasks +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public Projects() {
    }
}
