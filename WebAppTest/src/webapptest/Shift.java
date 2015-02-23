package webapptest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mitch McLaughlin
 */
public class Shift {
    public String shiftID;
    public String shiftTime;
    public String houseID;
    public String empID;
    
    public Shift(){
        shiftID = "";
        shiftTime = "";
        houseID = "";
        empID = "";
    }
    
    public Shift(String sID, String sT, String hID, String eID) {
        shiftID = sID;
        shiftTime = sT;
        houseID = hID;
        empID = eID;
    }
    
    //Setters
    public void setShiftID(String s){
        shiftID = s;
    }
    
    public void setShiftTime(String t){
        shiftTime = t;
    }
    
    public void setHouseID(String h){
        houseID = h;
    }
    
    public void setEmpID(String e){
        empID = e;
    }
    
    //Getters
    String getShiftID(){
        return shiftID;
    }
    
    String getShiftTime(){
        return shiftTime;
    }
    
    String getHouseID(){
        return houseID;
    }
    
    String getEmpID(){
        return empID;
    }
}
