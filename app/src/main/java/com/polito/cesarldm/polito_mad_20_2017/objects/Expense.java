package com.polito.cesarldm.polito_mad_20_2017.objects;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by CesarLdM on 14/4/17.
 */

public class Expense {
    public String name;
    public String who;
    public double cost;
    public String date;
    public String id;


    public Expense(String name) {
        this.name = name;
        this.cost=0;
        this.date= DateFormat.getDateTimeInstance().format(new Date());
    }
    public Expense(double cost){
        this.cost=cost;
        this.name="Generic expense";
        this.date= DateFormat.getDateTimeInstance().format(new Date());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Expense(String name, double cost, String who,String id) {

        this.name = name;
        this.cost = cost;
        this.who=who;
       this.date= DateFormat.getDateInstance().format(new Date());
        this.id=id;
    }

    public Expense() {
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
