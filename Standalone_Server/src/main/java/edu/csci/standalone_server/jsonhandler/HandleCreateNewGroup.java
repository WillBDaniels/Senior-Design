package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import edu.csci.standalone_server.Structures.Employee;
import edu.csci.standalone_server.Structures.Group;
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
                    query = "INSERT INTO `groups` (`group_id`, `group_name`, `employee_id`, `manager_id`) VALUES "
                            + "(?,?, ?, ?);";
                    pstmt = con.prepareStatement(query);
                    pstmt.setInt(1, g.getGroupID());
                    pstmt.setString(2, g.getGroupName());
                    pstmt.setInt(3, emp.getEmployeeID());
                    pstmt.setInt(4, g.getManagerID());
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
