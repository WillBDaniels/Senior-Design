/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapptest;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;
import static webapptest.WebAppTest.sendData;

/**
 *
 * @author fritz
 */
public class LogInScreenController {
    
    @FXML private TextField loginName;
    @FXML private TextField loginPassword;
    
    @FXML
    public void initialize(){
        
    }
    
    //Runs the login process
    @FXML
    public void logInPressed(ActionEvent event) throws IOException{
        int response = passwordVerification();
        
        if(response == 1)
        {
            Stage stageTheLabelBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
            Parent webapp = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
            Scene scene = new Scene(webapp);
        
            stageTheLabelBelongs.setScene(scene);
            stageTheLabelBelongs.show();
        }
    }
    
    //Verifies if the password and username were correct
    public int passwordVerification(){
        String password ="";
        List<String> temp = new ArrayList<>();
        temp.add("check_login");
        temp.add("username=\"" + loginName.getText() + "\"");
        
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            password = (Hex.encodeHexString(md.digest(loginPassword.getText().getBytes())));
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace(System.err);
        }
        temp.add("password=\"" + password + "\"");
        String response = sendData(temp);
        System.out.println("Results of send data: " + response);
        
        switch(response) {
            case "Verification_Success|Done":
                return 1;
            case "Invalid_Password|Done":
                return 2;
            case "Invalid_Username|Done":
                return 3;
            default:
                System.out.println(response);
                return 4;
        }
    }
    
}
