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
public class HandleCreateNewHouse extends JSONHandler {

    public HandleCreateNewHouse(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return createNewHouse();
    }

    private String createNewHouse() {
        String query;
        PreparedStatement pstmt;
        DataPOJO data = new DataPOJO();
        Gson gson = new Gson();

        int returnCode;
        for (House g : jsonData.getHouseList()) {
            try {
                query = "INSERT INTO house (`house_id`, `house_location`, `house_name`, `is_active`) VALUES "
                        + "(?,?,?,?);";
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1, g.getHouseID());
                pstmt.setString(2, g.getHouseLocation());
                pstmt.setString(3, g.getHouseLocation());
                pstmt.setBoolean(4, g.isIsActive());
                returnCode = pstmt.executeUpdate();

            } catch (SQLException ex) {
                data.setReturnMessage("There was an error with one of the insertions, we were on house: " + g.getHouseName());
                ex.printStackTrace(System.err);
            }
        }
        return gson.toJson(data, DataPOJO.class);
    }

}
