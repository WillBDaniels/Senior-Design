package edu.csci.standalone_server.enums;

import edu.csci.standalone_server.jsonhandler.HandleCheckLogin;
import edu.csci.standalone_server.jsonhandler.JSONHandler;

/**
 *
 * @author William
 */
public enum State {

    /**
     * This enum builds an enumeration of 'states' that the server iterates
     * through, which then point the program to the appropriate handling class.
     * All classes extend the JSONHandler abstract class.
     *
     * @author William
     */
    /**
     * This all of the 'state' classes available currently
     *
     * @{
     */
    GETMOBILEINFO(HandleCheckLogin.class, "handleAndroidLogin");
    /**
     * @}
     */

    private final Class<? extends JSONHandler> handler;

    private final String checkString;

    /**
     * This is our constructor.
     *
     * @param handler
     * @param checkString
     */
    State(Class<? extends JSONHandler> handler, String checkString) {
        this.handler = handler;
        this.checkString = checkString;
    }

    /**
     *
     * @return
     */
    public Class<? extends JSONHandler> getHandler() {
        return handler;
    }

    /**
     *
     * @return
     */
    public String getCheckString() {
        return checkString;
    }

}
