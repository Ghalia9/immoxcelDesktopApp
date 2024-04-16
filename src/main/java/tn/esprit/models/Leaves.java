package tn.esprit.models;

import java.sql.Date;
import java.util.Objects;

public class Leaves {
    private int id;
    private String leaveType;
    private Date startDate;
    private Date finishDate;
    private String status;
    private String leaveDescription;

    private int employee_id;
    public Leaves() {
    }

    public Leaves(int id, String leaveType, Date startDate, Date finishDate,String status, String leaveDescription,int employee_id) {
        this.id = id;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
        this.leaveDescription = leaveDescription;
        this.employee_id=employee_id;
    }

    public Leaves(String leaveType, Date startDate, Date finishDate,String status, String leaveDescription,int employee_id) {
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
        this.leaveDescription = leaveDescription;
        this.employee_id=employee_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveDescription() {
        return leaveDescription;
    }

    public void setLeaveDescription(String leaveDescription) {
        this.leaveDescription = leaveDescription;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    @Override
    public String toString() {
        return "Leaves{" +
                "leaveType='" + leaveType + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", status='" + status + '\'' +
                ", leaveDescription='" + leaveDescription + '\'' +
                ", employee_id='" + employee_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaves leaves = (Leaves) o;
        return id == leaves.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
