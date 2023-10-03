package Database;
import Context.Client;
import Context.Wallet;
import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

// TODO: gestire le migrazioni del database con Flyway

public class Database_management {
    static final String DB_URL = "jdbc:mysql://localhost/swe_court_prenotation_db";
    static final String USER = "root";
    static final String PASS = "";

    Connection conn = null;

    private Statement connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            return stmt;
        } catch (SQLException e) {
            System.err.println("Error during connection: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                //System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error during disconnection: " + e.getMessage());
        }
    }

    public void printAllReservations(int Client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select reservation.date, court, start_hour, finish_hour from reservation, time_slots where reservation.time_slot = time_slots.id and client = '" + Client + "'");

            Formatter fmt = new Formatter();
            fmt.format("%-15s%-15s%-15s%-15s%-15s\n", "ID", "DATE", "COURT", "START TIME", "END TIME");
            while (rs.next()) {
                fmt.format("%-15s%-15s%-15s%-15s%-15s\n", rs.getInt(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
            }
            System.out.println(fmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] getReservationsId(int Client) {
        Statement stmt = connect();
        assert stmt != null;
        try {
            ResultSet rs = stmt.executeQuery("select reservation.id from reservation where client = '" + Client + "'");
            int[] reservations = new int[rs.getInt(1)];
            while (rs.next()) {
                for (int i = 0; i < rs.getInt(1); i++) {
                    reservations[i] = rs.getInt(1);
                }
            }
            rs.close();
            disconnect();
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public float getReservationPrice(int reservation) {
        Statement stmt = connect();
        assert stmt != null;
        try {
            ResultSet rs = stmt.executeQuery("select prices.price from prices, reservation where reservation.id = '" + reservation + "' and prices.id = reservation.price");
            float price = rs.getFloat(1);
            rs.close();
            disconnect();
            return price;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
            resultSet.close();
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
                rs.close();
                disconnect();
                return null;
            }
            Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5),
                    rs.getInt(6), rs.getInt(7),
                    rs.getInt(8), getWallet(rs.getInt(1)));
            rs.close();
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
            Wallet wallet = new Wallet(rs.getInt(1), rs.getFloat(2));
            rs.close();
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
            stmt.executeUpdate("INSERT INTO client (name, surname, email, password, telephone_number, points, is_premium) VALUES ('" + client.getName() + "', '" + client.getSurname() + "', '" + client.getEmail() + "', '" + client.getPassword() + "', '" + client.getTelephoneNumber() + "', '" + client.getPoints() + "', '" + client.getIsPremium() + "')");
            String user = client.getEmail();
            ResultSet rs = stmt.executeQuery("select id from client where email = '" + user + "'");
            rs.next();
            int client_id = rs.getInt(1);
            stmt.executeUpdate("INSERT INTO wallet (id, balance, client) VALUES ('" + client_id + "', '" + 0 + "', '" + client_id + "')");
            stmt.executeUpdate("update client set wallet = '" + client_id + "' where id = '" + client_id + "'");
            rs.close();
            disconnect();
            return 0;
        } catch (SQLIntegrityConstraintViolationException e1) {
            disconnect();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            disconnect();
            return -2;
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

    public void modifyBalance(Client client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            stmt.executeUpdate("update wallet set balance = '" + client.getWallet().getBalance() + "' where id = '" + client.getWallet().getId() + "'");
            disconnect();
        } catch (SQLIntegrityConstraintViolationException e1) {
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Court_type_price> getCourt() {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT court.id, type_of_court.type_of_court, prices.price\n FROM court JOIN type_of_court ON court.type = type_of_court.id JOIN prices ON prices.type = type_of_court.id");
            List<Court_type_price> court_type_prices = new ArrayList<>();
            while (rs.next()) {
                court_type_prices.add(new Court_type_price(rs.getInt(1), rs.getString(2), rs.getFloat(3)));
            }
            rs.close();
            disconnect();
            return court_type_prices;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TimeSlot> getTimeSlots(Date date, int court_id) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT * FROM time_slots WHERE time_slots.id NOT IN(SELECT time_slots.id as ts FROM court JOIN reservation ON reservation.court = court.id JOIN time_slots ON time_slots.id = reservation.time_slot WHERE court.id = '" + court_id + "' AND reservation.date = '" + date + "')");
            List<TimeSlot> timeSlots = new ArrayList<>();
            while (rs.next()) {
                timeSlots.add(new TimeSlot(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            rs.close();
            disconnect();
            return timeSlots;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertReservation() {

    }

    public void deleteReservation(int reservation) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            stmt.executeUpdate("DELETE FROM reservation WHERE id = '" + reservation + "'");
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Connecting to a selected database...");
        // Open a connection
        Database_management db = new Database_management();
        db.printAllClient();
    }


    public void modifyPremium(Client client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            stmt.executeUpdate("update client set is_premium = '" + client.getIsPremium() + "' where id = '" + client.getId() + "'");
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}