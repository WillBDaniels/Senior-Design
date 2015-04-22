package edu.csci.standalone_server.Structures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author William
 */
public class Shift {

    private String name = "";
    private String time = "";
    private int houseID = 0;
    private int shiftID = 0;
    private int employeeID = 0;

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }
    private List<Employee> asigneeList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Employee> getAsigneeList() {
        return asigneeList;
    }

    public void setAsigneeList(List<Employee> asigneeList) {
        this.asigneeList = asigneeList;
    }

}
