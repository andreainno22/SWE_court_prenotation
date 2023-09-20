package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database_management {

    static final String DB_URL = "jdbc:mysql://localhost/swe_court_prenotation_db";
    static final String USER = "root";
    static final String PASS = "Ginoqwerty1234";

    public static void main(String[] args) {
        System.out.println("Connecting to a selected database...");
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();) {
            System.out.println("Connected database successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}