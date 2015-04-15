package webapptest;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum is to provide a listing that is a common interface for accessing
 * various methods of the Com enum. This essentially allows us to call really
 * generic methods without knowing what sort of software is going to be using
 * this.
 *
 * @author William
 */
public enum Type {

    SHIFTY(Com.GETMOBILEINFO.getProtocol(), Com.DELETESHIFT.getProtocol(), Com.DELETEHOUSE.getProtocol(),
            Com.DELETEGROUP.getProtocol(), Com.DELETEEMPLOYEE.getProtocol(), Com.ADDEMPLOYEE.getProtocol(),
            Com.ADDHOUSE.getProtocol(), Com.ADDSHIFT.getProtocol(), Com.ADDGROUP.getProtocol(), Com.GETALLDATA.getProtocol());
    private final Map<Action, String> actionMap = new HashMap<>();

    Type(String getMobileInfo, String deleteShift, String deleteHouse, String deleteGroup,
            String deleteEmployee, String addEmployee, String addHouse, String addShift, String addGroup, String getAllData) {
        actionMap.put(Action.GETMOBILEINFO, getMobileInfo);
        actionMap.put(Action.DELETESHIFT, deleteShift);
        actionMap.put(Action.DELETEHOUSE, deleteHouse);
        actionMap.put(Action.DELETEGROUP, deleteGroup);
        actionMap.put(Action.DELETEEMPLOYEE, deleteEmployee);
        actionMap.put(Action.ADDEMPLOYEE, addEmployee);
        actionMap.put(Action.ADDHOUSE, addHouse);
        actionMap.put(Action.ADDSHIFT, addShift);
        actionMap.put(Action.ADDGROUP, addGroup);
        actionMap.put(Action.GETALLDATA, getAllData);

    }

    public Map<Action, String> getActions() {
        return actionMap;
    }
}
