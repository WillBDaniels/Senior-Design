package webapptest;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author fritz
 */
public class FXMLDocumentController implements Initializable {

    //Fields for the employee addition
    @FXML
    private CheckBox managerCheck;
    @FXML
    private TextField empName;
    @FXML
    private Label lbl_employee_error;

    @FXML
    private CheckBox cb_house_is_active;
    @FXML
    private TextField empUserName;
    @FXML
    private TextField empPhone;
    @FXML
    private TextField empPassword;
    @FXML
    private CheckBox cb_employee_admin;
    @FXML
    private CheckBox cb_employee_backup;

    //Fields for the facility addition
    @FXML
    private TextField tf_house_name;
    @FXML
    private TextField tf_house_neighborhood;

    @FXML
    private Label lbl_house_error;
    //Fields for the shift addition
    @FXML
    private Label lbl_shift_error;
    @FXML
    private TextField shiftTime;
    @FXML
    private TextField shiftEmpID;
    @FXML
    private TextField shiftHouseID;
    @FXML
    private TextField tf_shift_name;

    //Fields for the group addition
    @FXML
    private Label lbl_group_error;
    @FXML
    private TextField groupName;
    @FXML
    private TextField groupManagerID;
    @FXML
    private TextField groupID;
    @FXML
    private TextField groupEmpIDs;

    //Fields for the employee delete
    @FXML
    private TextField empDeleteID;
    @FXML
    private TextField empDeleteName;

    //Fields for the shift delete
    @FXML
    private TextField shiftDeleteID;
    @FXML
    private TextField shiftDeleteEmpID;

    //Fields for the facility delete
    @FXML
    private TextField facDeleteID;
    @FXML
    private TextField facDeleteNeighborhood;

    //Fields for the group delete
    @FXML
    private TextField groupDeleteID;
    @FXML
    private TextField groupDeleteName;

    //Areas for the deletion information to be displayed
    @FXML
    private VBox empDeleteDisplay;
    @FXML
    private VBox shiftDeleteDisplay;
    @FXML
    private VBox facDeleteDisplay;
    @FXML
    private VBox groupDeleteDisplay;

    @FXML
    private TableView tv_group, tv_employee, tv_house, tv_shift;

    @FXML
    private Button btn_update_employees;

    /**
     * Table columns for employee
     */
    private TableColumn nameCol, idCol, phoneNumberCol, userNameCol, managerCol, adminCol, backupCol;

    /**
     * Table columns for shifts
     */
    private TableColumn shiftIDCol, timeCol, houseIDCol, empIDCol, shiftNameCol;

    /**
     * Table columns for facilities
     */
    private TableColumn facilityIDCol, neighborhoodCol, houseNameCol, houseIsActiveCol;

    /**
     * Table columns for groups
     */
    private TableColumn groupIDCol, groupNameCol, groupManagerIDCol, groupEmployeeNameCol, groupEmployeeIDCol;

    //Booleans to check if it is ok to delete
    private Boolean employeeDelete;
    private Boolean shiftDelete;
    private Boolean facilityDelete;
    private Boolean groupDelete;
    private final Gson gson = new Gson();

    @FXML
    public void groupCreateButtonPressed() {
        if (groupName.getText().isEmpty()) {
            lbl_group_error.setText("Group name required!");
            WebAppTest.setCSS(true, groupName);
            return;
        } else {
            WebAppTest.setCSS(false, groupName);
        }
        if (groupManagerID.getText().isEmpty()) {
            lbl_group_error.setText("Manager ID required!");
            WebAppTest.setCSS(true, groupManagerID);
            return;
        } else {
            WebAppTest.setCSS(false, groupManagerID);
        }
        if (groupID.getText().isEmpty()) {
            lbl_group_error.setText("Group ID required!");
            WebAppTest.setCSS(true, groupID);
            return;
        } else {
            WebAppTest.setCSS(false, groupID);
        }
        if (groupEmpIDs.getText().isEmpty()) {
            lbl_group_error.setText("Employee ID's required!");
            WebAppTest.setCSS(true, groupEmpIDs);
            return;
        } else {
            WebAppTest.setCSS(false, groupEmpIDs);
        }
        lbl_group_error.setText("");
        DataPOJO groupCreate = new DataPOJO();
        List<Group> groupListInner = new ArrayList<>();
        Group group = new Group();
        group.setManagerID(Integer.valueOf(groupManagerID.getText()));
        group.setGroupName(groupName.getText());
        group.setGroupID(Integer.valueOf(groupID.getText()));
        //add employee ids here
        ArrayList<String> eList = new ArrayList<>(Arrays.asList(groupEmpIDs.getText().split("\\,")));
        ArrayList<Employee> realEList = new ArrayList<>();
        for (String e : eList) {
            Employee emp = new Employee();
            emp.setEmployeeID(Integer.valueOf(e));
            realEList.add(emp);
        }
        group.setEmpList(realEList);
        groupListInner.add(group);
        groupCreate.setGroupList(groupListInner);
        String response = WebAppTest.postToServer(Type.SHIFTY, Action.ADDGROUP, gson.toJson(groupCreate, DataPOJO.class));
        System.out.println("This was the response to creating a new group: " + response);
    }

    private void initializeEmployeeTableColumns() {
        nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameProp"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, String> t) {
                btn_update_employees.setDisable(false);
                ((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNameProp(t.getNewValue());

            }
        });

        phoneNumberCol = new TableColumn<>("Phone #");
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumberProp"));
        phoneNumberCol.setCellFactory(numberCallback());
        phoneNumberCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, Integer> t) {
                btn_update_employees.setDisable(false);
                ((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhoneNumber(t.getNewValue());

            }
        });

        userNameCol = new TableColumn<>("Username");
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("usernameProp"));
        userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, String> t) {
                btn_update_employees.setDisable(false);
                ((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUsername(t.getNewValue());

            }
        });
//
        idCol = new TableColumn<>("ID");
        idCol.setEditable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("employeeIDProp"));
        idCol.setCellFactory(numberCallback());
        idCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, Integer> t) {
                btn_update_employees.setDisable(false);
                ((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmployeeIDProp(Integer.valueOf(t.getNewValue()));

            }
        });
        managerCol = new TableColumn<>("Is Manager");
        managerCol.setCellValueFactory(new PropertyValueFactory<>("isManagerProp"));
        managerCol.setCellFactory(CheckBoxTableCell.forTableColumn(managerCol));
        managerCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, Boolean> t) {
                btn_update_employees.setDisable(false);
                ((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsManager(t.getNewValue());

            }
        });

        adminCol = new TableColumn<>("Is Admin");
        adminCol.setCellValueFactory(new PropertyValueFactory<>("isAdminProp"));
        adminCol.setCellFactory(CheckBoxTableCell.forTableColumn(adminCol));
        adminCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, Boolean> t) {
                btn_update_employees.setDisable(false);
                ((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsAdmin(t.getNewValue());

            }
        });

        backupCol = new TableColumn<>("Is Backup");
        backupCol.setEditable(true);
        backupCol.setCellValueFactory(new PropertyValueFactory<>("isBackupProp"));
        backupCol.setCellFactory(CheckBoxTableCell.forTableColumn(backupCol));
        backupCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, Boolean> t) {
                btn_update_employees.setDisable(false);
                ((Employee) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsBackup(t.getNewValue());
            }
        });
        tv_employee.setEditable(true);
        tv_employee.getColumns().addAll(idCol, nameCol, phoneNumberCol, userNameCol, managerCol, backupCol, adminCol);
    }

    private Callback<TableColumn, TableCell> numberCallback() {
        return new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                TableCell cell = new TableCell<Employee, Integer>() {
                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : getString());
                    }

                    private String getString() {
                        String ret;
                        if (getItem() != null) {
                            ret = getItem().toString();
                        } else {
                            ret = "0";
                        }
                        return ret;
                    }
                };
                return cell;
            }
        };
    }

    private void initializeShiftTableColumns() {
        shiftIDCol = new TableColumn<>("Shift ID");
        shiftIDCol.setCellValueFactory(new PropertyValueFactory<>("shiftIDProp"));
        shiftIDCol.setCellFactory(numberCallback());
        shiftIDCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Shift, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Shift, Integer> t) {
                ((Shift) t.getTableView().getItems().get(t.getTablePosition().getRow())).setShiftID(t.getNewValue());

            }
        });
        timeCol = new TableColumn<>("Shift Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeProp"));
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Shift, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Shift, String> t) {
                ((Shift) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTime(t.getNewValue());

            }
        });
        empIDCol = new TableColumn<>("Main Employee ID");
        empIDCol.setCellValueFactory(new PropertyValueFactory<>("employeeIDProp"));
        empIDCol.setCellFactory(numberCallback());
        empIDCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Shift, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Shift, Integer> t) {
                ((Shift) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmployeeID(t.getNewValue());

            }
        });
        houseIDCol = new TableColumn<>("House ID");
        houseIDCol.setCellValueFactory(new PropertyValueFactory<>("houseIDProp"));
        houseIDCol.setCellFactory(numberCallback());
        houseIDCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Shift, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Shift, Integer> t) {
                ((Shift) t.getTableView().getItems().get(t.getTablePosition().getRow())).setHouseID(t.getNewValue());

            }
        });
        shiftNameCol = new TableColumn<>("Shift Name");
        shiftNameCol.setCellValueFactory(new PropertyValueFactory<>("nameProp"));
        shiftNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        shiftNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Shift, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Shift, String> t) {
                ((Shift) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNameProp(t.getNewValue());

            }
        });
        tv_shift.getColumns().addAll(timeCol, houseIDCol, empIDCol, shiftIDCol, shiftNameCol);
    }

    private void initializeGroupColumns() {
        groupIDCol = new TableColumn<>("Group ID");
        groupIDCol.setCellValueFactory(new PropertyValueFactory<>("groupIDProp"));
        groupIDCol.setCellFactory(numberCallback());
        groupIDCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Group, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Group, Integer> t) {
                ((Group) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGroupID(t.getNewValue());

            }
        });
        groupNameCol = new TableColumn<>("Group Name");
        groupNameCol.setCellValueFactory(new PropertyValueFactory<>("groupNameProp"));
        groupNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        groupNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Group, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Group, String> t) {
                ((Group) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGroupName(t.getNewValue());

            }
        });
        groupManagerIDCol = new TableColumn<>("Manager ID");
        groupManagerIDCol.setCellValueFactory(new PropertyValueFactory<>("managerIDProp"));
        groupManagerIDCol.setCellFactory(numberCallback());
        groupManagerIDCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Group, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Group, Integer> t) {
                ((Group) t.getTableView().getItems().get(t.getTablePosition().getRow())).setManagerID(t.getNewValue());

            }
        });
        groupEmployeeNameCol = new TableColumn<>("Employee Name");
        groupEmployeeNameCol.setCellValueFactory(new PropertyValueFactory<>("groupEmployeeNameProp"));
        groupEmployeeNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        groupEmployeeNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Group, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Group, String> t) {
                ((Group) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGroupEmployeeName(t.getNewValue());

            }
        });

        groupEmployeeIDCol = new TableColumn<>("Employee ID");
        groupEmployeeIDCol.setCellValueFactory(new PropertyValueFactory<>("groupEmployeeIDProp"));
        groupEmployeeIDCol.setCellFactory(numberCallback());
        groupEmployeeIDCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Group, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Group, Integer> t) {
                ((Group) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGroupEmployeeID(t.getNewValue());

            }
        });
        tv_group.getColumns().addAll(groupIDCol, groupNameCol, groupManagerIDCol, groupEmployeeNameCol, groupEmployeeIDCol);
    }

    private void initializeHouseColumns() {
        facilityIDCol = new TableColumn<>("House ID");
        facilityIDCol.setEditable(true);
        facilityIDCol.setCellValueFactory(new PropertyValueFactory<>("houseIDProp"));
        facilityIDCol.setCellFactory(numberCallback());
        facilityIDCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<House, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<House, Integer> t) {
                ((House) t.getTableView().getItems().get(t.getTablePosition().getRow())).setHouseID(t.getNewValue());

            }
        });
        neighborhoodCol = new TableColumn<>("Neighborhood");
        neighborhoodCol.setEditable(true);
        neighborhoodCol.setCellValueFactory(new PropertyValueFactory<>("houseLocationProp"));
        neighborhoodCol.setCellFactory(TextFieldTableCell.forTableColumn());
        neighborhoodCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<House, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<House, String> t) {
                ((House) t.getTableView().getItems().get(t.getTablePosition().getRow())).setHouseLocation(t.getNewValue());

            }
        });
        houseNameCol = new TableColumn<>("House Name");
        houseNameCol.setEditable(true);
        houseNameCol.setCellValueFactory(new PropertyValueFactory<>("houseNameProp"));
        houseNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        houseNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<House, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<House, String> t) {
                ((House) t.getTableView().getItems().get(t.getTablePosition().getRow())).setHouseName(t.getNewValue());

            }
        });
        houseIsActiveCol = new TableColumn<>("Is Active");
        houseIsActiveCol.setEditable(true);
        houseIsActiveCol.setCellValueFactory(new PropertyValueFactory<>("isActiveProp"));
        houseIsActiveCol.setCellFactory(CheckBoxTableCell.forTableColumn(houseIsActiveCol));
        houseIsActiveCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<House, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<House, Boolean> t) {
                ((House) t.getTableView().getItems().get(t.getTablePosition().getRow())).setIsActiveProp(t.getNewValue());

            }
        });
        tv_house.getColumns().addAll(neighborhoodCol, facilityIDCol, houseNameCol, houseIsActiveCol);
    }

    @FXML
    public void UpdateEmployeeButtonPressed() {
        DataPOJO addEmp = new DataPOJO();
        ObservableList<Employee> empList = tv_employee.getItems();
        ArrayList<Employee> strippedEmpList = new ArrayList<>();
        for (Employee emp : empList) {
            emp.setEmployeeIDProp(null);
            emp.setIsAdminProp(null);
            emp.setIsBackupProp(null);
            emp.setIsManagerProp(null);
            emp.setNameProp(null);
            emp.setPasswordProp(null);
            emp.setPhoneNumberProp(null);
            emp.setUsernameProp(null);
            strippedEmpList.add(emp);
        }
        addEmp.setAllEmployees(strippedEmpList);
        String response = WebAppTest.postToServer(Type.SHIFTY, Action.UPDATEEMEPLOYEE, gson.toJson(addEmp));
        System.out.println("Response: " + response);
    }

    @FXML
    public void groupRefreshButtonPressed(ActionEvent event) {
        tv_group.getItems().clear();
        DataPOJO result = gson.fromJson(WebAppTest.postToServer(Type.SHIFTY, Action.GETALLDATA, ""), DataPOJO.class);
        refreshAllTables(result);

    }

    @FXML
    public void groupDeleteButtonPressed(ActionEvent event) {
        //enables deleting of a facility
        groupDelete = true;

        //displays the facility id to be deleted inside the delete area
        groupDeleteDisplay.getChildren().add(new TextField(groupDeleteID.getText()));
        groupDeleteDisplay.getChildren().add(new TextField(groupDeleteName.getText()));

    }

    @FXML
    public void deleteGroupConfirmPressed(ActionEvent event) {
        DataPOJO delete = new DataPOJO();
        if (!(groupDeleteID.getText().isEmpty()) && (groupDelete == true) && !(groupDeleteName.getText().isEmpty())) {
            List<Group> groupListInner = new ArrayList<>();
            Group group = new Group();
            group.setGroupID(Integer.valueOf(groupDeleteID.getText()));
            group.setGroupName(groupDeleteName.getText());
            groupListInner.add(group);
            delete.setGroupList(groupListInner);
            WebAppTest.postToServer(Type.SHIFTY, Action.DELETEGROUP, gson.toJson(delete, DataPOJO.class));
            refreshButtonPressed(null);
        }

        //Disables deletes
        groupDelete = false;
        //clears the delete fields
        groupDeleteID.clear();
        groupDeleteName.clear();
        groupDeleteDisplay.getChildren().clear();
    }

    @FXML
    public void deleteGroupCancelPressed(ActionEvent event) {
        //Disables deletes
        groupDelete = false;
        //clears the delete fields
        groupDeleteID.clear();
        groupDeleteName.clear();
        groupDeleteDisplay.getChildren().clear();
    }

    @FXML //Deletes the employee if the textfields are used and the delete boolean has been set
    public void deleteHouseConfirmPressed(ActionEvent event) throws IOException {
        if (!(facDeleteID.getText().isEmpty()) && (facilityDelete == true) && !(facDeleteNeighborhood.getText().isEmpty())) {
            DataPOJO deleteHouse = new DataPOJO();
            List<House> houseList = new ArrayList<>();
            House house = new House();
            house.setHouseID(Integer.valueOf(facDeleteID.getText()));
            house.setHouseLocation(facDeleteNeighborhood.getText());
            houseList.add(house);
            deleteHouse.setHouseList(houseList);
            String result = WebAppTest.postToServer(Type.SHIFTY, Action.DELETEHOUSE, gson.toJson(deleteHouse, DataPOJO.class));
            System.out.println(result);
        }

        //disables facility deletes again.
        facilityDelete = false;
        facDeleteID.clear();
        facDeleteNeighborhood.clear();
        facDeleteDisplay.getChildren().clear();

    }

    @FXML //Pressed to cancel a delete facility action
    public void deleteHouseCancelPressed(ActionEvent event) throws IOException {
        //Disables deletes
        facilityDelete = false;
        //clears the delete fields
        facDeleteID.clear();
        facDeleteNeighborhood.clear();
        facDeleteDisplay.getChildren().clear();
    }

    @FXML //Pressed to initiate a delete for a facility
    public void deleteFacButtonPressed(ActionEvent event) throws IOException {
        //enables deleting of a facility
        facilityDelete = true;

        //displays the facility id to be deleted inside the delete area
        facDeleteDisplay.getChildren().add(new TextField(facDeleteID.getText()));
        facDeleteDisplay.getChildren().add(new TextField(facDeleteNeighborhood.getText()));
    }

    @FXML //Deletes the shift if the textfields are used and the delete boolean has been set
    public void deleteShiftConfirmPressed(ActionEvent event) throws IOException {
        if (!(shiftDeleteID.getText().isEmpty()) && (shiftDelete == true) && !(shiftDeleteEmpID.getText().isEmpty())) {
            DataPOJO deleteShift = new DataPOJO();
            List<House> houseList = new ArrayList<>();

            House house = new House();
            List<Shift> shiftListInner = new ArrayList<>();
            Shift shift = new Shift();
            shift.setShiftID(Integer.valueOf(shiftDeleteID.getText()));
            shift.setEmployeeID(Integer.valueOf(shiftDeleteEmpID.getText()));
            shiftListInner.add(shift);
            house.setShiftList(shiftListInner);
            houseList.add(house);
            deleteShift.setHouseList(houseList);
            String result = WebAppTest.postToServer(Type.SHIFTY, Action.DELETESHIFT, gson.toJson(deleteShift, DataPOJO.class));
        }

        //Resets the delete fields for the next use
        shiftDelete = false;
        shiftDeleteID.clear();
        shiftDeleteEmpID.clear();
        shiftDeleteDisplay.getChildren().clear();

    }

    @FXML //Pressed to cancel a delete employee action
    public void deleteShiftCancelPressed(ActionEvent event) throws IOException {
        //Disables deletes
        shiftDelete = false;

        //clears the delete fields
        shiftDeleteID.clear();
        shiftDeleteEmpID.clear();
        shiftDeleteDisplay.getChildren().clear();
    }

    @FXML
    public void shiftDeleteButtonPressed(ActionEvent event) {
        //enables deleting of a shift
        shiftDelete = true;

        //displays the shift information to be deleted inside the delete area
        shiftDeleteDisplay.getChildren().add(new TextField(shiftDeleteID.getText()));
        shiftDeleteDisplay.getChildren().add(new TextField(shiftDeleteEmpID.getText()));

    }

    @FXML //Deletes the employee if the textfields are used and the delete boolean has been set
    public void deleteConfirmPressed(ActionEvent event) throws IOException {
        if (!(empDeleteID.getText().isEmpty()) && (employeeDelete == true) && !(empDeleteName.getText().isEmpty())) {
            DataPOJO deleteEmp = new DataPOJO();
            List<Employee> empList = new ArrayList<>();
            Employee emp = new Employee();
            emp.setEmployeeID(Integer.valueOf(empDeleteID.getText()));
            empList.add(emp);
            deleteEmp.setAllEmployees(empList);
            String response = WebAppTest.postToServer(Type.SHIFTY, Action.DELETEEMPLOYEE, gson.toJson(deleteEmp, DataPOJO.class));
            System.out.println(response);
        }

        //disables employee deletes again.
        employeeDelete = false;
        empDeleteID.clear();
        empDeleteName.clear();
        empDeleteDisplay.getChildren().clear();

    }

    @FXML //Pressed to cancel a delete employee action
    public void deleteCancelPressed(ActionEvent event) throws IOException {
        //Disables deletes
        employeeDelete = false;
        //clears the delete fields
        empDeleteID.clear();
        empDeleteName.clear();
        empDeleteDisplay.getChildren().clear();
    }

    @FXML //Pressed to initiate a delete for an employee
    public void deleteEmpButtonPressed(ActionEvent event) throws IOException {
        //enables deleting of an employee
        employeeDelete = true;

        //displays the employee id to be deleted inside the delete area
        empDeleteDisplay.getChildren().add(new TextField(empDeleteID.getText()));
        empDeleteDisplay.getChildren().add(new TextField(empDeleteName.getText()));
    }

    @FXML //DIsplays the shifts from the server
    public void refreshShiftsButtonPressed(ActionEvent event) {
        tv_shift.getItems().clear();
        //get the data of employees from the server
        DataPOJO result = gson.fromJson(WebAppTest.postToServer(Type.SHIFTY, Action.GETALLDATA, ""), DataPOJO.class);
        refreshAllTables(result);
    }

    @FXML //Adds the fields from the window as a shift to the database
    public void addShiftButtonPressed(ActionEvent event) {
        if (shiftTime.getText().isEmpty()) {
            lbl_shift_error.setText("Shift time required!");
            WebAppTest.setCSS(true, shiftTime);
            return;
        } else {
            WebAppTest.setCSS(false, shiftTime);
        }
        if (tf_shift_name.getText().isEmpty()) {
            lbl_shift_error.setText("Shift name required!");
            WebAppTest.setCSS(true, tf_shift_name);
            return;
        } else {
            WebAppTest.setCSS(false, tf_shift_name);
        }
        if (shiftEmpID.getText().isEmpty()) {
            lbl_shift_error.setText("Employee ID required!");
            WebAppTest.setCSS(true, shiftEmpID);
            return;
        } else {
            WebAppTest.setCSS(false, shiftEmpID);
        }
        if (shiftHouseID.getText().isEmpty()) {
            lbl_shift_error.setText("Shift House ID required!");
            WebAppTest.setCSS(true, shiftHouseID);
            return;
        } else {
            WebAppTest.setCSS(false, shiftHouseID);
        }
        lbl_shift_error.setText("");
        String sTime = shiftTime.getText();
        String empIDInner = shiftEmpID.getText();
        String fID = shiftHouseID.getText();
        String shiftname = tf_shift_name.getText();
        DataPOJO addShift = new DataPOJO();
        List<House> houseList = new ArrayList<>();
        List<Shift> shiftListInner = new ArrayList<>();
        House house = new House();
        Shift shift = new Shift();
        house.setHouseID(Integer.valueOf(fID));
        shift.setHouseID(Integer.valueOf(fID));
        shift.setEmployeeID(Integer.valueOf(empIDInner));
        shift.setName(shiftname);
        shift.setTime(sTime);
        shiftListInner.add(shift);
        house.setShiftList(shiftListInner);
        houseList.add(house);
        addShift.setHouseList(houseList);

        String response = WebAppTest.postToServer(Type.SHIFTY, Action.ADDSHIFT, gson.toJson(addShift, DataPOJO.class));
        System.out.println(response);
    }

    @FXML //Displays the list of facilities
    public void refreshHouse(ActionEvent event) {
        tv_house.getItems().clear();
        DataPOJO result = gson.fromJson(WebAppTest.postToServer(Type.SHIFTY, Action.GETALLDATA, ""), DataPOJO.class);
        refreshAllTables(result);
    }

    @FXML //Displays the employees in the database
    public void refreshButtonPressed(ActionEvent event) {
        tv_employee.getItems().clear();
        //get the data of employees from the server
        DataPOJO result = gson.fromJson(WebAppTest.postToServer(Type.SHIFTY, Action.GETALLDATA, ""), DataPOJO.class);
        refreshAllTables(result);
    }

    @FXML //Provides the code that will add a new facility
    public void addHouseButton(ActionEvent event) {
        if (tf_house_name.getText().isEmpty()) {
            lbl_house_error.setText("House name required!");
            WebAppTest.setCSS(true, tf_house_name);
            return;
        } else {
            WebAppTest.setCSS(false, tf_house_name);
        }
        if (tf_house_neighborhood.getText().isEmpty()) {
            lbl_house_error.setText("House neighborhood required!");
            WebAppTest.setCSS(true, tf_house_neighborhood);
            return;
        } else {
            WebAppTest.setCSS(false, tf_house_neighborhood);
        }
        lbl_house_error.setText("");
        DataPOJO addHouse = new DataPOJO();
        List<House> houseList = new ArrayList<>();
        House house = new House();
        house.setHouseName(tf_house_name.getText());
        house.setHouseLocation(tf_house_neighborhood.getText());
        houseList.add(house);
        addHouse.setHouseList(houseList);

        String response = WebAppTest.postToServer(Type.SHIFTY, Action.ADDHOUSE, gson.toJson(addHouse, DataPOJO.class));
        System.out.println(response);
    }

    @FXML //Provides the code that will add a new employee
    public void employeeButtonPressed(ActionEvent event) {
        if (empName.getText().isEmpty()) {
            lbl_employee_error.setText("Employee name required!");
            WebAppTest.setCSS(true, empName);
            return;
        } else {
            WebAppTest.setCSS(false, empName);
        }
        if (empPhone.getText().isEmpty()) {
            lbl_employee_error.setText("Employee phone number required!");
            WebAppTest.setCSS(true, empPhone);
            return;
        } else {
            WebAppTest.setCSS(false, empPhone);
        }
        if (empUserName.getText().isEmpty()) {
            lbl_employee_error.setText("Employee username required!");
            WebAppTest.setCSS(true, empUserName);
            return;
        } else {
            WebAppTest.setCSS(false, empUserName);
        }
        if (empPassword.getText().isEmpty()) {
            lbl_employee_error.setText("Employee password required!");
            WebAppTest.setCSS(true, empPassword);
            return;
        } else {
            WebAppTest.setCSS(false, empPassword);
        }
        lbl_employee_error.setText("");
        DataPOJO addEmp = new DataPOJO();
        String password = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            password = (Hex.encodeHexString(md.digest(empPassword.getText().getBytes())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
        Employee emp = new Employee();
        emp.setName(empName.getText());
        emp.setPhoneNumber(Long.valueOf(empPhone.getText()));
        emp.setIsManager(managerCheck.isSelected());
        emp.setUsername(empUserName.getText());
        emp.setPassword(password);
        emp.setIsAdmin(cb_employee_admin.isSelected());
        emp.setIsBackup(cb_employee_backup.isSelected());
        List<Employee> empList = new ArrayList<>();
        empList.add(emp);
        addEmp.setAllEmployees(empList);

        //addEmp.set(Long.valueOf(empPhone.getText()));
        String response = WebAppTest.postToServer(Type.SHIFTY, Action.ADDEMPLOYEE, gson.toJson(addEmp, DataPOJO.class));
        System.out.println("This is the result of adding the employee: " + response);
    }

    private void refreshAllTables(DataPOJO inputPOJO) {
        List<Shift> shiftListInner = new ArrayList<>();
        for (House house : inputPOJO.getHouseList()) {
            for (Shift shift : house.getShiftList()) {
                shift.setNameProp(shift.getName());
                shift.setTimeProp(shift.getTime());
                shift.setEmployeeIDProp(shift.getEmployeeID());
                shift.setHouseIDProp(shift.getHouseID());
                shift.setShiftIDProp(shift.getShiftID());
                shiftListInner.add(shift);
            }
        }
        ObservableList<Shift> ol2 = FXCollections.observableArrayList();
        ol2.addAll(shiftListInner);
        tv_shift.setItems(ol2);

        ObservableList<Employee> ol = FXCollections.observableArrayList();
        List<Employee> empList = inputPOJO.getAllEmployees();
        for (Employee emp : empList) {
            emp.getName();
            emp.setEmployeeIDProp(emp.getEmployeeID());
            emp.setNameProp(emp.getName());
            emp.setUsernameProp(emp.getUsername());
            emp.setPhoneNumberProp(((Long) emp.getPhoneNumber()).intValue());
            emp.setIsAdminProp(emp.isIsAdmin());
            emp.setIsBackupProp(emp.isIsBackup());
            emp.setIsManagerProp(emp.isIsManager());
        }
        ol.addAll(empList);
        tv_employee.setItems(ol);

        List<House> houseList = inputPOJO.getHouseList();
        for (House house : houseList) {
            house.setHouseIDProp(house.getHouseID());
            house.setHouseLocationProp(house.getHouseLocation());
            house.setHouseNameProp(house.getHouseName());
            house.setIsActiveProp(house.isIsActive());
        }
        ObservableList<House> ol3 = FXCollections.observableArrayList();
        ol3.addAll(houseList);
        tv_house.setItems(ol3);

        List<Group> groupListInner = inputPOJO.getGroupList();
        int i = 0;
        int currentGroupID = 0;
        for (Group group : groupListInner) {
            if (currentGroupID == 0 || currentGroupID != group.getGroupID()) {
                i = 0;
                currentGroupID = group.getGroupID();
            }
            group.setGroupEmployeeNameProp(group.getEmpList().get(i).getName());
            group.setGroupEmployeeIDProp(group.getEmpList().get(i).getEmployeeID());
            group.setGroupIDProp(group.getGroupID());
            group.setGroupNameProp(group.getGroupName());
            group.setManagerIDProp(group.getManagerID());
            i++;
        }
        ObservableList<Group> ol4 = FXCollections.observableArrayList();
        ol4.addAll(groupListInner);
        tv_group.setItems(ol4);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeeDelete = false;
        facilityDelete = false;
        shiftDelete = false;

        initializeEmployeeTableColumns();
        initializeShiftTableColumns();
        initializeHouseColumns();
        initializeGroupColumns();
        refreshAllTables(LogInScreenController.completeInfo);
    }
}
