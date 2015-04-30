package com.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This is simply a basic data POJO for use in the serialization routine that we
 * use for transferring JSON from our server to various other areas.
 *
 * @author William
 */
public class DataPOJO {

    private String username = "";
    private String wholeName = "";
    private String passwordHash = "";
    private String returnMessage = "";
    private String secretQuestion = "";
    private String secretAnswer = "";
    private int employeeID = 0;
    private boolean lastActionSuccess = false;
    private boolean isAdmin = false;
    private List<Employee> favorites = new ArrayList<>();
    private List<Group> groupList = new ArrayList<>();
    private List<House> houseList = new ArrayList<>();
    private List<Employee> allEmployees = new ArrayList<>();

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWholeName() {
        return wholeName;
    }

    public void setWholeName(String wholeName) {
        this.wholeName = wholeName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public boolean isLastActionSuccess() {
        return lastActionSuccess;
    }

    public void setLastActionSuccess(boolean lastActionSuccess) {
        this.lastActionSuccess = lastActionSuccess;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<Employee> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(List<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }

    public List<Employee> getFavorites() {
        return favorites;
    }

    public List<House> getHouseList() {
        return houseList;
    }

    public void setHouseList(List<House> houseList) {
        this.houseList = houseList;
    }

    public void setFavorites(List<Employee> favorites) {
        this.favorites = favorites;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

}
