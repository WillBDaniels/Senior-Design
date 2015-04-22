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
public class HandleDeleteEmployee extends JSONHandler {

    public HandleDeleteEmployee(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return deleteEmployee();
    }

    private String deleteEmployee() {
        DataPOJO data = jsonData;
        Gson gson = new Gson();
        for (Employee emp : data.getAllEmployees()) {
            try {
                String deleteStatement = "DELETE FROM employee WHERE employee_id=?";
                PreparedStatement pstmt = dbm.getConnection().prepareStatement(deleteStatement);
                pstmt.setInt(1, emp.getEmployeeID());
                pstmt.executeUpdate();
                // data.getAllEmployees().remove(emp);
            } catch (SQLException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return gson.toJson(data, DataPOJO.class);

    }

}
