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
public class Shift {

    private String name = "";

    private StringProperty nameProp;

    public void setNameProp(String value) {
        namePropProperty().set(value);
    }

    public String getNameProp() {
        if (namePropProperty().get().isEmpty()) {
            setNameProp(getName());
        }
        return namePropProperty().get();
    }

    public StringProperty namePropProperty() {
        if (nameProp == null) {
            nameProp = new SimpleStringProperty(this, "nameProp");
        }
        return nameProp;
    }

    private String time = "";

    private StringProperty timeProp;

    public void setTimeProp(String value) {
        timePropProperty().set(value);
    }

    public String getTimeProp() {
        if (timePropProperty().get().isEmpty()) {
            setTimeProp(getTime());
        }
        return timePropProperty().get();
    }

    public StringProperty timePropProperty() {
        if (timeProp == null) {
            timeProp = new SimpleStringProperty(this, "timeProp");
        }
        return timeProp;
    }

    private int houseID = 0;

    private IntegerProperty houseIDProp;

    public void setHouseIDProp(Integer value) {
        houseIDPropProperty().set(value);
    }

    public Integer getHouseIDProp() {
        if (houseIDPropProperty().get() == 0) {
            setHouseIDProp(getHouseID());
        }
        return houseIDPropProperty().get();
    }

    public IntegerProperty houseIDPropProperty() {
        if (houseIDProp == null) {
            houseIDProp = new SimpleIntegerProperty(this, "houseIDProp");
        }
        return houseIDProp;
    }

    private int shiftID = 0;

    private IntegerProperty shiftIDProp;

    public void setShiftIDProp(Integer value) {
        shiftIDPropProperty().set(value);
    }

    public Integer getShiftIDProp() {
        if (shiftIDPropProperty().get() == 0) {
            setShiftIDProp(getShiftID());
        }
        return shiftIDPropProperty().get();
    }

    public IntegerProperty shiftIDPropProperty() {
        if (shiftIDProp == null) {
            shiftIDProp = new SimpleIntegerProperty(this, "shiftIDProp");
        }
        return shiftIDProp;
    }

    private int employeeID = 0;

    private IntegerProperty employeeIDProp;

    public void setEmployeeIDProp(Integer value) {
        employeeIDPropProperty().set(value);
    }

    public Integer getEmployeeIDProp() {
        if (employeeIDPropProperty().get() == 0) {
            setEmployeeIDProp(getEmployeeID());
        }
        return employeeIDPropProperty().get();
    }

    public IntegerProperty employeeIDPropProperty() {
        if (employeeIDProp == null) {
            employeeIDProp = new SimpleIntegerProperty(this, "employeeIDProp");
        }
        return employeeIDProp;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }
    private List<Employee> asigneeList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Employee> getAsigneeList() {
        return asigneeList;
    }

    public void setAsigneeList(List<Employee> asigneeList) {
        this.asigneeList = asigneeList;
    }

}
