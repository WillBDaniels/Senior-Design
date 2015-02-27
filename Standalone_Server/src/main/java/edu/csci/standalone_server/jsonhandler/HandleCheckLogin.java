package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.DataPOJO;
import edu.csci.standalone_server.Employee;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author William
 */
public class HandleCheckLogin extends JSONHandler {

    /**
     * This public constructor takes in a json string, as well as a HTTPExchange
     * client object, and then builds the initial jsonMap with the provided
     * items by de-serializing the json that is input.
     *
     * @param json the string representation of the entire JSON information
     * being sent by the client.
     * @param client The HTTPExchange client object being used
     */
    public HandleCheckLogin(String json, HttpExchange client) {
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
        return verifyLogin();
    }

    private String verifyLogin() {
        String username = jsonData.getUsername();
        String password = jsonData.getPasswordHash();
        DataPOJO outputData = new DataPOJO();
        Gson gson = new Gson();
        outputData.setUsername(username);
        outputData.setPasswordHash("");
        if (checkLoginDetails(username, password)) {
            outputData.setReturnMessage("Login Verification Success!");
            outputData.setLastActionSuccess(true);
            fillDataPojo(outputData);
        } else {
            outputData.setReturnMessage("Login verification failure!");
            outputData.setLastActionSuccess(false);
        }

        return gson.toJson(outputData, DataPOJO.class);
    }

    private void fillDataPojo(DataPOJO inputPOJO) {
        try {
            String query;
            ResultSet rs;
            ResultSet rsInner;

            //possible bug if the db doesn't line this up properly, we're expecting this to be de-duped.
            //if it's not.. well, it'll pick the first one. Something to remember at least.
            query = "SELECT name, id, isAdmin FROM Employee WHERE username=?;";
            PreparedStatement pstmt;
            pstmt = con.prepareStatement(query);
            pstmt.setString(0, inputPOJO.getUsername());
            rs = pstmt.executeQuery();
            if (rs != null && rs.next()) {
                inputPOJO.setWholeName(rs.getString("name"));
                inputPOJO.setEmployeeID(rs.getInt("id"));
                inputPOJO.setIsAdmin(rs.getBoolean("isAdmin"));
            }
            query = "SELECT * FROM House;";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {
                //get everything we need from houses and such.. yea.
                query = "SELECT * FROM Shift WHERE house_id=?;";
                pstmt = con.prepareStatement(query);
                pstmt.setInt(0, rs.getInt("house_id"));
                rsInner = pstmt.executeQuery();
                while (rsInner != null && rsInner.next()) {
                    //Set all of the shifts for this house. coolio.
                }
            }

            //Building up the list of all the employees.
            query = "SELECT * FROM Employee WHERE isManager=0;";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            List<Employee> employeeList = new ArrayList<>();
            Employee temp;
            while (rs != null && rs.next()) {
                temp = new Employee();
                temp.setEmployeeID(rs.getInt("employee_ID"));
                temp.setIsBackup(rs.getBoolean("is_backup"));
                temp.setName(rs.getString("name"));
                temp.setPhoneNumber(rs.getInt("phone_number"));
                employeeList.add(temp);
            }
            inputPOJO.setAllEmployees(employeeList);

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private boolean checkLoginDetails(String username, String passwordHash) {
        try {
            String query = "SELECT username, password FROM Employee WHERE username=? AND password=?;";

            PreparedStatement pstmt;

            pstmt = con.prepareStatement(query);
            pstmt.setString(0, username);
            pstmt.setString(1, passwordHash);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            return rs != null && rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            return false;
        }

    }
}
