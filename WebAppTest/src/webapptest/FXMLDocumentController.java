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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;
import static webapptest.WebAppTest.sendData;

/**
 *
 * @author fritz
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private Label label;
    @FXML private VBox displayUsers;
    @FXML private VBox employeeList;
    @FXML private VBox facilityList;
    @FXML private VBox shiftList;
    @FXML private VBox deleteEmpBox;
    @FXML private CheckBox managerCheck;
    @FXML private TextField empName;
    @FXML private TextField empPhone;
    @FXML private TextField empID;
    @FXML private TextField empUserName;
    @FXML private TextField empPassword;
    @FXML private TextField facilityID;
    @FXML private TextField facilityNeighborhood;
    @FXML private TextField shiftTime;
    @FXML private TextField shiftEmpID;
    @FXML private TextField shiftFacilityID;
    @FXML private TextField empDeleteID;
    @FXML private TextField empDeleteName;
    @FXML private VBox empDeleteDisplay;
    private Boolean employeeDelete;
    
    
    @FXML
    public void shiftDeleteButtonPressed(ActionEvent event){
        
    }
    
    @FXML //If the textfield isnt empty then it deletes the chosen user
    public void deleteConfirmPressed(ActionEvent event) throws IOException{
        if(!( empDeleteID.getText().isEmpty() ) && (employeeDelete == true) && !( empDeleteName.getText().isEmpty() ) ){
            List<String> temp = new ArrayList<>();
            temp.add("delete_user");
            temp.add("\"employee_id\"=" + empDeleteID.getText());
            temp.add("\"employee_name\"=" + empDeleteName.getText() + "\"");
        
            String debug = sendData(temp);
            System.out.println(debug);
        }
        
        //disables employee deletes again.
        employeeDelete = false;
        empDeleteID.clear();
        empDeleteName.clear();
        empDeleteDisplay.getChildren().clear();
        
    }
    
    @FXML //Pressed to cancel a delete
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
    }       
}
    
