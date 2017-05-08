package com.polito.cesarldm.polito_mad_20_2017.objects;

import android.text.Editable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by CesarLdM on 23/3/17.
 */

@SuppressWarnings("serial")
public class Group implements Serializable {
    public String name;
    public String creator;
    public double budget;
    ArrayList<String> memberList;
    ArrayList<String> expenseList;
    String id ;


    public Group(String name, double budget, String id, ArrayList<String> memberList,String creator) {
        this.name = name;
        this.budget = budget;
        this.id=id;
        this.memberList=memberList;
        this.creator=creator;

    }
    public Group(){

    }

    public Group(double budget, String name, ArrayList<String> memberList) {
        super();
        this.name=name;
        this.budget = budget;
        this.memberList = memberList;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Group (String name){
    this.name= name;
    this.budget=0;

}

    public ArrayList<String> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(ArrayList<String> expenseList) {
        this.expenseList = expenseList;
    }

    public String getId(){
       return id;
   }

    public double getBudget() {
        return budget;
    }

    public String getName() {
        return name;
    }


    public void setBudget(double budget) {
        this.budget = budget;
    }


    public ArrayList<String> getMemberList() {
        return memberList;
    }


    public void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }

    public void setName(String name) {
        this.name = name;
    }







    //public void setNumberofMembers(int numberofMembers) {
    //    this.numberofMembers = numberofMembers;
    //}
}
