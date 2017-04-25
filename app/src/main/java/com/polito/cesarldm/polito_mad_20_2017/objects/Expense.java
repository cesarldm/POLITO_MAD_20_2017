package com.polito.cesarldm.polito_mad_20_2017.objects;

/**
 * Created by CesarLdM on 14/4/17.
 */

public class Expense {
    public String name;
    public double cost;

    public Expense(String name) {
        this.name = name;
        this.cost=0;
    }
    public Expense(double cost){
        this.cost=cost;
        this.name="Generic expense";
    }

    public Expense(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public Expense() {
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
