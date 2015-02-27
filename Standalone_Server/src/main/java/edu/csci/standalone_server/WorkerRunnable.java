package edu.csci.standalone_server;

import com.sun.net.httpserver.HttpExchange;
import edu.csci.standalone_server.enums.Data;
import edu.csci.standalone_server.enums.State;
import edu.csci.standalone_server.jsonhandler.JSONHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * This class handles all of the interaction between itself and the various
 * programs that might query our server. Later this will be optimized to remove
 * some of the logic out of this class and into another one for easier re-use.
 */
public class WorkerRunnable implements Runnable {

    protected HttpExchange clientSocket = null;
    private String dataToSend = "";

    public WorkerRunnable(HttpExchange clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     *
     * This is the standard 'run()' method of the Runnable interface, and the
     * primary event loop for this thread. Once this method finished, the client
     * connection is dismissed.
     */
    @Override
    public void run() {
        boolean foundMatch = false;
        Map<String, Object> params = (Map<String, Object>) clientSocket.getAttribute("parameters");
        if (params == null) {
            showUnknownMessage();
            clientSocket.close();
            return;
        }
        if ((String) params.get("secretkey") != null && ((String) params.get("secretkey")).equals(Data.CONNECTIONSECRET.getData())) {
            String json = (String) params.get("data");
            for (State item : State.values()) {
                if (item.getCheckString().equals(params.get("action"))) {
                    foundMatch = true;
                    try {
                        JSONHandler handler = item.getHandler().getDeclaredConstructor(String.class, HttpExchange.class).newInstance(json, clientSocket);
                        sendJSONResponse(handler.getJSONResponse());
                    } catch (NoSuchMethodException | SecurityException ex) {
                        ex.printStackTrace(System.err);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            }
            if (!foundMatch) {
                showUnknownMessage();
            }

        } else {
            showUnknownMessage();
        }
        clientSocket.close();
    }

    /**
     * This method simply sends a basic JSON response which is encrypted. Only
     * difference is that this method is always expecting to send json
     *
     * @param json the (unencrypted) JSON string to be sent back to the client.
     */
    private void sendJSONResponse(String json) {
        // MVCipher dc = new MVCipher();
        try {
            try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
                //json = dc.encrypt(json);
                clientSocket.sendResponseHeaders(200, json.length());
                output.write(json.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * This is the message that gets sent to the client if they send something
     * that we don't recognize. This one is pretty self-explanatory.
     *
     */
    private void showUnknownMessage() {
        try {
            try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
                dataToSend = "Welcome to Team Shifty's Senior Design Server! This web end-point isn't really "
                        + "any use to browsing, so all there is is this message, sorry!";
                clientSocket.sendResponseHeaders(200, dataToSend.length());
                output.write(dataToSend.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

//
//    /**
//     * This method scans and parses up the input of the GET request, and then sends the
//     * input to the appropriate handler according to what is on the first line.
//     */
//    @Override
//    public void run() {
//
//        try {
//            try (InputStream input = clientSocket.getRequestBody()) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(input));
//                String inputLine;
//                String temp;
//                String line = in.readLine();
//
//                if (line == null) {
//                    try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
//                        dataToSend = "Welcome to our Senior Design Server! If you were hoping for something more exciting.. here's a joke: \n"
//                                + "Two peanuts walk into a bar, one was a salted.\n"
//                                + "bwahahahaha. ";
//                        clientSocket.sendResponseHeaders(200, dataToSend.length());
//                        output.write(dataToSend.getBytes());
//                    }
//                } else if (line.contains("get_numbers")) {
//                    handleGetNumbers(in);
//                } else if (line.contains("get_backups")) {
//                    handleGetBackups();
//                } else if (line.contains("get_all_houses")) {
//                    handleGetAllHouses(in);
//                } else if (line.contains("get_house_specific")) {
//                    handleGetHouseSpecific(in);
//                } else if (line.contains("get_shift_specific")) {
//                    handleGetShiftSpecific(in);
//                }else if (line.contains("check_login")){
//                    handleCheckLogin(in);
//                }else if (line.contains("new_user")){
//                    handleNewUser(in);
//                }else if (line.contains("delete_user")){
//                    handleDeleteUser(in);
//                }else if (line.contains("new_house")){
//                    handleNewHouse(in);
//                }else if (line.contains("new_shift")){
//                    handleNewShift(in);
//                }else {
//                    try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
//                        dataToSend += "Unknown Request, please try again";
//                        dataToSend += "Done";
//                        clientSocket.sendResponseHeaders(200, dataToSend.length());
//                        output.write(dataToSend.getBytes());
//                    }
//                }
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace(System.err);
//        }
//        clientSocket.close();
//    }
//
//    private void handleNewShift(BufferedReader in){
//        String output;
//        String username = "", password= "";
//        String line;
//        try {
//            boolean firstLine = true;
//            String statement = "INSERT INTO Shift (`shift_id`,\n" +
//                                "`shift_time`,\n" +
//                                "`employee_1`,\n" +
//                                "`location`) VALUES (" ;
//
//            con = createInitialCon();
//            if (con == null) {
//                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                    String error = "I'm sorry, an error occured, please try again.";
//                    clientSocket.sendResponseHeaders(200, error.length());
//                    os.write(error.getBytes());
//                }
//                return;
//            }
//            String dataToWrite = "";
//
//            PreparedStatement pstmt;
//            output = "";
//
//            while ((line = in.readLine()) != null) {
//                if (firstLine){
//                    statement += line;
//                    firstLine = false;
//                }else{
//                    statement += "," + line;
//                }
//            }
//            statement += ");";
//            System.out.println("This is the statement in handleCheckLogin: " + statement);
//            pstmt = con.prepareStatement(statement);
//            pstmt.execute();
//            dataToWrite += "Done";
//             try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                clientSocket.sendResponseHeaders(200, dataToWrite.length());
//                os.write(dataToWrite.getBytes());
//            }
//        }catch (IOException | SQLException e){
//            e.printStackTrace(System.err);
//        }
//    }
//
//    private void handleNewHouse(BufferedReader in){
//        String line;
//        try {
//            boolean firstLine = true;
//            String statement = "INSERT INTO House (`house_id`,\n" +
//                                "`house_neighborhood) VALUES (" ;
//
//            con = createInitialCon();
//            if (con == null) {
//                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                    String error = "I'm sorry, an error occured, please try again.";
//                    clientSocket.sendResponseHeaders(200, error.length());
//                    os.write(error.getBytes());
//                }
//                return;
//            }
//            String dataToWrite = "";
//
//            PreparedStatement pstmt;
//
//            while ((line = in.readLine()) != null) {
//                if (firstLine){
//                    statement += line;
//                    firstLine = false;
//                }else{
//                    statement += "," + line;
//                }
//            }
//            statement += ");";
//            System.out.println("This is the statement in handleNewHouse: " + statement);
//            pstmt = con.prepareStatement(statement);
//            pstmt.execute();
//            dataToWrite += "Done";
//             try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                clientSocket.sendResponseHeaders(200, dataToWrite.length());
//                os.write(dataToWrite.getBytes());
//            }
//        }catch (IOException | SQLException e){
//            e.printStackTrace(System.err);
//        }
//
//    }
//
//    /**
//     * This method is for deleting a specific user
//     *
//     * @param in
//     */
//    private void handleDeleteUser(BufferedReader in){
//        String output;
//        String line;
//        try {
//            boolean firstLine = true;
//            String statement = "DELETE FROM Employee";
//
//            con = createInitialCon();
//            if (con == null) {
//                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                    String error = "I'm sorry, an error occured, please try again.";
//                    clientSocket.sendResponseHeaders(200, error.length());
//                    os.write(error.getBytes());
//                }
//                return;
//            }
//            String dataToWrite = "";
//
//            PreparedStatement pstmt;
//            output = "";
//            int countParams = 0;
//            while ((line = in.readLine()) != null) {
//                if (firstLine) {
//                    statement += " WHERE ";
//                    firstLine = false;
//                } else {
//                    statement += " AND ";
//                }
//                countParams ++;
//                statement += line;
//            }
//            if (countParams > 2){
//                output += "I'm sorry, not enough parameters to delete user, please"
//                        + "make sure to use at least 2 types of parameters, ex: username/password.";
//                dataToWrite += output;
//                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                    clientSocket.sendResponseHeaders(200, dataToWrite.length());
//                    os.write(dataToWrite.getBytes());
//                }
//                return;
//            }
//            statement += ";";
//
//            pstmt = con.prepareStatement(statement);
//            pstmt.executeUpdate();
//
//            ResultSet rs = pstmt.getResultSet();
//            dataToWrite += ("Done");
//
//            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                clientSocket.sendResponseHeaders(200, dataToWrite.length());
//                os.write(dataToWrite.getBytes());
//            }
//        } catch (SQLException | IOException e) {
//            e.printStackTrace(System.err);
//            try{
//                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                    String error = "I'm sorry, an error occured, please try again.";
//                    clientSocket.sendResponseHeaders(200, error.length());
//                    os.write(error.getBytes());
//                }
//            }catch(IOException ex){
//                ex.printStackTrace(System.err);
//            }
//        }
//    }
//
//    private void handleNewUser(BufferedReader in){
//        String output;
//        String username = "", password= "";
//        String line;
//        try {
//            boolean firstLine = true;
//            String statement = "INSERT INTO Employee (`employee_id`,\n" +
//                                "`employee_name`,\n" +
//                                "`is_manager`,\n" +
//                                "`username`,\n" +
//                                "`password`,\n" +
//                                "`phone_number`) VALUES (" ;
//
//            con = createInitialCon();
//            if (con == null) {
//                try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                    String error = "I'm sorry, an error occured, please try again.";
//                    clientSocket.sendResponseHeaders(200, error.length());
//                    os.write(error.getBytes());
//                }
//                return;
//            }
//            String dataToWrite = "";
//
//            PreparedStatement pstmt;
//            output = "";
//
//            while ((line = in.readLine()) != null) {
//                if (firstLine){
//                    statement += line;
//                    firstLine = false;
//                }else{
//                    statement += "," + line;
//                }
//            }
//            statement += ");";
//            System.out.println("This is the statement in handleCheckLogin: " + statement);
//            pstmt = con.prepareStatement(statement);
//            pstmt.execute();
//            dataToWrite += "Done";
//             try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
//                clientSocket.sendResponseHeaders(200, dataToWrite.length());
//                os.write(dataToWrite.getBytes());
//            }
//        }catch (IOException | SQLException e){
//            e.printStackTrace(System.err);
//        }
//
//
//    }
//
}
