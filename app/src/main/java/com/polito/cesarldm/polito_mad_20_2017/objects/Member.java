package com.polito.cesarldm.polito_mad_20_2017.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CesarLdM on 23/3/17.
 */

@SuppressWarnings("serial")
public class Member implements Serializable {
    public String name;
    public double spent;
    public String id;
    public String email;
     ArrayList<String> expensesList;
     ArrayList<String> groupList;

    public Member(){}

    public Member(String name, double spent, String id, String email, ArrayList<String> expensesList, ArrayList<String> groupList) {
        this.name = name;
        this.spent = spent;
        this.id = id;
        this.email = email;
        this.expensesList = expensesList;
        this.groupList = groupList;
    }

    public Member(String id) {
        super();
        this.id = id;
    }

    public ArrayList<String> getExpensesList() {
        return expensesList;
    }
    public ArrayList<String> getGroupList() {
        return groupList;
    }

    public void setExpenseList(ArrayList<String> expensesList) {
        this.expensesList = expensesList;
    }

    public Member(String id, String email) {
        super();
        this.name="genericName";
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public String getEmail(){
        return email;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public void addGroup(String group){
        groupList.add(group);
    }




    public void setId(String id){
        this.id=id;
    }
    public String getId(){

        return id;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Member other = (Member) obj;
        if (id != other.id)
            return false;
        return true;
    }


}
