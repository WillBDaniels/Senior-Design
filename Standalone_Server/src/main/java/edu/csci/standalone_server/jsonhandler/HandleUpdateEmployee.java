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
public class HandleUpdateEmployee extends JSONHandler {

    public HandleUpdateEmployee(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return updateEmployee();
    }

    private String updateEmployee() {
        DataPOJO data = jsonData;
        Gson gson = new Gson();
        for (Employee emp : data.getAllEmployees()) {
            try {
                String updateStatement = "UPDATE employee SET  employee_name=?, username=?, phone_number=? WHERE employee_id = ?";
                PreparedStatement pstmt = dbm.getConnection().prepareStatement(updateStatement);
                pstmt.setString(1, emp.getName());
                pstmt.setString(2, emp.getUsername());
                pstmt.setLong(3, emp.getPhoneNumber());
                pstmt.setInt(4, emp.getEmployeeID());
                pstmt.executeUpdate();

                // data.getAllEmployees().remove(emp);
            } catch (SQLException ex) {
                data.setReturnMessage("There was an error updating the employee, sorry. ");
                ex.printStackTrace(System.err);
                return gson.toJson(data, DataPOJO.class);
            }
        }
        data.setReturnMessage("Update successfull!");
        return gson.toJson(data, DataPOJO.class);

    }

}
