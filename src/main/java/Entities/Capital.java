package Entities;

import java.util.Objects;

public class Capital {
    private int Id;
    private float  Salary;
    private float  Exepenses ;
    private float  funds;
    private float  Profits;
    private float  Big_capital;

    public Capital() {
    }

    public Capital(float salary, float exepenses, float funds, float profits, float big_capital) {
        Salary = salary;
        Exepenses = exepenses;
        funds = funds;
        this.Profits = profits;
        this.Big_capital =  salary + exepenses + funds + profits;
    }

    public Capital(int id, float salary, float exepenses, float funds, float profits, float big_capital) {
        Id = id;
        Salary = salary;
        Exepenses = exepenses;
        funds = funds;
        this.Profits = profits;
        this.Big_capital =  salary + exepenses + funds + profits;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public float getSalary() {
        return Salary;
    }

    public void setSalary(float salary) {
        Salary = salary;
    }

    public float getExepenses() {
        return Exepenses;
    }

    public void setExepenses(float exepenses) {
        Exepenses = exepenses;
    }

    public float getFunds() {
        return funds;
    }

    public void setFunds(float funds) {
        funds = funds;
    }

    public float getProfits() {
        return Profits;
    }

    public void setProfits(float profits) {
        this.Profits = profits;
    }

    public float getBig_capital() {
        return Big_capital;
    }

    public void setBig_capital(float big_capital) {
        this.Big_capital = big_capital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capital capital = (Capital) o;
        return Id == capital.Id && Float.compare(Salary, capital.Salary) == 0 && Float.compare(Exepenses, capital.Exepenses) == 0 && Float.compare(funds, capital.funds) == 0 && Float.compare(Profits, capital.Profits) == 0 && Float.compare(Big_capital, capital.Big_capital) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Salary, Exepenses, funds, Profits, Big_capital);
    }
}
