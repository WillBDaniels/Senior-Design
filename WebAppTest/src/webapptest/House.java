package webapptest;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author William
 */
public class House {

    private String houseName = "";

    private StringProperty houseNameProp;

    public void setHouseNameProp(String value) {
        houseNamePropProperty().set(value);
    }

    public String getHouseNameProp() {
        return houseNamePropProperty().get();
    }

    public StringProperty houseNamePropProperty() {
        if (houseNameProp == null) {
            houseNameProp = new SimpleStringProperty(this, "houseNameProp");
        }
        return houseNameProp;
    }

    private String houseLocation = "";

    private StringProperty houseLocationProp;

    public void setHouseLocationProp(String value) {
        houseLocationPropProperty().set(value);
    }

    public String getHouseLocationProp() {
        return houseLocationPropProperty().get();
    }

    public StringProperty houseLocationPropProperty() {
        if (houseLocationProp == null) {
            houseLocationProp = new SimpleStringProperty(this, "houseLocationProp");
        }
        return houseLocationProp;
    }

    private int houseID = 0;

    private IntegerProperty houseIDProp;

    public void setHouseIDProp(Integer value) {
        houseIDPropProperty().set(value);
    }

    public Integer getHouseIDProp() {
        return houseIDPropProperty().get();
    }

    public IntegerProperty houseIDPropProperty() {
        if (houseIDProp == null) {
            houseIDProp = new SimpleIntegerProperty(this, "houseIDProp");
        }
        return houseIDProp;
    }

    private boolean isActive = true;

    private BooleanProperty isActiveProp;

    public void setIsActiveProp(Boolean value) {
        isActivePropProperty().set(value);
    }

    public Boolean getIsActiveProp() {
        return isActivePropProperty().get();
    }

    public BooleanProperty isActivePropProperty() {
        if (isActiveProp == null) {
            isActiveProp = new SimpleBooleanProperty(this, "isActiveProp");
        }
        return isActiveProp;
    }

    private List<Shift> shiftList = new ArrayList<>();

    public List<Shift> getShiftList() {
        return shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
