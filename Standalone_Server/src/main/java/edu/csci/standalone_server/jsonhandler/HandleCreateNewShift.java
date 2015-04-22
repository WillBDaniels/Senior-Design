package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import edu.csci.standalone_server.Structures.House;
import edu.csci.standalone_server.Structures.Shift;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleCreateNewShift extends JSONHandler {

    public HandleCreateNewShift(String json, HttpExchange client) {
        super(json, client);
    }

    @Override
    public String buildResponse() {
        return createNewShift();
    }

    private String createNewShift() {
        String query;
        PreparedStatement pstmt;
        DataPOJO data = new DataPOJO();
        Gson gson = new Gson();

        int returnCode;
        for (House g : jsonData.getHouseList()) {
            for (Shift shift : g.getShiftList()) {
                try {
                    query = "INSERT INTO shift ( `shift_name`, `house_id`, `shift_time`, `employee_id`) VALUES "
                            + "(?,?,?,?);";
                    pstmt = con.prepareStatement(query);
                    System.out.println("This is the query: " + query);
                    pstmt.setString(1, shift.getName());
                    pstmt.setInt(2, shift.getHouseID());
                    pstmt.setString(3, shift.getTime());
                    pstmt.setInt(4, shift.getEmployeeID());
                    returnCode = pstmt.executeUpdate();

                } catch (SQLException ex) {
                    data.setReturnMessage("There was an error with one of the insertions, we were on house: " + g.getHouseName() + " and shift: " + shift.getName());
                    ex.printStackTrace(System.err);
                }
            }

        }
        return gson.toJson(data, DataPOJO.class);
    }

}
