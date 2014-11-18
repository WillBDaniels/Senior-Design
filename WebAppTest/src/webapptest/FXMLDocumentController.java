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
 * @author fritz
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private Label label;
    @FXML private VBox displayUsers;
    @FXML private VBox employeeList;
    @FXML private CheckBox managerCheck;
    @FXML private TextField empName;
    @FXML private TextField empPhone;
    @FXML private TextField empID;
    @FXML private TextField empUserName;
    @FXML private TextField empPassword;
    @FXML private TextField facilityID;
    @FXML private TextField facilityNeighborhood;
    
    
    
    @FXML
    public void userButtonPressed(ActionEvent event) throws IOException{
        System.out.println("User Button Clicked");
    }
    
    @FXML
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
    
    @FXML
    public void addFacilityButton(ActionEvent event){
        List<String> temp = new ArrayList<>();
        String fID = facilityID.getText();
        String neighborhood = facilityNeighborhood.getText();
        temp.add("new_house");
        temp.add("house_id=" + fID );
        temp.add("house_neighborhood=\"" + neighborhood + "\"");
        
        sendData(temp);
    }
    
    @FXML
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
        // TODO
    }       
}
    
