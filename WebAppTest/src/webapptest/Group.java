package webapptest;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mitch McLaughlin
 */
public class Group {
    public String groupID;
    public String groupManagerID;
    public String groupName;
    public List<Member> empIDs;
    public String ID;
    
    public Group(){
        ID = "";
        groupID = "";
        groupManagerID = "";
        groupName = "";
        empIDs = new ArrayList();
    }
    
    public Group(String I, String i, String m, String n, List<Member> e){
        ID = I;
        groupID = i;
        groupManagerID = m;
        groupName = n;
        empIDs = e;
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
    
    void addEmp( Member i){
        empIDs.add(i);
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
    List<Member> getEmps(){
        return empIDs;
    }
    
    
}
