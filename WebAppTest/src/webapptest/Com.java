package webapptest;

/**
 * This enum defines all of our communication protocols between the server and
 * the various magVAR products. This should be shared between all of our
 * products to ensure that we are using the same protocol. AKA: if anything here
 * changes, it needs to also be mirrored across products.
 *
 * @author William
 */
public enum Com {

    /**
     * These are all of the communication protocols for the Com enum. They
     * represent the common communication between all of our products (once
     * decrypted)
     *
     * @{
     */
    FIRSTNAME("firstName"),
    LASTNAME("lastName"),
    EMAILEXT("emailExtension"),
    EMAILFULL("emailAddress"),
    PHONENUMBER("phoneNumber"),
    HARDWAREID("hardwareID"),
    USERID("userID"),
    CUBEID("cubeID"),
    SECRETKEY("secretkey"),
    JSONDATA("data"),
    RETURNMESSAGE("returnMessage"),
    CHOOSEACTION("action"),
    GETMOBILEINFO("checkLogin"),
    DELETESHIFT("deleteShift"),
    DELETEHOUSE("deleteHouse"),
    DELETEGROUP("deleteGroup"),
    DELETEEMPLOYEE("deleteEmployee"),
    ADDEMPLOYEE("addEmployee"),
    ADDHOUSE("addHouse"),
    ADDSHIFT("addShift"),
    ADDGROUP("addGroup"),
    UPDATEEMPLOYEE("updateEmployee"),
    GETALLDATA("getData"),
    CONNECTIONSECRET("%%%%##555_hskfusd&2ksfkjdf;;;___---118431@8459438754hdkdsf-98134ihfadfafdf");
    /**
     * @}
     */

    private final String communicationString;

    /**
     * This is our public constructor for the enum, takes in a string value
     * which represents the common communication protocol across products so
     * that we can communicate in an easy fashion.
     *
     * @param communicationString the communication string protocol defined in
     * the enum
     */
    Com(String communicationString) {
        this.communicationString = communicationString;
    }

    /**
     *
     * @return returns the communication protocol for the given enum member.
     */
    public String getProtocol() {
        return communicationString;
    }

    /**
     *
     * @return a nicely formatted version of the communication protocol.
     */
    @Override
    public String toString() {
        return "Communication protocol for " + this.name() + " is: " + communicationString;
    }
}
