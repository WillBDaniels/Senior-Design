package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import edu.csci.standalone_server.Structures.Group;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleDeleteGroup extends JSONHandler {

    public HandleDeleteGroup(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return deleteEmployee();
    }

    private String deleteEmployee() {
        DataPOJO data = jsonData;
        Gson gson = new Gson();
        for (Group group : data.getGroupList()) {
            try {
                String deleteStatement = "DELETE FROM groups WHERE group_id=?";
                PreparedStatement pstmt = dbm.getConnection().prepareStatement(deleteStatement);
                pstmt.setInt(1, group.getGroupID());
                pstmt.executeUpdate();
                //data.getGroupList().remove(group);
            } catch (SQLException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return gson.toJson(data, DataPOJO.class);

    }

}
