/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapptest;

/**
 *
 * @author fritz
 */
public class Facility {
    public String houseID;
    public String houseNeighborhood;
    
    public Facility(){
        houseID = "";
        houseNeighborhood = "";
    }
    
    public Facility(String i, String n){
        houseID = i;
        houseNeighborhood = n;
    }
    
    //Setters
    void setHouseID(String i){
        houseID = i;
    }
    
    void setHouseNeighborhood(String n){
        houseNeighborhood = n;
    }
    
    //Getters
    String getHouseID(){
        return houseID;
    }
    
    String getHouseNeighborhood(){
        return houseNeighborhood;
    }
    
}
