package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.DataPOJO;
import edu.csci.standalone_server.Employee;
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
                String deleteStatement = "DELETE FROM Employee WHERE employee_id=?";
                PreparedStatement pstmt = dbm.getConnection().prepareStatement(deleteStatement);
                pstmt.setInt(0, emp.getEmployeeID());
                pstmt.executeUpdate();
                data.getAllEmployees().remove(emp);
            } catch (SQLException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return gson.toJson(data, DataPOJO.class);

    }

}
