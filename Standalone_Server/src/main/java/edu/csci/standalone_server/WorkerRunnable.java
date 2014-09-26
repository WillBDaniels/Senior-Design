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
 */
public class WorkerRunnable implements Runnable {

    protected HttpExchange clientSocket = null;
    private Connection con;
    private String dataToSend = "";

    public WorkerRunnable(HttpExchange clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            try (InputStream input = clientSocket.getRequestBody()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String inputLine;
                String temp;
                String line = in.readLine();
                if (line == null) {
//                    try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
//                        dataToSend = "Welcome to our Senior Design Server!";
//                        clientSocket.sendResponseHeaders(200, dataToSend.length());
//                        output.write(dataToSend.getBytes());
//                    }
                    handleSQL(in);
                }
//                while ((inputLine = line) != null && inputLine != null && !inputLine.equals("Done")) {
//                    if (inputLine.contains("SQL:")) {
//                        temp = handleSQL(in);
//                        if (temp.equals("Done")) {
//                            break;
//                        }
//                        if (con != null) {
//                            try {
//                                con.close();
//                            } catch (SQLException e) {
//                                e.printStackTrace(System.out);
//                            }
//                        }
//                        break;
//                    } else if (inputLine.contains("License4j:")) {
//                        handleLicense4j(in);
//                    } else if (inputLine.contains("Email:")) {
//                        temp = handleEmail(in);
//                        try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
//                            dataToSend += temp;
//                            dataToSend += "Done";
//                            clientSocket.sendResponseHeaders(200, dataToSend.length());
//                            output.write(dataToSend.getBytes());
//
//                            if (temp.equals("Done")) {
//                                break;
//                            }
//                        }
//                    } else {
//                        try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
//
//                            dataToSend = "Welcome to Magvar's Server! If you see "
//                                    + " this page, it means that you can reach amazon web services, "
//                                    + "which means our calculators should function normally on this network. ";
//                            clientSocket.sendResponseHeaders(200, dataToSend.length());
//                            output.write(dataToSend.getBytes());
//
//                        }
//                    }
//                    if (in.ready()) {
//                        line = in.readLine();
//                    } else {
//                        line = null;
//                    }
//                }
            }

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        clientSocket.close();
    }

    private String handleSQL(BufferedReader in) {
        String output = "";
        con = createInitialCon();
        String line;
        String dataToWrite = "";
        try {
            PreparedStatement pstmt;
            output = "";
            pstmt = con.prepareStatement("SELECT * FROM Test;");
            pstmt.execute();

            ResultSet rs = pstmt.getResultSet();
            while (rs != null && rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    switch (rsmd.getColumnType(i)) {
                        case Types.BIT: {
                            output += String.valueOf(rs.getInt(i));
                            break;
                        }
                        case Types.VARCHAR: {
                            output += rs.getString(i);
                        }
                    }
                }
                dataToWrite += (output) + "\n";
                output = "";
            }
            
            //dataToWrite += ("Done");

            try (OutputStream os = new DataOutputStream(clientSocket.getResponseBody())) {
                clientSocket.sendResponseHeaders(200, dataToWrite.length());
                os.write(dataToWrite.getBytes());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
        }
        return output;
    }

    /**
     * This method creates the initial connection to our licensefeatures DB.
     * NOTE:: we should probably re-encode the password elsewhere and read it
     * in, but it works for now.
     *
     * @return the Connection object if it was successful, null otherwise.
     */
    private Connection createInitialCon() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test_senior_design",
                    "root",
                    "Senior_Design");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

}
