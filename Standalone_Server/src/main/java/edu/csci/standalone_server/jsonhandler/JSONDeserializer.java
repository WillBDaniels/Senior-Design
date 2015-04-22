package edu.csci.standalone_server.jsonhandler;

import com.google.gson.Gson;
import edu.csci.standalone_server.Structures.DataPOJO;

/**
 * This class uses the GSON library to deserialize a POJO into an operable JSON
 * object. This allows much easier communication between the server and our apps
 *
 * @author William
 */
public class JSONDeserializer {

    private final String json;

    /**
     * This is simply our constructor which takes in a String which represents
     * the entirety of the json communication.
     *
     * @param json A string that represents valid JSON.
     */
    public JSONDeserializer(String json) {
        this.json = json;
    }

    /**
     * This method uses the Google gson library to deserialize the JSON from the
     * client. It then takes that info and parses it into a map of strings to
     * Objects.
     *
     * @return a Map<String, Object> that represents all of the json information
     * from the client.
     */
    public DataPOJO jsonBuilder() {
        Gson gson = new Gson();
        DataPOJO data;
        data = (DataPOJO) gson.fromJson(json, DataPOJO.class);
        return data;
    }
}
