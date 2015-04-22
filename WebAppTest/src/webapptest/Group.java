package webapptest;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author William
 */
public class Group {

    private int groupID = 0;
    private IntegerProperty groupIDProp;

    public void setGroupIDProp(Integer value) {
        groupIDPropProperty().set(value);
    }

    public Integer getGroupIDProp() {
        return groupIDPropProperty().get();
    }

    public IntegerProperty groupIDPropProperty() {
        if (groupIDProp == null) {
            groupIDProp = new SimpleIntegerProperty(this, "groupIDProp");
        }
        return groupIDProp;
    }

    private int managerID = 0;
    private IntegerProperty managerIDProp;

    public void setManagerIDProp(Integer value) {
        managerIDPropProperty().set(value);
    }

    public Integer getManagerIDProp() {
        return managerIDPropProperty().get();
    }

    public IntegerProperty managerIDPropProperty() {
        if (managerIDProp == null) {
            managerIDProp = new SimpleIntegerProperty(this, "managerIDProp");
        }
        return managerIDProp;
    }

    private String groupName = "";
    private StringProperty groupNameProp;

    public void setGroupNameProp(String value) {
        groupNamePropProperty().set(value);
    }

    public String getGroupNameProp() {
        return groupNamePropProperty().get();
    }

    public StringProperty groupNamePropProperty() {
        if (groupNameProp == null) {
            groupNameProp = new SimpleStringProperty(this, "groupNameProp");
        }
        return groupNameProp;
    }

    private String groupEmployeeName = "";

    public String getGroupEmployeeName() {
        return groupEmployeeName;
    }

    public void setGroupEmployeeName(String groupEmployeeName) {
        this.groupEmployeeName = groupEmployeeName;
    }
    private StringProperty groupEmployeeNameProp;

    public void setGroupEmployeeNameProp(String value) {
        groupEmployeeNamePropProperty().set(value);
    }

    public String getGroupEmployeeNameProp() {
        return groupEmployeeNamePropProperty().get();
    }

    public StringProperty groupEmployeeNamePropProperty() {
        if (groupEmployeeNameProp == null) {
            groupEmployeeNameProp = new SimpleStringProperty(this, "groupEmployeeNameProp");
        }
        return groupEmployeeNameProp;
    }

    private int groupEmployeeID = 0;
    private IntegerProperty groupEmployeeIDProp;

    public int getGroupEmployeeID() {
        return groupEmployeeID;
    }

    public void setGroupEmployeeID(int groupEmployeeID) {
        this.groupEmployeeID = groupEmployeeID;
    }

    public void setGroupEmployeeIDProp(int value) {
        groupEmployeeIDPropProperty().set(value);
    }

    public int getGroupEmployeeIDProp() {
        return groupEmployeeIDPropProperty().get();
    }

    public IntegerProperty groupEmployeeIDPropProperty() {
        if (groupEmployeeIDProp == null) {
            groupEmployeeIDProp = new SimpleIntegerProperty(this, "groupEmployeeIDProp");
        }
        return groupEmployeeIDProp;
    }

    private List<Employee> empList = new ArrayList<>();

    public List<Employee> getEmpList() {
        return empList;
    }

    public void setEmpList(List<Employee> empList) {
        this.empList = empList;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
