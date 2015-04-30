package webapptest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is our 'employee' object we are using.
 *
 * @author William
 */
public class Employee {


        private String secretQuestion = "";
    private String secretAnswer = "";

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    



    private String name = "";
    private StringProperty nameProp;

    public void setNameProp(String value) {
        if (value == null) {
            nameProp = null;
            return;
        }
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

    private String username = "";

    private StringProperty usernameProp;

    public void setUsernameProp(String value) {
        if (value == null) {
            usernameProp = null;
            return;
        }
        usernamePropProperty().set(value);
    }

    public String getUsernameProp() {
        if (usernamePropProperty().get().isEmpty()) {
            setUsernameProp(getUsername());
        }
        return usernamePropProperty().get();
    }

    public StringProperty usernamePropProperty() {
        if (usernameProp == null) {
            usernameProp = new SimpleStringProperty(this, "usernameProp");
        }
        return usernameProp;
    }

    private String password = "";
    private StringProperty passwordProp;

    public void setPasswordProp(String value) {
        if (value == null) {
            passwordProp = null;
            return;
        }
        passwordPropProperty().set(value);
    }

    public String getPasswordProp() {
        return passwordPropProperty().get();
    }

    public StringProperty passwordPropProperty() {
        if (passwordProp == null) {
            passwordProp = new SimpleStringProperty(this, "passwordProp");
        }
        return passwordProp;
    }
    private int employeeID = 0;
    private IntegerProperty employeeIDProp;

    public void setEmployeeIDProp(Integer value) {
        if (value == null) {
            employeeIDProp = null;
            return;
        }
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

    private String phoneNumber = "";
    private StringProperty phoneNumberProp;

    public void setPhoneNumberProp(String value) {
        if (value == null) {
            phoneNumberProp = null;
            return;
        }
        phoneNumberPropProperty().set(value);
    }

    public String getPhoneNumberProp() {
        if (phoneNumberPropProperty().get().isEmpty()) {
            setPhoneNumberProp((getPhoneNumber()));
        }
        return phoneNumberPropProperty().get();
    }

    public StringProperty phoneNumberPropProperty() {
        if (phoneNumberProp == null) {
            phoneNumberProp = new SimpleStringProperty(this, "phoneNumberProp");
        }
        return phoneNumberProp;
    }
    private boolean isBackup = false;
    private BooleanProperty isBackupProp;

    public void setIsBackupProp(Boolean value) {
        if (value == null) {
            isBackupProp = null;
            return;
        }
        isBackupPropProperty().set(value);
    }

    public Boolean getIsBackupProp() {
        return isBackupPropProperty().get();
    }

    public BooleanProperty isBackupPropProperty() {
        if (isBackupProp == null) {
            isBackupProp = new SimpleBooleanProperty(this, "isBackupProp");
        }
        return isBackupProp;
    }

    private boolean isManager = false;
    private BooleanProperty isManagerProp;

    public void setIsManagerProp(Boolean value) {
        if (value == null) {
            isManagerProp = null;
            return;
        }
        isManagerPropProperty().set(value);
    }

    public Boolean getIsManagerProp() {
        return isManagerPropProperty().get();
    }

    public BooleanProperty isManagerPropProperty() {
        if (isManagerProp == null) {
            isManagerProp = new SimpleBooleanProperty(this, "isManagerProp");
        }
        return isManagerProp;
    }
    private boolean isAdmin = false;
    private BooleanProperty isAdminProp;

    public void setIsAdminProp(Boolean value) {
        if (value == null) {
            isAdminProp = null;
            return;
        }
        isAdminPropProperty().set(value);
    }

    public Boolean getIsAdminProp() {
        return isAdminPropProperty().get();
    }

    public BooleanProperty isAdminPropProperty() {
        if (isAdminProp == null) {
            isAdminProp = new SimpleBooleanProperty(this, "isAdminProp");
        }
        return isAdminProp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isIsBackup() {
        return isBackup;
    }

    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }

}
