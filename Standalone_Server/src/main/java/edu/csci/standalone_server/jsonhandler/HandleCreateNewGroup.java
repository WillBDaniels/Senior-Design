package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.DataPOJO;
import edu.csci.standalone_server.Employee;
import edu.csci.standalone_server.Group;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleCreateNewGroup extends JSONHandler {

    public HandleCreateNewGroup(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return createNewGroup();
    }

    private String createNewGroup() {
        String query;
        PreparedStatement pstmt;
        DataPOJO data = new DataPOJO();
        Gson gson = new Gson();

        int returnCode;
        for (Group g : jsonData.getGroupList()) {
            for (Employee emp : g.getEmpList()) {
                try {
                    query = "INSERT INTO GROUP ('groupID', 'groupName', 'employeeID') VALUES "
                            + "(?,?, ?);";
                    pstmt = con.prepareStatement(query);
                    pstmt.setInt(0, g.getGroupID());
                    pstmt.setString(1, g.getGroupName());
                    pstmt.setInt(2, emp.getEmployeeID());
                    returnCode = pstmt.executeUpdate();

                } catch (SQLException ex) {
                    data.setReturnMessage("There was an error with one of the insertions, we were on employee: " + emp.getName() + " and group: " + g.getGroupName());
                    ex.printStackTrace(System.err);
                }
            }
        }
        return gson.toJson(data, DataPOJO.class);
    }

}
