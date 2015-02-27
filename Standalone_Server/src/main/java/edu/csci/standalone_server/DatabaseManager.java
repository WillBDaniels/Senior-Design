package edu.csci.standalone_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author William
 */
public class DatabaseManager {

    private final Connection con;

    /**
     * This is the public default constructor, used for initializing our 'con'
     * variable so that it will never be null.
     */
    public DatabaseManager() {
        con = createInitialCon();
    }

    /**
     * This method closes the connection to the Con variable, potentially
     * throwing an SQLException if the connection has already been closed.
     *
     */
    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * THis returns the Connection object so that SQL queries can be made.
     *
     * @return the Connection object Con.
     */
    public Connection getConnection() {
        return con;
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
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/shifty_db",
                    "root",
                    "Senior_Design");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
