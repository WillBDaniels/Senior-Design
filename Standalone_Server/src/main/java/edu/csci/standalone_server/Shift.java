package edu.csci.standalone_server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author William
 */
public class Shift {

    private String name = "";
    private String time = "";
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
