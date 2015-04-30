package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class HandleGetQA extends JSONHandler {

    /**
     * This public constructor takes in a json string, as well as a HTTPExchange
     * client object, and then builds the initial jsonMap with the provided
     * items by de-serializing the json that is input.
     *
     * @param json the string representation of the entire JSON information
     * being sent by the client.
     * @param client The HTTPExchange client object being used
     */
    public HandleGetQA(String json, HttpExchange client) {
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
        DataPOJO inputPOJO = new DataPOJO();
        try {
            String query;
            ResultSet rs;
            ResultSet rsInner;

            //possible bug if the db doesn't line this up properly, we're expecting this to be de-duped.
            //if it's not.. well, it'll pick the first one. Something to remember at least.
            PreparedStatement pstmt;
            query = "SELECT * FROM `employee` WHERE employee_id = ? ;";

            pstmt = con.prepareStatement(query);
            if (jsonData.getEmployeeID() != 0) {
                pstmt.setInt(1, jsonData.getEmployeeID());
            }else{
                inputPOJO.setReturnMessage("Employee ID required for this operation!");
                return gson.toJson(inputPOJO, DataPOJO.class);
            }
            rs = pstmt.executeQuery();
            while (rs != null && rs.next()){
                inputPOJO.setSecretQuestion(rs.getString("secret_question"));
                inputPOJO.setSecretAnswer(rs.getString("secret_answer"));
            }
            inputPOJO.setReturnMessage("Query Success!");
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
        return gson.toJson(inputPOJO, DataPOJO.class);
    }
}
