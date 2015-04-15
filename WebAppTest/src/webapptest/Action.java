package webapptest;

/**
 * This enum simply lists all of the currently available server actions. This
 * list could be expanded in the future, but it should be ensured that the
 * number of actions in Type.java have one field for each possible action,
 * defined here.
 *
 * @author William
 */
public enum Action {

    GETMOBILEINFO(), DELETESHIFT(), DELETEHOUSE(), DELETEGROUP(), DELETEEMPLOYEE(),
    ADDEMPLOYEE(), ADDHOUSE(), ADDSHIFT(), ADDGROUP(), GETALLDATA();
}
