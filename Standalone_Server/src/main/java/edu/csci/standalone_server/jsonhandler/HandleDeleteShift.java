package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.DataPOJO;
import edu.csci.standalone_server.House;
import edu.csci.standalone_server.Shift;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleDeleteShift extends JSONHandler {

    public HandleDeleteShift(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return deleteShift();
    }

    private String deleteShift() {
        DataPOJO data = jsonData;
        Gson gson = new Gson();
        for (House house : data.getHouseList()) {
            for (Shift shift : house.getShiftList()) {
                try {
                    String deleteStatement = "DELETE FROM Shift WHERE shift_id=?";
                    PreparedStatement pstmt = dbm.getConnection().prepareStatement(deleteStatement);
                    pstmt.setInt(0, shift.getShiftID());
                    pstmt.executeUpdate();
                    house.getShiftList().remove(shift);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }

        return gson.toJson(data, DataPOJO.class);

    }

}
