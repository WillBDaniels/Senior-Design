package edu.csci.standalone_server.enums;

/**
 *
 * @author William
 */
public enum Data {

    DBPASSWORD("Senior_Design"),
    CONNECTIONSECRET("555hskfusd2ksfkjdf118431@8459438754hdkdsf-98134ihfadfafdf");

    private final String data;

    Data(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
