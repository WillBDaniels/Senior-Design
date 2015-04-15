package edu.csci.standalone_server.enums;

import edu.csci.standalone_server.jsonhandler.HandleCheckLogin;
import edu.csci.standalone_server.jsonhandler.HandleCreateNewEmployee;
import edu.csci.standalone_server.jsonhandler.HandleCreateNewGroup;
import edu.csci.standalone_server.jsonhandler.HandleCreateNewHouse;
import edu.csci.standalone_server.jsonhandler.HandleCreateNewShift;
import edu.csci.standalone_server.jsonhandler.HandleDeleteEmployee;
import edu.csci.standalone_server.jsonhandler.HandleDeleteGroup;
import edu.csci.standalone_server.jsonhandler.HandleDeleteHouse;
import edu.csci.standalone_server.jsonhandler.HandleDeleteShift;
import edu.csci.standalone_server.jsonhandler.HandleGetAllData;
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
    GETMOBILEINFO(HandleCheckLogin.class, "checkLogin"),
    DELETESHIFT(HandleDeleteShift.class, "deleteShift"),
    DELETEHOUSE(HandleDeleteHouse.class, "deleteHouse"),
    DELETEGROUP(HandleDeleteGroup.class, "deleteGroup"),
    DELETEEMPLOYEE(HandleDeleteEmployee.class, "deleteEmployee"),
    ADDEMPLOYEE(HandleCreateNewEmployee.class, "addEmployee"),
    ADDHOUSE(HandleCreateNewHouse.class, "addHouse"),
    ADDSHIFT(HandleCreateNewShift.class, "addShift"),
    ADDGROUP(HandleCreateNewGroup.class, "addGroup"),
    GETALLDATA(HandleGetAllData.class, "getData");
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
