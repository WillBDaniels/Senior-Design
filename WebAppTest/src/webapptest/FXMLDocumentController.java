/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapptest;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.codec.binary.Hex;
import static webapptest.WebAppTest.sendData;

/**
 *
 * @author Mitch McLaughlin
 */
public class FXMLDocumentController implements Initializable {
    //Areas for the database stored information to be displayed.
    @FXML private VBox employeeList;
    @FXML private VBox facilityList;
    @FXML private VBox shiftList;
    @FXML private VBox groupList;
    
    //Fields for the employee addition
    @FXML private CheckBox managerCheck;
    @FXML private TextField empName;
    @FXML private TextField empPhone;
    @FXML private TextField empID;
    @FXML private TextField empUserName;
    @FXML private TextField empPassword;
    
    //Fields for the facility addition
    @FXML private TextField facilityID;
    @FXML private TextField facilityNeighborhood;
    
    //Fields for the shift addition
    @FXML private TextField shiftTime;
    @FXML private TextField shiftEmpID;
    @FXML private TextField shiftFacilityID;
    
    //Fields for the group addition
    @FXML private TextField groupName;
    @FXML private TextField groupManagerID;
    @FXML private TextField groupID;
    @FXML private TextField groupEmpIDs;
    
    //Fields for the employee delete
    @FXML private TextField empDeleteID;
    @FXML private TextField empDeleteName;
    
    //Fields for the shift delete
    @FXML private TextField shiftDeleteID;
    @FXML private TextField shiftDeleteEmpID;
    
    //Fields for the facility delete
    @FXML private TextField facDeleteID;
    @FXML private TextField facDeleteNeighborhood;
    
    //Fields for the group delete
    @FXML private TextField groupDeleteID;
    @FXML private TextField groupDeleteName;
    
    //Areas for the deletion information to be displayed 
    @FXML private VBox empDeleteDisplay;
    @FXML private VBox shiftDeleteDisplay;
    @FXML private VBox facDeleteDisplay;
    @FXML private VBox groupDeleteDisplay;
    
    //Booleans to check if it is ok to delete
    private Boolean employeeDelete;
    private Boolean shiftDelete;
    private Boolean facilityDelete;
    private Boolean groupDelete;
    
    @FXML
    public void groupCreateButtonPressed(ActionEvent event){
        List<String> temp = new ArrayList<>();
        temp.add("create_group");
        temp.add("managerID=" + groupManagerID.getText());
        temp.add("groupName=\"" + groupName.getText() + "\"");
        
        temp.add("groupID=" + groupID.getText());
        //add employee ids here
        ArrayList<String> eList = new ArrayList<>(Arrays.asList(groupEmpIDs.getText().split("\\&")));
        for( String e : eList){
            temp.add(e);
        }
        temp.add("Done");
        
        sendData(temp);
    }
    
    public List<Group> parseGroupData(String input){
        List<Member> mmbr = new ArrayList<>();
        System.out.println(input);
        
        //Loops through the string from the server
        ArrayList<String> rows = new ArrayList<>(Arrays.asList(input.split("\\|")));
        //This loops through each employee record
        for (String row : rows){
            Member temp = new Member();
            ArrayList<String> cols = new ArrayList<>(Arrays.asList(row.split("\\&")));
            //The loops through each field of the group member.
            for (String col : cols){
                col = col.replaceAll("\"", "");
                ArrayList<String> entry = new ArrayList<>(Arrays.asList(col.split("=")));
                //Adds the found field to the temporary group object
                switch(entry.get(0)){
                    case "groupName": temp.setName(entry.get(1));
                        break;
                    case "ID": temp.setID(entry.get(1));
                        break;
                    case "ManagerID": temp.setManager(entry.get(1));
                        break;
                    case "employeeID": temp.addEmp(entry.get(1));
                        break;
                    case "groupID": temp.setGroupID(entry.get(1));
                        break;
                    default:
                        System.out.println(entry.get(0));
                }
            }  
            //We have looped through each field of the current employee.
            //Now we add this employee to our structure of employees
            mmbr.add(temp);
        }
        //loop through the members and separate them into groups
        //
        //creates a list of groups
        List<Group> grps = new ArrayList();
        for( Member m : mmbr){
            String g = m.getGroupID();
            if(!grps.isEmpty()){ //if the group isn't empty
                //Check if group id exists
                String correctGroupID = "";
                for(Group gr : grps){//Finds the correct group if there is one
                    if(gr.getGroupID().equals(g)){
                        correctGroupID = g;
                    }
                }
                if(correctGroupID.equals(g)){//the group existed
                    for(Group currGroup : grps){
                        if(currGroup.getGroupID().equals(g)){
                            //The current group is where the member belongs
                            
                            //Adds the member to the group
                            currGroup.addEmp(m);
                            
                        }
                    }
                       
                    }else{//The current members group doesnt exist
                        Group _group = new Group();
                        //Sets the data of the current member for the new group
                        _group.addEmp(m);
                        _group.setGroupID(m.getGroupID());
                        _group.setID(m.getID());
                        _group.setManager(m.getManager());
                        _group.setName(m.getName());
                        
                        //Adds the group to the list of groups
                        grps.add(_group);
                    }
            }else{ //The group list was empty
                //Create a new group
                Group tmp = new Group();
                //add current employee to new group
                tmp.addEmp(m);
                tmp.setGroupID(m.getGroupID());
                tmp.setID(m.getID());
                tmp.setManager(m.getManager());
                tmp.setName(m.getName());
                //Add new group to the list
                grps.add(tmp);
            }
        }
        return grps;
    }
    
    @FXML
    public void groupRefreshButtonPressed(ActionEvent event){
        groupList.getChildren().clear();
        
        //get the data of employees from the server
        List<String> temp = new ArrayList<>();
        temp.add("get_group");
        String input = sendData(temp);
        
        //parse the data into an object
        List<Group> grps = parseGroupData(input);
        
        //display the headings for the group display
        HBox heading = new HBox();
        Label ID = new Label("Database ID");
        Label gid = new Label("Group ID");
        Label name = new Label("Group Name");
        Label manager = new Label("Manager ID");
        Label employee = new Label("Employee ID");
        
        heading.getChildren().add(ID);
        heading.getChildren().add(gid);
        heading.getChildren().add(name);
        heading.getChildren().add(manager);
        heading.getChildren().add(employee);
        
        
        groupList.getChildren().add(heading);
        for (Group grp : grps) {
            
            List<Member> member = grp.getEmps();
            
            for(Member mem : member){ //Loops through the members of the group
                HBox tempLine = new HBox();
                TextField tfid = new TextField( mem.getID() );
                TextField tfname = new TextField(mem.getName() );
                TextField tfgroupid = new TextField(mem.getGroupID());
                TextField tfmanager = new TextField(mem.getManager());
                TextField tfemp = new TextField(mem.getEmps() );
            
                tempLine.getChildren().add(tfid);
                tempLine.getChildren().add(tfname);
                tempLine.getChildren().add(tfgroupid);
                tempLine.getChildren().add(tfmanager);
                tempLine.getChildren().add(tfemp);
                
                groupList.getChildren().add(tempLine);
            }
        }
    }
    
    @FXML
    public void groupDeleteButtonPressed(ActionEvent event){
        //enables deleting of a facility
        groupDelete = true;
        
        //displays the facility id to be deleted inside the delete area
        groupDeleteDisplay.getChildren().add(new TextField(groupDeleteID.getText()));
        groupDeleteDisplay.getChildren().add(new TextField(groupDeleteName.getText()));
    
    }
    
    @FXML
    public void deleteGroupConfirmPressed(ActionEvent event){
        if(!( groupDeleteID.getText().isEmpty() ) && (groupDelete == true) && !( groupDeleteName.getText().isEmpty() ) ){
            List<String> temp = new ArrayList<>();
            temp.add("delete_group");
            temp.add("groupID=" + groupDeleteID.getText());
            temp.add("groupName=\"" + groupDeleteName.getText() + "\"");
        
            String debug = sendData(temp);
            System.out.println(debug);
        }
        
        //Disables deletes
        groupDelete = false;
        //clears the delete fields
        groupDeleteID.clear();
        groupDeleteName.clear();
        groupDeleteDisplay.getChildren().clear();
    }
    
    @FXML
    public void deleteGroupCancelPressed(ActionEvent event){
        //Disables deletes
        groupDelete = false;
        //clears the delete fields
        groupDeleteID.clear();
        groupDeleteName.clear();
        groupDeleteDisplay.getChildren().clear();
    }
    
    @FXML //Deletes the employee if the textfields are used and the delete boolean has been set
    public void deleteFacilityConfirmPressed(ActionEvent event) throws IOException{
        if(!( facDeleteID.getText().isEmpty() ) && (facilityDelete == true) && !( facDeleteNeighborhood.getText().isEmpty() ) ){
            List<String> temp = new ArrayList<>();
            temp.add("delete_house");
            temp.add("house_id=" + facDeleteID.getText());
            temp.add("`house_neighborhood`=\"" + facDeleteNeighborhood.getText() + "\"");
        
            String debug = sendData(temp);
            System.out.println(debug);
        }
        
        //disables facility deletes again.
        facilityDelete = false;
        facDeleteID.clear();
        facDeleteNeighborhood.clear();
        facDeleteDisplay.getChildren().clear();
        
    }
    
    @FXML //Pressed to cancel a delete facility action
    public void deleteFacilityCancelPressed(ActionEvent event) throws IOException{
        //Disables deletes
        facilityDelete = false;
        //clears the delete fields
        facDeleteID.clear();
        facDeleteNeighborhood.clear();
        facDeleteDisplay.getChildren().clear();
    }
    
    @FXML //Pressed to initiate a delete for a facility
    public void deleteFacButtonPressed(ActionEvent event) throws IOException{
        //enables deleting of a facility
        facilityDelete = true;
        
        //displays the facility id to be deleted inside the delete area
        facDeleteDisplay.getChildren().add(new TextField(facDeleteID.getText()));
        facDeleteDisplay.getChildren().add(new TextField(facDeleteNeighborhood.getText()));
    }
    
    
    @FXML //Deletes the shift if the textfields are used and the delete boolean has been set
    public void deleteShiftConfirmPressed(ActionEvent event) throws IOException{
        if(!( shiftDeleteID.getText().isEmpty() ) && (shiftDelete == true) && !( shiftDeleteEmpID.getText().isEmpty() ) ){
            List<String> temp = new ArrayList<>();
            temp.add("delete_shift");
            temp.add("shift_id=" + shiftDeleteID.getText() );
            temp.add("employee1=" + shiftDeleteEmpID.getText() );
        
            String debug = sendData(temp);
            System.out.println(debug);
        }
        
        //Resets the delete fields for the next use
        shiftDelete = false;
        shiftDeleteID.clear();
        shiftDeleteEmpID.clear();
        shiftDeleteDisplay.getChildren().clear();
        
    }
    
    @FXML //Pressed to cancel a delete employee action
    public void deleteShiftCancelPressed(ActionEvent event) throws IOException{
        //Disables deletes
        shiftDelete = false;
        
        //clears the delete fields
        shiftDeleteID.clear();
        shiftDeleteEmpID.clear();
        shiftDeleteDisplay.getChildren().clear();
    }
    
    @FXML
    public void shiftDeleteButtonPressed(ActionEvent event){
        //enables deleting of a shift
        shiftDelete = true;
        
        //displays the shift information to be deleted inside the delete area
        shiftDeleteDisplay.getChildren().add(new TextField(shiftDeleteID.getText()));
        shiftDeleteDisplay.getChildren().add(new TextField(shiftDeleteEmpID.getText()));
    
    }
    
    @FXML //Deletes the employee if the textfields are used and the delete boolean has been set
    public void deleteConfirmPressed(ActionEvent event) throws IOException{
        if(!( empDeleteID.getText().isEmpty() ) && (employeeDelete == true) && !( empDeleteName.getText().isEmpty() ) ){
            List<String> temp = new ArrayList<>();
            temp.add("delete_user");
            temp.add("`employee_id`=" + empDeleteID.getText());
            temp.add("`username`=\"" + empDeleteName.getText() + "\"");
        
            String debug = sendData(temp);
            System.out.println(debug);
        }
        
        //disables employee deletes again.
        employeeDelete = false;
        empDeleteID.clear();
        empDeleteName.clear();
        empDeleteDisplay.getChildren().clear();
        
    }
    
    @FXML //Pressed to cancel a delete employee action
    public void deleteCancelPressed(ActionEvent event) throws IOException{
        //Disables deletes
        employeeDelete = false;
        //clears the delete fields
        empDeleteID.clear();
        empDeleteName.clear();
        empDeleteDisplay.getChildren().clear();
    }
    
    @FXML //Pressed to initiate a delete for an employee
    public void deleteEmpButtonPressed(ActionEvent event) throws IOException{
        //enables deleting of an employee
        employeeDelete = true;
        
        //displays the employee id to be deleted inside the delete area
        empDeleteDisplay.getChildren().add(new TextField(empDeleteID.getText()));
        empDeleteDisplay.getChildren().add(new TextField(empDeleteName.getText()));
    }
    
    @FXML //DIsplays the shifts from the server
    public void refreshShiftsButtonPressed(ActionEvent event){
        shiftList.getChildren().clear();
        
        //get the data of employees from the server
        List<String> temp = new ArrayList<>();
        temp.add("get_shift_specific");
        String input = sendData(temp);
        
        //parse the data into an object
        List<Shift> shifts = parseShiftData(input);
        
        //display the headings for the employee display
        HBox heading = new HBox();
        Label sID = new Label("Shift ID");
        Label sTime = new Label("Shift Time");
        Label sEmp = new Label("Shift Employee");
        Label sFac = new Label("Shift House");
        
        heading.getChildren().add(sID);
        heading.getChildren().add(sTime);
        heading.getChildren().add(sEmp);
        heading.getChildren().add(sFac);
        
        shiftList.getChildren().add(heading);
        
        //loop through all available employees and display to screen
        for( Shift s : shifts){
            HBox tempLine = new HBox();
           
            TextField sIDText = new TextField(s.getShiftID());
            TextField sTimeText = new TextField(s.getShiftTime());
            TextField sEmpText = new TextField(s.getEmpID());
            TextField sFacText = new TextField(s.getHouseID());
            
            tempLine.getChildren().add(sIDText);
            tempLine.getChildren().add(sTimeText);
            tempLine.getChildren().add(sEmpText);
            tempLine.getChildren().add(sFacText);
            
            shiftList.getChildren().add(tempLine);
        }
    }
    
    // Used internally for parsing the data from the server
    public List<Shift> parseShiftData(String input){
        List<Shift> shifts = new ArrayList<>();
        System.out.println(input);
        
        //Loops through the string from the server
        ArrayList<String> rows = new ArrayList<>(Arrays.asList(input.split("\\|")));
        //This loops through each employee record
        for (String row : rows){
            Shift temp = new Shift();
            ArrayList<String> cols = new ArrayList<>(Arrays.asList(row.split("\\&")));
            //The loops through each field of the employee record.
            for (String col : cols){
                col = col.replaceAll("\"", "");
                ArrayList<String> entry = new ArrayList<>(Arrays.asList(col.split("=")));
                //Adds the found field to the temporary employee object
                switch(entry.get(0)){
                    case "shift_id": temp.setShiftID(entry.get(1));
                        break;
                    case "shift_time": temp.setShiftTime(entry.get(1));
                        break;
                    case "location": temp.setHouseID(entry.get(1));
                        break;
                    case "employee1": temp.setEmpID(entry.get(1));
                        break;
                    default:
                        System.out.println(entry.get(0));
                }
            }  
            //We have looped through each field of the current facility.
            //Now we add this facility to our structure of facilities
            shifts.add(temp);
        }
        return shifts;
    }
    
    @FXML //Adds the fields from the window as a shift to the database
    public void addShiftButtonPressed(ActionEvent event){
        List<String> temp = new ArrayList<>();
        String sTime = shiftTime.getText();
        String empID = shiftEmpID.getText();
        String fID = shiftFacilityID.getText();
        temp.add("new_shift");
        temp.add("\"" + sTime + "\"");
        temp.add(empID);
        temp.add(fID);
        
        String debug = sendData(temp);
        System.out.println(debug);
    }
    
    @FXML //Displays the list of facilities
    public void refreshFacility(ActionEvent event){
        facilityList.getChildren().clear();
        
        //get the data of employees from the server
        List<String> temp = new ArrayList<>();
        temp.add("get_all_houses");
        String input = sendData(temp);
        
        //parse the data into an object
        List<Facility> facs = parseFacilityData(input);
        
        //display the headings for the employee display
        HBox heading = new HBox();
        Label id = new Label("House ID");
        Label neighborhood = new Label("House Neighborhood");
        
        heading.getChildren().add(id);
        heading.getChildren().add(neighborhood);
        
        facilityList.getChildren().add(heading);
        
        //loop through all available employees and display to screen
        for( Facility fac : facs){
            HBox tempLine = new HBox();
            
            TextField hid = new TextField( fac.getHouseID());
            TextField hneighborhood = new TextField(fac.getHouseNeighborhood());
            
            tempLine.getChildren().add(hid);
            tempLine.getChildren().add(hneighborhood);
            
            
            facilityList.getChildren().add(tempLine);
        }
    }
    
    //Used internally to parse the list of facilities from the server
    public List<Facility> parseFacilityData(String input){
        List<Facility> facs = new ArrayList<>();
        System.out.println(input);
        
        //Loops through the string from the server
        ArrayList<String> rows = new ArrayList<>(Arrays.asList(input.split("\\|")));
        //This loops through each employee record
        for (String row : rows){
            Facility temp = new Facility();
            ArrayList<String> cols = new ArrayList<>(Arrays.asList(row.split("\\&")));
            //The loops through each field of the employee record.
            for (String col : cols){
                col = col.replaceAll("\"", "");
                ArrayList<String> entry = new ArrayList<>(Arrays.asList(col.split("=")));
                //Adds the found field to the temporary employee object
                switch(entry.get(0)){
                    case "house_id": temp.setHouseID(entry.get(1));
                        break;
                    case "house_neighborhood": temp.setHouseNeighborhood(entry.get(1));
                        break;
                    default:
                        System.out.println(entry.get(0));
                }
            }  
            //We have looped through each field of the current facility.
            //Now we add this facility to our structure of facilities
            facs.add(temp);
        }
        return facs;
    }
    
    @FXML //Displays the employees in the database
    public void refreshButtonPressed(ActionEvent event){
        employeeList.getChildren().clear();
        
        //get the data of employees from the server
        List<String> temp = new ArrayList<>();
        temp.add("employee_info");
        String input = sendData(temp);
        
        //parse the data into an object
        List<Employee> emps = parseEmpData(input);
        
        //display the headings for the employee display
        HBox heading = new HBox();
        Label id = new Label("Employee ID");
        Label name = new Label("Employee Name");
        Label phone = new Label("Employee Phone Number");
        Label user = new Label("Employee User Name");
        Label manager = new Label("Is Manager?");
        Label backup = new Label("Is Backup?");
        
        heading.getChildren().add(id);
        heading.getChildren().add(name);
        heading.getChildren().add(phone);
        heading.getChildren().add(user);
        heading.getChildren().add(manager);
        heading.getChildren().add(backup);
        
        employeeList.getChildren().add(heading);
        
        //loop through all available employees and display to screen
        for( Employee emp : emps){
            HBox tempLine = new HBox();
            
            String managerString;
            if( emp.getManager() == true){
                managerString = "true";
            }else{
                managerString = "false";
            }
            String backupString;
            if( emp.getBackup() == true){
                backupString = "true";
            }else{
                backupString = "false";
            }
            
            
            TextField tfid = new TextField( Integer.toString( emp.getID() ));
            TextField tfname = new TextField(emp.getName());
            TextField tfphone = new TextField(emp.getPhone());
            TextField tfuser = new TextField(emp.getUserName());
            TextField tfmanager = new TextField(managerString);
            TextField tfbackup = new TextField(backupString);
            
            tempLine.getChildren().add(tfid);
            tempLine.getChildren().add(tfname);
            tempLine.getChildren().add(tfphone);
            tempLine.getChildren().add(tfuser);
            tempLine.getChildren().add(tfmanager);
            tempLine.getChildren().add(tfbackup);
            
            
            employeeList.getChildren().add(tempLine);
        }
    }
    
    //Used internally to help parse the list of employees recieved from the database
    public List<Employee> parseEmpData(String input){
        List<Employee> emps = new ArrayList<>();
        System.out.println(input);
        
        //Loops through the string from the server
        ArrayList<String> rows = new ArrayList<>(Arrays.asList(input.split("\\|")));
        //This loops through each employee record
        for (String row : rows){
            Employee temp = new Employee();
            ArrayList<String> cols = new ArrayList<>(Arrays.asList(row.split("\\&")));
            //The loops through each field of the employee record.
            for (String col : cols){
                col = col.replaceAll("\"", "");
                ArrayList<String> entry = new ArrayList<>(Arrays.asList(col.split("=")));
                //Adds the found field to the temporary employee object
                switch(entry.get(0)){
                    case "employee_name": temp.setName(entry.get(1));
                        break;
                    case "employee_id": temp.setID(Integer.parseInt(entry.get(1)));
                        break;
                    case "is_manager": temp.setManager(entry.get(1).equals("1"));
                        break;
                    case "phone_number": temp.setPhone(entry.get(1));
                        break;
                    case "is_backup": temp.setBackup(entry.get(1).equals("1"));
                        break;
                    case "username": temp.setUserName(entry.get(1));
                        break;
                    default:
                        System.out.println(entry.get(0));
                }
            }  
            //We have looped through each field of the current employee.
            //Now we add this employee to our structure of employees
            emps.add(temp);
        }
        return emps;
    }
    
    @FXML //Provides the code that will add a new facility
    public void addFacilityButton(ActionEvent event){
        List<String> temp = new ArrayList<>();
        String fID = facilityID.getText();
        String neighborhood = facilityNeighborhood.getText();
        temp.add("new_house");
        temp.add(fID);
        temp.add("\"" + neighborhood + "\"");
        
        String facilityDebug = sendData(temp);
        System.out.println(facilityDebug);
    }
    
    @FXML //Provides the code that will add a new employee
    public void employeeButtonPressed(ActionEvent event){
        String password = "";
        List<String> temp = new ArrayList<>();
        temp.add("new_user");
        temp.add("\"" + empID.getText() + "\"");
        temp.add("\"" + empName.getText() + "\"");
        if(managerCheck.isSelected() )
        {
            temp.add("1");
        }
        else
        {
            temp.add("0");
        }
        temp.add("\"" + empUserName.getText() + "\"");
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            password = (Hex.encodeHexString(md.digest(empPassword.getText().getBytes())));
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace(System.err);
        }
        temp.add("\"" + password + "\"");
        temp.add("\"" + empPhone.getText() + "\"");
        sendData(temp);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeeDelete = false;
        facilityDelete = false;
        shiftDelete = false;
    }       
}
    
