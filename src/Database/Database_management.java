package Database;

import Context.Client;
import Context.Wallet;

import javax.management.Query;
import java.sql.*;

public class Database_management {

    static final String DB_URL = "jdbc:mysql://localhost/swe_court_prenotation_db";
    static final String USER = "root";
    static final String PASS = "Ginoqwerty1234";

    Connection conn = null;

    private Statement connect() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            return stmt;
        } catch (SQLException e) {
            System.err.println("Error during connection: " + e.getMessage());
        }
        return null;
    }

    private void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error during disconnection: " + e.getMessage());
        }
    }

    public void printAllClient() {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM client");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(", ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " [" + rsmd.getColumnName(i) + "]");
                }
                System.out.println("");
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Client getClient(String email, String password) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select * from client where email = '" + email + "' and password = '" + password + "'");
            if (!rs.next()) {
                System.err.println("Wrong email or password");
                disconnect();
                return null;
            }
            //rs.next();
            Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5),
                    rs.getInt(6), rs.getInt(7),
                    rs.getInt(8), getWallet(rs.getInt(1)));
            disconnect();
            return client;
        } catch (SQLException e) {
            e.printStackTrace();
            disconnect();
        }
        return null;
    }

    Wallet getWallet(int id) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select * from wallet where client = '" + id + "'");
            rs.next();
            Wallet wallet = new Wallet(rs.getInt(1), rs.getInt(2));
            disconnect();
            return wallet;
        } catch (SQLException e) {
            e.printStackTrace();
            disconnect();
        }
        return null;
    }

    public int insertClient(Client client) {
        Statement stmt = connect();
        assert stmt != null;
        try {
            stmt.executeUpdate("INSERT INTO client (name, surname, email, password, telephone_number, points, is_premium) VALUES ('" + client.getName() + "', '" + client.getSurname() + "', '" + client.getEmail() + "', '" + client.getPassword() + "', '" + client.getTelephoneNumber() + "', '" + client.getPoints() + "', '" + client.getisPremium() + "')");
            String user = client.getEmail();
            ResultSet rs = stmt.executeQuery("select id from client where email = '" + user + "'");
            rs.next();
            int client_id = rs.getInt(1);
            stmt.executeUpdate("INSERT INTO wallet (id, balance, client) VALUES ('" + client_id + "', '" + client.getWallet().getBalance() + "', '" + client_id + "')");
            stmt.executeUpdate("update client set wallet = '" + client_id + "' where id = '" + client_id + "'");
            disconnect();
            return 0;
        } catch (SQLIntegrityConstraintViolationException e1) {
            System.err.println("Email already used");
            disconnect();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(Client client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            stmt.executeUpdate("DELETE FROM client WHERE id = '" + client.getId() + "'");
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWallet(Client client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            stmt.executeUpdate("INSERT INTO wallet (id, balance, client_id) VALUES ('" + client.getWallet().getBalance() + "', '" + client.getId() + "', '" + client.getWallet().getId() + "')");
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TimeSlot[] getTimeSlots(Date date) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            String query1 = "WITH booked as (SELECT court, date, time_slot FROM reservation WHERE court = 1 )";
            String query2 = "SELECT all_things.court, start_hour, finish_hour, all_things.ts FROM (SELECT court.id as court, start_hour, finish_hour, booked.time_slot, time_slots.id as ts";
            String query3 = "FROM (court CROSS JOIN time_slots) LEFT JOIN booked ON booked.court = court.id and time_slots.id = booked.time_slot";
            String query4 = "WHERE court.id = 1) as all_things WHERE all_things.time_slot IS NULL";
            ResultSet rs = stmt.executeQuery( query1 + query2 + query3 + query4);
            rs.next();

            TimeSlot[] timeSlots = new TimeSlot[rs.getFetchSize()];
            disconnect();
            return timeSlots;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertReservation() {

    }

    public static void main(String[] args) {
        System.out.println("Connecting to a selected database...");
        // Open a connection
        Database_management db = new Database_management();
        db.printAllClient();
    }


}