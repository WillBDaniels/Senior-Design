/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author fritz
 */
public class WebAppTest extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LogInScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static String sendData(List<String> data) {
        PrintWriter osw;
        String returnOut = "";
        String output;
        BufferedReader in;
        HttpURLConnection serverSocket = null;
        try {
           
            URL obj = new URL("http://54.68.136.126:443");
            serverSocket = (HttpURLConnection) obj.openConnection();
            
            serverSocket.setDoOutput(true);
            serverSocket.setDoInput(true);

            osw = new PrintWriter(serverSocket.getOutputStream(), true);
            for (String line : data) {
                osw.println(line);
            }
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            while ((output = in.readLine()) != null && (!output.equals("Done"))) {
                returnOut += output;
            }
            
            osw.close();
        } catch (IOException ex) {

            ex.printStackTrace(System.err);
        } finally {
            if (serverSocket != null) {
                serverSocket.disconnect();
            }
        }
        return returnOut;
    }
    
    
}
