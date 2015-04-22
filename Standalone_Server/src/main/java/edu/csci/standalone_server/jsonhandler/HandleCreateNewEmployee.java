package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import edu.csci.standalone_server.Structures.Employee;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleCreateNewEmployee extends JSONHandler {

    public HandleCreateNewEmployee(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return createNewEmployee();
    }

    private String createNewEmployee() {
        String query;
        PreparedStatement pstmt;
        DataPOJO data = new DataPOJO();
        Gson gson = new Gson();

        int returnCode;
        for (Employee emp : jsonData.getAllEmployees()) {
            try {

                query = "INSERT INTO employee ( `employee_name`, `phone_number`, `is_manager`, `is_backup`, `username`,`password`, `is_admin`) VALUES "
                        + "(?,?,?,?, ? , ? ,?);";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, emp.getName());
                pstmt.setLong(2, emp.getPhoneNumber());
                pstmt.setBoolean(3, emp.isIsManager());
                pstmt.setBoolean(4, emp.isIsBackup());
                pstmt.setString(5, emp.getUsername());
                pstmt.setString(6, emp.getPassword());
                pstmt.setBoolean(7, emp.isIsAdmin());
                returnCode = pstmt.executeUpdate();

            } catch (SQLException ex) {
                data.setReturnMessage("There was an error with one of the insertions, we were on employee: " + emp.getName());
                ex.printStackTrace(System.err);
            }
        }
        return gson.toJson(data, DataPOJO.class);
    }

}
