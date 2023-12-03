package DAOs;

import java.sql.*;


 class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://40s.h.filess.io:3307/swecourtprentiondb_recordfell";
    private static final String USER = "swecourtprentiondb_recordfell";
    private static final String PASS = "f47c79a3b3652a4dcaae3fcd5a2bd813b9fb4a5e";
    private static DatabaseLogging logging = null;

    protected void dbError(Exception e) {
        if (logging == null) logging = new DatabaseLogging();
        System.err.println("Database responded with an error. See log file for more information.");
        logging.logger.severe("Exception: " + e);
    }

    Connection conn = null;

    protected Statement connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn.createStatement();
        } catch (SQLException e) {
            dbError(e);
            System.err.println("Error during connection.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    protected Statement newStatement() {
        try {
            if (conn != null) return conn.createStatement();
        } catch (SQLException e) {
            dbError(e);
        }
        return null;
    }

    protected Statement connectTransaction() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return conn.createStatement();
        } catch (SQLException e) {
            dbError(e);
            System.err.println("Error during connection.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    protected boolean commitTransaction() throws SQLException {
        try {
            conn.commit();
            return true;
        } catch (SQLException e) {
            dbError(e);
            conn.rollback();
            return false;
        }
    }

    protected void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            dbError(e);
            System.err.println("Error during disconnection.");
        }
    }
}