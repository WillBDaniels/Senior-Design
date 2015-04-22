package edu.csci.standalone_server.jsonhandler;

import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.Structures.DataPOJO;
import edu.csci.standalone_server.DatabaseManager;
import java.sql.Connection;

/**
 * This class is the abstract class that all of our json handlers will extend
 * from. This allows us to have a common interface which still has some solid
 * default functionality built into it, as well as forcing everyone to provide a
 * real implementation of the 'buildResponse' method.
 *
 *
 * @author William
 */
public abstract class JSONHandler {

    /**
     * This is the protected jsonMap that is used throughout to access data from
     * the client that was in JSON form.
     */
    protected final DataPOJO jsonData;
    /**
     * This is our connection that will be instantiated by our DatabaseManager
     */
    protected Connection con;
    /**
     * This is the Database manager that is to be used throughout the various
     * JSONHandlers. This centralizes the database manager to make sure that we
     * don't have any sort of strange differing configurations.
     */
    protected final DatabaseManager dbm = new DatabaseManager();

    /**
     * This public constructor takes in a json string, as well as a HTTPExchange
     * client object, and then builds the initial jsonMap with the provided
     * items by de-serializing the json that is input.
     *
     * @param json the string representation of the entire JSON information
     * being sent by the client.
     * @param client The HTTPExchange client object being used
     */
    public JSONHandler(String json, HttpExchange client) {
        JSONDeserializer jsd = new JSONDeserializer(json);
        this.jsonData = jsd.jsonBuilder();
    }

    /**
     * This public abstract method is for building the JSON response we will
     * send to the client.
     *
     * @return a String that represents the entire JSON object to send to the
     * client.
     */
    public abstract String buildResponse();

    /**
     * This public string makes it so that anything that extends this class will
     * have the common workflow as far as how to obtain the string JSon
     * response.
     *
     * @return
     */
    public String getJSONResponse() {
        String response;
        con = dbm.getConnection();
        response = buildResponse();
        dbm.closeConnection();
        return response;
    }
}
