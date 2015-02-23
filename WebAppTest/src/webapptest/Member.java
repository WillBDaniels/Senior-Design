/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapptest;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fritz
 */
public class Member {
    public String groupID;
    public String groupManagerID;
    public String groupName;
    public String empID;
    public String ID;
    
    public Member(){
        ID = "";
        groupID = "";
        groupManagerID = "";
        groupName = "";
        empID = "";
    }
    
    public Member(String I, String i, String m, String n, String e){
        ID = I;
        groupID = i;
        groupManagerID = m;
        groupName = n;
        empID = e;
    }
            
    
    //Setters
    void setGroupID(String i){
        groupID = i;
    }
    
    void setID(String i){
        ID = i;
    }
    
    void setManager(String m){
        groupManagerID = m;
    }
    
    void setName(String n){
        groupName = n;
    }
    
    void addEmp( String i){
        empID = i;
    }
    
    //Getters
    String getGroupID(){
        return groupID;
    }
    
    String getID(){
        return ID;
    }
    String getManager(){
        return groupManagerID;
    }
    String getName(){
        return groupName;
    }
    String getEmps(){
        return empID;
    }
}
