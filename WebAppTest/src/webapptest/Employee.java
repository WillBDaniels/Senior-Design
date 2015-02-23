/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapptest;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Mitch McLaughlin
 */
public class Employee {
    public String empName;
    public String empPhone;
    public String mainFacility;
    public int empID;
    public String userName;
    public Boolean manager;
    public Boolean backup;
    
    public Employee(){
        empName = "";
        empPhone = "";
        mainFacility = "";
        empID = 0;
        userName = "";
        manager = false;
        backup = false;
    }
    
    public Employee(String n, String p, String f, int i, String u, Boolean m, Boolean b) {
        empName = n;
        empPhone = p;
        mainFacility = f;
        empID = i;
        userName = u;
        manager = m;
        backup = b;
    }

    //Setters
    public void setName(String n){
        empName = n;
    }
    
    public void setPhone(String p){
        empPhone = p;
    }
    
    public void setFacility(String f){
        mainFacility = f;
    }
    
    public void setID(int i){
        empID = i;
    }
    
    public void setUserName(String u){
        userName = u;
    }
    
    public void setManager(Boolean m){
        manager = m;
    }
    
    public void setBackup(Boolean b){
        backup = b;
    }
    
    //getters
    public String getName(){
        return empName;
    }
    
    public String getPhone(){
        return empPhone;
    }
    
    public String getFacility(){
        return mainFacility;
    }
    
    public int getID(){
        return empID;
    }
    
    public String getUserName(){
        return userName;
    }
    
    public Boolean getManager(){
        return manager;
    }
    
    public Boolean getBackup(){
        return backup;
    }
}
