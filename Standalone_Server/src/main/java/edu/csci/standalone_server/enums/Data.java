package edu.csci.standalone_server.enums;

/**
 *
 * @author William
 */
public enum Data {

    DBPASSWORD("Senior_Design"),
    CONNECTIONSECRET("O*&*&)*$IUHHDSKJLF{P#@))@#$OIJFOIEJSOIJSODIFH*##&#&#&#@P@#_RJFPSIDHSHJSGFO*@HRUHGRDUHFW#$R");

    private final String data;

    Data(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
