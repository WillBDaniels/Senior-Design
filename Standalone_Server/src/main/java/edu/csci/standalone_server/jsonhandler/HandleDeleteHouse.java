package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import edu.csci.standalone_server.Structures.House;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleDeleteHouse extends JSONHandler {

    public HandleDeleteHouse(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return deleteHouse();
    }

    private String deleteHouse() {
        DataPOJO data = jsonData;
        Gson gson = new Gson();
        for (House house : data.getHouseList()) {
            try {
                String deleteStatement = "DELETE FROM house WHERE house_id=?";
                PreparedStatement pstmt = dbm.getConnection().prepareStatement(deleteStatement);
                pstmt.setInt(1, house.getHouseID());
                pstmt.executeUpdate();
                //data.getHouseList().remove(house);
            } catch (SQLException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return gson.toJson(data, DataPOJO.class);

    }

}
