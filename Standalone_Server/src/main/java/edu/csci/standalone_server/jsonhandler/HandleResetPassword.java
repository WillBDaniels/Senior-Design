package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleResetPassword extends JSONHandler {

    /**
     * This public constructor takes in a json string, as well as a HTTPExchange
     * client object, and then builds the initial jsonMap with the provided
     * items by de-serializing the json that is input.
     *
     * @param json the string representation of the entire JSON information
     * being sent by the client.
     * @param client The HTTPExchange client object being used
     */
    public HandleResetPassword(String json, HttpExchange client) {
        super(json, client);
    }

    /**
     * This public abstract method is for building the JSON response we will
     * send to the client.
     *
     * @return a String that represents the entire JSON object to send to the
     * client.
     */
    @Override
    public String buildResponse() {
        return fillDataPojo();
    }

    /**
     * This method gathers loads of data from our database and aggregates it all
     * into one single POJO object that we can send back to the client user.
     *
     * @param inputPOJO
     */
    private String fillDataPojo() {
        Gson gson = new Gson();
        DataPOJO inputPOJO = jsonData;
        try {
            String query = "UPDATE Employee SET `password` = ? WHERE `employee_id` = ?;";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, inputPOJO.getPasswordHash());
            pstmt.setInt(2, inputPOJO.getEmployeeID());
            pstmt.executeUpdate();
            inputPOJO.setReturnMessage("Update success!");
        } catch (SQLException ex) {
            inputPOJO.setReturnMessage("Update Failure");
            ex.printStackTrace(System.err);
        }
        return gson.toJson(inputPOJO, DataPOJO.class);

    }
}
