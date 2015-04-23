package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import edu.csci.standalone_server.Structures.Employee;
import edu.csci.standalone_server.Structures.Group;
import edu.csci.standalone_server.Structures.House;
import edu.csci.standalone_server.Structures.Shift;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author William
 */
public class HandleGetAllData extends JSONHandler {

    /**
     * This public constructor takes in a json string, as well as a HTTPExchange
     * client object, and then builds the initial jsonMap with the provided
     * items by de-serializing the json that is input.
     *
     * @param json the string representation of the entire JSON information
     * being sent by the client.
     * @param client The HTTPExchange client object being used
     */
    public HandleGetAllData(String json, HttpExchange client) {
        super(json, client);
    }

    /**
     * This public abstract method is for building the JSON response we will
     * send to the client.
     *
     * @return a String that represents the entire JSON object to send to the
     * client.
     */
    @Override
    public String buildResponse() {
        return fillDataPojo();
    }

    /**
     * This method gathers loads of data from our database and aggregates it all
     * into one single POJO object that we can send back to the client user.
     *
     * @param inputPOJO
     */
    private String fillDataPojo() {
        Gson gson = new Gson();
        DataPOJO inputPOJO = new DataPOJO();
        try {
            String query;
            ResultSet rs;
            ResultSet rsInner;

            //possible bug if the db doesn't line this up properly, we're expecting this to be de-duped.
            //if it's not.. well, it'll pick the first one. Something to remember at least.
            PreparedStatement pstmt;
            query = "SELECT * FROM `groups`;";
            if (inputPOJO.getEmployeeID() == 0) {
                query = "SELECT * FROM `groups` WHERE manager_id = ? ;";
            }
            pstmt = con.prepareStatement(query);
            if (inputPOJO.getEmployeeID() == 0) {
                pstmt.setInt(1, inputPOJO.getEmployeeID());
            }
            //System.out.println("This is the query: " + query);
            rs = pstmt.executeQuery();
            List<Group> groupList = new ArrayList<>();
            while (rs != null && rs.next()) {
                Group group = new Group();
                group.setGroupID(rs.getInt("group_id"));
                group.setGroupName(rs.getString("group_name"));
                group.setManagerID(rs.getInt("manager_id"));
                String innerQuery = "SELECT * FROM `groups` WHERE `groups`.group_id = ?;";
                PreparedStatement pstmtInner = con.prepareStatement(innerQuery);
                pstmtInner.setInt(1, group.getGroupID());
                ResultSet rsInner2 = pstmtInner.executeQuery();
                List<Employee> empList = new ArrayList<>();
                while (rsInner2 != null && rsInner2.next()) {
                    innerQuery = "SELECT * FROM employee WHERE employee_id= ?;";
                    PreparedStatement pstmt3 = con.prepareStatement(innerQuery);
                    pstmt3.setInt(1, rs.getInt("employee_id"));
                    ResultSet rsInner3 = pstmt3.executeQuery();
                    while (rsInner3 != null && rsInner3.next()) {
                        Employee emp = new Employee();
                        emp.setEmployeeID(rsInner3.getInt("employee_id"));
                        emp.setIsBackup(rsInner3.getBoolean("is_backup"));
                        emp.setName(rsInner3.getString("employee_name"));
                        emp.setUsername(rsInner3.getString("username"));
                        emp.setPhoneNumber(rsInner3.getString("phone_number"));
                        empList.add(emp);
                    }
                }
                group.setEmpList(empList);
                groupList.add(group);
            }
            inputPOJO.setGroupList(groupList);
            query = "SELECT * FROM house;";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            List<House> houseList = new ArrayList<>();
            while (rs != null && rs.next()) {
                House temp = new House();
                temp.setHouseID(rs.getInt("house_id"));
                temp.setHouseLocation(rs.getString("house_location"));
                temp.setIsActive(rs.getBoolean(("is_active")));
                temp.setHouseName(rs.getString("house_name"));
                //get everything we need from houses and such.. yea.
                query = "SELECT * FROM shift WHERE house_id=?;";
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1, rs.getInt("house_id"));
                rsInner = pstmt.executeQuery();
                List<Shift> shiftList = new ArrayList<>();
                while (rsInner != null && rsInner.next()) {
                    PreparedStatement pstmt2;
                    Shift tempInner = new Shift();
                    tempInner.setName(rsInner.getString("shift_name"));
                    tempInner.setTime(rsInner.getString("shift_time"));
                    tempInner.setEmployeeID(rsInner.getInt("employee_id"));
                    tempInner.setHouseID(rsInner.getInt("house_id"));
                    tempInner.setShiftID(rsInner.getInt("shift_id"));
                    String query2 = "SELECT * FROM asignee_list, (SELECT * FROM employee) AS t WHERE shift_id =? AND t.employee_id = `asignee_list`.employee_id;";
                    pstmt2 = con.prepareStatement(query2);
                    pstmt2.setInt(1, rsInner.getInt("shift_id"));
                    ResultSet rsInner2 = pstmt2.executeQuery();
                    List<Employee> empList = new ArrayList<>();
                    while (rsInner2 != null && rsInner2.next()) {
                        Employee asignee = new Employee();
                        asignee.setEmployeeID(rsInner2.getInt("employee_id"));
                        asignee.setIsBackup(rsInner2.getBoolean("is_backup"));
                        asignee.setName(rsInner2.getString("employee_name"));
                        asignee.setPhoneNumber(rsInner2.getString("phone_number"));
                        empList.add(asignee);
                    }
                    tempInner.setAsigneeList(empList);
                    shiftList.add(tempInner);
                    //Set all of the shifts for this house. coolio.
                }
                temp.setShiftList(shiftList);
                houseList.add(temp);
            }
            inputPOJO.setHouseList(houseList);

            //Building up the list of all the employees.
            query = "SELECT * FROM employee;";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            List<Employee> employeeList = new ArrayList<>();
            Employee temp;
            while (rs != null && rs.next()) {
                temp = new Employee();
                temp.setEmployeeID(rs.getInt("employee_id"));
                temp.setIsBackup(rs.getBoolean("is_backup"));
                temp.setName(rs.getString("employee_name"));
                temp.setUsername(rs.getString("username"));
                temp.setIsManager(rs.getBoolean("is_manager"));
                temp.setIsAdmin(rs.getBoolean("is_admin"));
                temp.setPhoneNumber(rs.getString("phone_number"));
                employeeList.add(temp);
            }
            inputPOJO.setAllEmployees(employeeList);

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
        return gson.toJson(inputPOJO, DataPOJO.class);
    }
}
