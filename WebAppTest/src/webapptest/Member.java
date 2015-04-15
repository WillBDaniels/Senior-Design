package webapptest;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author fritz
 */
public class Member {

    public StringProperty groupID;
    public StringProperty groupManagerID;
    public StringProperty groupName;
    public StringProperty empID;
    public StringProperty ID;

    //Setters
    void setGroupID(String value) {
        groupIDProperty().set(value);
    }

    void setID(String value) {
        idProperty().set(value);
    }

    void setManager(String value) {
        groupManagerIDProperty().set(value);
    }

    void setName(String value) {
        groupNameProperty().set(value);
    }

    void addEmp(String value) {
        empIDProperty().set(value);
    }

    public StringProperty empIDProperty() {
        if (empID == null) {
            empID = new SimpleStringProperty(this, "empID");
        }
        return empID;
    }

    //String property setters
    public StringProperty groupNameProperty() {
        if (groupName == null) {
            groupName = new SimpleStringProperty(this, "groupName");
        }
        return groupName;
    }

    public StringProperty idProperty() {
        if (ID == null) {
            ID = new SimpleStringProperty(this, "ID");
        }
        return ID;
    }

    public StringProperty groupManagerIDProperty() {
        if (groupManagerID == null) {
            groupManagerID = new SimpleStringProperty(this, "groupManagerID");
        }
        return groupManagerID;
    }

    public StringProperty groupIDProperty() {
        if (groupID == null) {
            groupID = new SimpleStringProperty(this, "groupIDProperty");
        }
        return groupID;
    }

    //Getters
    String getGroupID() {
        return groupIDProperty().get();
    }

    String getID() {
        return idProperty().get();
    }

    String getManager() {
        return groupManagerIDProperty().get();
    }

    String getName() {
        return groupNameProperty().get();
    }

    String getEmps() {
        return empIDProperty().get();
    }
}
