package edu.csci.standalone_server;

import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * This class handles all of the interaction between itself and the various programs that might
 * query our server. Later this will be optimized to remove some of the logic out of 
 * this class and into another one for easier re-use. 
 */
public class WorkerRunnable implements Runnable {

    protected HttpExchange clientSocket = null;
    private Connection con;
    private String dataToSend = "";

    public WorkerRunnable(HttpExchange clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * This method scans and parses up the input of the GET request, and then sends the 
     * input to the appropriate handler according to what is on the first line. 
     */
    @Override
    public void run() {

        try {
            try (InputStream input = clientSocket.getRequestBody()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String inputLine;
                String temp;
                String line = in.readLine();

                if (line == null) {
                    try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
                        dataToSend = "Welcome to our Senior Design Server! If you were hoping for something more exciting.. here's a joke: \n"
                                + "Two peanuts walk into a bar, one was a salted.\n"
                                + "bwahahahaha. ";
                        clientSocket.sendResponseHeaders(200, dataToSend.length());
                        output.write(dataToSend.getBytes());
                    }
                } else if (line.contains("get_numbers")) {
                    handleGetNumbers(in);
                } else if (line.contains("get_backups")) {
                    handleGetBackups();
                } else if (line.contains("get_all_houses")) {
                    handleGetAllHouses(in);
                } else if (line.contains("get_house_specific")) {
                    handleGetHouseSpecific(in);
                } else if (line.contains("get_shift_specific")) {
                    handleGetShiftSpecific(in);
                }else if (line.contains("check_login")){
                    handleCheckLogin(in);
                }else if (line.contains("new_user")){
                    handleNewUser(in);
                }else if (line.contains("delete_user")){
                    handleDeleteUser(in);
                }else if (line.contains("new_house")){
                    handleNewHouse(in);
                }else if (line.contains("new_shift")){
                    handleNewShift(in);
                }else if (line.contains("employee_info")){
                    handleGetEmployeeInfo(in);
                }else {
                    try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
                        dataToSend += "Unknown Request, please try again";
                        dataToSend += "Done";
                        clientSocket.sendResponseHeaders(200, dataToSend.length());
                        output.write(dataToSend.getBytes());
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        clientSocket.close();
    }

    /**
     * This method handles the new shift creation
     * 
     * @param in 
     */
    private void handleNewShift(BufferedReader in){
        String output;
        String username = "", password= "";
        String line;
        try {
            boolean firstLine = true;
            String statement = "INSERT INTO Shift (`shift_id`,\n" +
                                "`shift_time`,\n" +
                                "`employee_1`,\n" +
                                "`location`) VALUES (" ;

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";
           
            while ((line = in.readLine()) != null) {
                if (firstLine){
                    statement += line;
                    firstLine = false;
                }else{
                    statement += "," + line;
                }
            }
            statement += ");";
            pstmt = con.prepareStatement(statement);
            pstmt.execute();
            dataToWrite += "Done";
             try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        }catch (IOException | SQLException e){
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured with your parameters, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    /**
     * This method handles the creation of a new house
     * 
     * @param in The buffered reader we get from the client. 
     */
    private void handleNewHouse(BufferedReader in){
        String line;
        try {
            boolean firstLine = true;
            String statement = "INSERT INTO House (`house_id`,\n" +
                                "`house_neighborhood) VALUES (" ;

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
           
            while ((line = in.readLine()) != null) {
                if (firstLine){
                    statement += line;
                    firstLine = false;
                }else{
                    statement += "," + line;
                }
            }
            statement += ");";
            pstmt = con.prepareStatement(statement);
            pstmt.execute();
            dataToWrite += "Done";
             try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        }catch (IOException | SQLException e){
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured with your parameters, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
        
    }
    
    /**
     * This method is for deleting a specific user
     * 
     * @param in 
     */
    private void handleDeleteUser(BufferedReader in){
        String output;
        String line;
        try {
            boolean firstLine = true;
            String statement = "DELETE FROM Employee";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";
            int countParams = 0;
            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    statement += " WHERE ";
                    firstLine = false;
                } else {
                    statement += " AND ";
                }
                countParams ++;
                statement += line;
            }
            if (countParams > 2){
                output += "I'm sorry, not enough parameters to delete user, please"
                        + "make sure to use at least 2 types of parameters, ex: username/password.";
                dataToWrite += output;
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    clientSocket.sendResponseHeaders(200, dataToWrite.length());
                    os.write(dataToWrite.getBytes());
                }
                return;
            }
            statement += ";";

            pstmt = con.prepareStatement(statement);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getResultSet();
            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured with your parameters, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    /**
     * This method creates a branch new user. It assumes it will get data in exactly the 
     * correct order, and with all values present 
     * 
     * @param in 
     */
    private void handleNewUser(BufferedReader in){
        String output;
        String username = "", password= "";
        String line;
        try {
            boolean firstLine = true;
            String statement = "INSERT INTO Employee (`employee_id`,\n" +
                                "`employee_name`,\n" +
                                "`is_manager`,\n" +
                                "`username`,\n" +
                                "`password`,\n" +
                                "`phone_number`) VALUES (" ;

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";
           
            while ((line = in.readLine()) != null) {
                if (firstLine){
                    statement += line;
                    firstLine = false;
                }else{
                    statement += "," + line;
                }
            }
            statement += ");";
            System.out.println("This is the statement in handleCheckLogin: " + statement);
            pstmt = con.prepareStatement(statement);
            pstmt.execute();
            dataToWrite += "Done";
             try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        }catch (IOException | SQLException e){
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                        String error = "I'm sorry, an error occured with your parameters, please try again.";
                        clientSocket.sendResponseHeaders(200, error.length());
                        os.write(error.getBytes());
                    }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
        
        
    }
    
    /**
     * This method checks the login credentials by way of username and password. 
     * 
     * @param in 
     */
    private void handleCheckLogin(BufferedReader in){
        String output;
        String username = "", password= "";
        String line;
        try {
            boolean firstLine = true;
            String statement = "SELECT username, password FROM Employee";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";
            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    statement += " WHERE ";
                    firstLine = false;
                    username = line.substring(line.indexOf("=")+2, line.lastIndexOf("\""));
                } else {
                    statement += " AND ";
                }
                statement += line;
            }
            statement += ";";
            System.out.println("This is the statement in handleCheckLogin: " + statement);
            pstmt = con.prepareStatement(statement);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            if (rs != null && rs.next()) {
                dataToWrite += "Verification_Success|";
            }else{
                statement = "SELECT username, password FROM Employee WHERE username=\"" + username + "\";";
                pstmt = con.prepareCall(statement);
                pstmt.execute();
                rs = pstmt.getResultSet();
                if (rs != null && rs.next()){
                    if (rs.getString("password").equals("null") || rs.getString("password").isEmpty()){
                        statement = "UPDATE Employee SET password=\"" + password + "\";";
                        pstmt = con.prepareStatement(statement);
                        pstmt.executeUpdate();
                        dataToWrite += "Verification_Success|";
                    }else{
                        dataToWrite += "Invalid_Password|";
                    }
                }else{
                    dataToWrite += "Invalid_Username|";
                }
            }
            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    
    /**
     * Get a specific Shift
     * query = get_shift_specific\nshift_time="{shift_time}"\nhouse_neighborhood="{neighborhood}"
     * This query will return all of the details available for the specified shift. 
     * All information must be present in the query, otherwise 'insufficient parameters' will
     * be given out (unable to recognize unique shift).
     * This will return data in the form:  "{column_name}"="{column_value}"&...&|repeat|Done
     * 
     * @param in The BufferedReader we are using from the client socket. 
     */
    private void handleGetShiftSpecific(BufferedReader in){
        String output;
        String line;
        try {
            boolean firstLine = true;
            String statement = "SELECT * FROM Shift";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";

            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    statement += " WHERE ";
                    firstLine = false;
                } else {
                    statement += " OR ";
                }
                statement += line;
            }
            statement += ";";
            System.out.println("This is the statement in get_shift_specific: " + statement);

            pstmt = con.prepareStatement(statement);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
             while (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    switch (rsmd.getColumnType(i)) {
                        case Types.BIT: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i)) + "\"&";
                            break;
                        }
                        case Types.VARCHAR: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + rs.getString(i) + "\"&";
                            break;
                        }
                        case Types.INTEGER: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i) + "\"&");
                            break;
                        }
                        default: 
                            break;
                    }
                }
                dataToWrite += (output) + "|";
                output = "";
            }

            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    /**
     * Get a list of current houses: 
     *	query = "get_all_houses"
     *	This will return the entire list of houses as such: "{column_name}"="{column_value}"&...&|repeat|Done
     * 
     * @param in The BufferedReader being used.  
     */
    private void handleGetAllHouses(BufferedReader in){
        String output;
        String line;
        try {
            boolean firstLine = true;
            String statement = "SELECT * FROM House;";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";
            pstmt = con.prepareStatement(statement);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            while (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    switch (rsmd.getColumnType(i)) {
                        case Types.BIT: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i)) + "\"&";
                            break;
                        }
                        case Types.VARCHAR: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + rs.getString(i) + "\"&";
                            break;
                        }
                        case Types.INTEGER: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i) + "\"&");
                            break;
                        }
                        default: 
                            break;
                    }
                }
                dataToWrite += (output) + "|";
                output = "";
            }

            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    /**
     * Get a list of backup employees on a given shift. 
     * query = get_backups\nshift_time="{some shift time}"
     * This query will check the time, and then from the time, there will
     * be an associated 'backup-id' which can then be used to iterate through 
     * the 'backup' table, and obtain a list of employees associated with that backup.
     * Will return data in the form: "{name}"="{employee_id}"|repeat|Done.
     * 
     */
    private void handleGetBackups(){
        String output;
        try {
            String statement = "SELECT employee_id FROM Employee WHERE is_backup=1;";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";

            pstmt = con.prepareStatement(statement);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            while (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                output += "\"employee_id\"=\'" + rs.getString("employee_id") + "\"&|";
                dataToWrite += (output) + "\n";
            }
            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    /**
     * Get a specific house. This will return the data of the first house that meets the parameters
     * query = get_house_specific\nhouse_neightborhood="{house_neighborhood}"\nhouse_id="{house_id}"
     * This will return data of the form: 
     * shift_1="{shift_time}"&employee_name="{employee name}"|shift_2="{shift_time}&employee_name="{employee_name}"|repeat|Done
     * 
     * @param in The BufferedReader we are using from the clientSocket. 
     */
    private void handleGetHouseSpecific(BufferedReader in){
        String output;
        String line;
        try {
            boolean firstLine = true;
            String statement = "SELECT * FROM House";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";

            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    statement += " WHERE ";
                    firstLine = false;
                } else {
                    statement += " OR ";
                }
                statement += line;
            }
            statement += ";";
            System.out.println("This is the statement in get_house_specific: " + statement);
            pstmt = con.prepareStatement(statement);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            while (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    switch (rsmd.getColumnType(i)) {
                        case Types.BIT: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i)) + "\"&";
                            break;
                        }
                        case Types.VARCHAR: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + rs.getString(i) + "\"&";
                            break;
                        }
                        case Types.INTEGER: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i) + "\"&");
                            break;
                        }
                        default: 
                            break;
                    }
                }
                dataToWrite += (output) + "|";
                output = "";
            }

            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    
    /**
     * Get a list of phone numbers: get_numbers\n employee_name="{name}" \n etc. 
     *	If used with no extra parameters (no employee names, etc), simply gets all phone numbers
     *	returns data in the form: "{name}"="{phone number}"|repeat|Done
     * 
     * @param in The Buffered Reader returned by the user. 
     */
    private void handleGetNumbers(BufferedReader in) {
        String output;
        String line;
        try {
            boolean firstLine = true;
            String statement = "SELECT employee_id, phone_number FROM Employee";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";

            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    statement += " WHERE ";
                    firstLine = false;
                } else {
                    statement += " OR ";
                }
                statement += line;
            }
            statement += ";";
            System.out.println("This is the statement in get_numbers: " + statement);

            pstmt = con.prepareStatement(statement);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            while (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                output += '"' + rs.getString("employee_name") + "\"=\"" + rs.getString("phone_number") + "\"&|";
                dataToWrite += (output) + "\n";
                output = "";
            }

            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }


    
    private void handleGetEmployeeInfo(BufferedReader in){
        String output;
        String line;
        try {
            boolean firstLine = true;
            String statement = "SELECT * FROM Employee";

            con = createInitialCon();
            if (con == null) {
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
                return;
            }
            String dataToWrite = "";

            PreparedStatement pstmt;
            output = "";

            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    statement += " WHERE ";
                    firstLine = false;
                } else {
                    statement += " OR ";
                }
                statement += line;
            }
            statement += ";";
            System.out.println("This is the statement in get_numbers: " + statement);

            pstmt = con.prepareStatement(statement);
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            while (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
               for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    switch (rsmd.getColumnType(i)) {
                        case Types.BIT: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i)) + "\"&";
                            break;
                        }
                        case Types.VARCHAR: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + rs.getString(i) + "\"&";
                            break;
                        }
                        case Types.INTEGER: {
                            output += "\"" + rsmd.getColumnName(i) + "\"=\"" + String.valueOf(rs.getInt(i) + "\"&");
                            break;
                        }
                        default: 
                            break;
                    }
                }
                dataToWrite += (output + "|");
                output = "";
            }

            dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
            try{
                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                    String error = "I'm sorry, an error occured, please try again.";
                    clientSocket.sendResponseHeaders(200, error.length());
                    os.write(error.getBytes());
                }
            }catch(IOException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    
    /**
     * This method creates the initial connection to our Employee DB.
     *
     * @return the Connection object if it was successful, null otherwise.
     */
    private Connection createInitialCon() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/shifty_db",
                    "root",
                    "Senior_Design");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

}
