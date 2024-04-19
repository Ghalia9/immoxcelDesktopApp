package tn.esprit.models;

import java.util.Date;
import java.util.Objects;

public class Tasks {
    private int id;
    private String title;
    private String description;
    private Date deadline;

    private String status;
    private Date completion_date;

    public Tasks(int id, String title, String description, Date deadline, String status, Date completion_date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.completion_date = completion_date;
    }

    public Tasks(String title, String description, Date deadline, String status, Date completion_date) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.completion_date = completion_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(Date completion_date) {
        this.completion_date = completion_date;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tasks tasks = (Tasks) o;
        return Objects.equals(title, tasks.title) && Objects.equals(description, tasks.description) && Objects.equals(deadline, tasks.deadline) && Objects.equals(status, tasks.status) && Objects.equals(completion_date, tasks.completion_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, deadline, status, completion_date);
    }

    @Override
    public String toString() {
        return "Tasks{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", status='" + status + '\'' +
                ", completion_date=" + completion_date +
                '}';
    }
}
