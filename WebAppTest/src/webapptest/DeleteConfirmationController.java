/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapptest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static webapptest.WebAppTest.sendData;

/**
 *
 * @author fritz
 */
public class DeleteConfirmationController {
    
    @FXML private TextField deleteID;
    private String empIDDelete;
    
    //Sets the employee ID to be deleted
    //Will be called from the main controller file
    public void setEmpID(String employeeID){
        this.empIDDelete = employeeID;
        deleteID.setText(employeeID);
    }
    
    @FXML //If the textfield isnt empty then it deletes the chosen user
    public void deleteConfirmPressed(ActionEvent event) throws IOException{
        if(!(empIDDelete.trim().length() == 0)){
            List<String> temp = new ArrayList<>();
            temp.add("delete_user");
            temp.add("\"" + empIDDelete + "\"");
        
            String debug = sendData(temp);
            System.out.println(debug);
        }
        
        FXMLLoader webapp = FXMLLoader.load(getClass().getResource("FXMLLoaderDocument.fxml"));
        Stage stage = new Stage();
        stage.setScene( new Scene((Pane) webapp.load() ));
        
        stage.show();
        
    }
    
    @FXML
    public void deleteCancelPressed(ActionEvent event) throws IOException{
        FXMLLoader webapp = FXMLLoader.load(getClass().getResource("FXMLLoaderDocument.fxml"));
        Stage stage = new Stage();
        stage.setScene( new Scene((Pane) webapp.load() ));
        
        stage.show();
    }
    
    @FXML
    public void initialize(){
        
    }
}
