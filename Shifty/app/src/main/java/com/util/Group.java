package com.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author William
 */
public class Group {

    private int groupID = 0;
    private int managerID = 0;
    private String groupName = "";
    private List<Employee> empList = new ArrayList<>();

    public List<Employee> getEmpList() {
        return empList;
    }

    public void setEmpList(List<Employee> empList) {
        this.empList = empList;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
