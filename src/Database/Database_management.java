package Database;

import Context.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Database_management {
    static final String DB_URL = "jdbc:mysql://40s.h.filess.io:3307/swecourtprentiondb_recordfell";
    static final String USER = "swecourtprentiondb_recordfell";
    static final String PASS = "f47c79a3b3652a4dcaae3fcd5a2bd813b9fb4a5e";
    static Logging logging = null;

    private static class Logging {

        private final Logger logger = Logger.getLogger(Database_management.class.getName());

        private FileHandler fh = null;
        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        private String FILENAME = System.getProperty("user.dir") + "/logs/DatabaseLogFile_" + format.format(Calendar.getInstance().getTime()) + ".log";

        private File file = new File(FILENAME);

        public Logging() {
            //just to make our log file nicer :)
            try {
                file.createNewFile();
                fh = new FileHandler(FILENAME);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        }
    }

    private void dbError(Exception e) {
        if (logging == null) logging = new Logging();
        System.err.println("Database responded with an error. See log file for more information.");
        logging.logger.severe("Exception: " + e);
    }

    Connection conn = null;

    private Statement connect() {
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

    private Statement newStatement() {
        try {
            if (conn != null) return conn.createStatement();
        } catch (SQLException e) {
            dbError(e);
        }
        return null;
    }

    private Statement connectTransaction() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            return conn.createStatement();
        } catch (SQLException e) {
            dbError(e);
            System.err.println("Error during connection.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private boolean commitTransaction() throws SQLException {
        try {
            conn.commit();
            return true;
        } catch (SQLException e) {
            dbError(e);
            conn.rollback();
            return false;
        }
    }

    private void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            dbError(e);
            System.err.println("Error during disconnection.");
        }
    }

    public void deleteTestClient(String email) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            stmt.executeUpdate("DELETE FROM client WHERE email = '" + email + "'");
        } catch (SQLException e) {
            System.err.println("Error deleting user from database.");
            System.err.println("ERROR: " + e);
        } finally {
            disconnect();
        }
    }


    public void printAllReservations(int Client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select reservation.id, reservation.date, court, start_hour, finish_hour, reservation.price from reservation, time_slots where reservation.time_slot = time_slots.id and client = '" + Client + "'");

            Formatter fmt = new Formatter();
            fmt.format("%-15s%-15s%-15s%-15s%-15s%-15s\n", "ID", "DATE", "COURT", "START TIME", "END TIME", "PRICE [€]");
            while (rs.next()) {
                fmt.format("%-15s%-15s%-15s%-15s%-15s%-15s\n", rs.getInt(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getFloat(6));
            }
            System.out.println(fmt);
            rs.close();
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
    }

    public void printAllFutureReservations(int Client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            Date today = new Date(Calendar.getInstance().getTimeInMillis());
            ResultSet rs = stmt.executeQuery("select reservation.id, reservation.date, court, start_hour, finish_hour, reservation.price from reservation, time_slots where date > '" + today + "' and reservation.time_slot = time_slots.id and client = '" + Client + "'");
            Formatter fmt = new Formatter();
            fmt.format("%-15s%-15s%-15s%-15s%-15s%-15s\n", "ID", "DATE", "COURT", "START TIME", "END TIME", "PRICE [€]");
            while (rs.next()) {
                fmt.format("%-15s%-15s%-15s%-15s%-15s%-15s\n", rs.getInt(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getFloat(6));
            }
            System.out.println(fmt);
            rs.close();
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
    }

    public Reservation getReservationById(int id) {
        try {
            Statement stmt1 = connect();
            assert stmt1 != null;
            Statement stmt2 = newStatement();
            assert stmt2 != null;
            ResultSet rs = stmt1.executeQuery("select * from reservation where id = '" + id + "'");
            ResultSet timeSlot = stmt2.executeQuery("select * from time_slots where id in (select time_slot from reservation where id = '" + id + "')");
            rs.next();
            timeSlot.next();
            Reservation reservation = new Reservation(rs.getInt(1), rs.getDate(2), new TimeSlot(timeSlot.getInt(1), timeSlot.getString(2), timeSlot.getString(3)), rs.getFloat(6), rs.getInt(7));
            reservation.setCourt(new Court(rs.getInt(3)));
            rs.close();
            timeSlot.close();
            return reservation;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }

    public ArrayList<Integer> getReservationsId(int Client) {
        Statement stmt = connect();
        assert stmt != null;
        try {
            ResultSet rs = stmt.executeQuery("select reservation.id from reservation where client = '" + Client + "'");
            ArrayList<Integer> reservations = new ArrayList<>();
            while (rs.next()) {
                reservations.add(rs.getInt(1));
            }
            rs.close();
            return reservations;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }


    public RentingKit getRentingKit(String type) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM renting_kits WHERE type = '" + type + "'");
            resultSet.next();
            RentingKit rentingKit = new RentingKit(resultSet.getInt(1), resultSet.getString(2), resultSet.getFloat(3));
            resultSet.close();
            return rentingKit;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }

    public Client getClient(String email, String password) {
        try {
            Statement stmt = connect();
            //assert stmt != null;
            if (stmt == null) {
                return null;
            }
            ResultSet rs = stmt.executeQuery("select * from client where email = '" + email + "' and password = '" + password + "'");
            if (!rs.next()) {
                System.err.println("Wrong email or password. Retry.");
                rs.close();
                disconnect();
                return null;
            }
            Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), getWallet(rs.getInt(1), stmt));
            rs.close();
            return client;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }

    public boolean checkTestReservation(Client client, Date date) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select * from reservation where client = '" + client.getId() + "' and date = '" + date + "'");
            if (!rs.next()) {
                rs.close();
                disconnect();
                return false;
            }
            rs.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Reservation not found or other generic SQLException.");
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            disconnect();
        }
        return false;
    }

    Wallet getWallet(int id, Statement stmt) {
        try {
            ResultSet rs = stmt.executeQuery("select * from wallet where client = '" + id + "'");
            rs.next();
            Wallet wallet = new Wallet(rs.getInt(1), rs.getFloat(2));
            rs.close();
            return wallet;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }

    public Date getPremiumExpiration(Client client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select end_date from premium_subs where client = '" + client.getId() + "'");
            rs.next();
            Date date = rs.getDate(1);
            rs.close();
            return date;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }

    public int insertClient(Client client) {
        Statement stmt = connectTransaction();
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
            commitTransaction();
            return 0;
        } catch (SQLIntegrityConstraintViolationException e1) {
            return -1;
        } catch (SQLException e) {
            dbError(e);
            return -2;
        } finally {
            disconnect();
        }
    }

    public boolean modifyBalance(Client client, Statement transactionStmt) {
        try {
            if (transactionStmt == null) {
                Statement stmt = connect();
                assert stmt != null;
                stmt.executeUpdate("update wallet set balance = '" + client.getWallet().getBalance() + "' where id = '" + client.getWallet().getId() + "'");
                disconnect();
            } else {
                transactionStmt.executeUpdate("update wallet set balance = '" + client.getWallet().getBalance() + "' where id = '" + client.getWallet().getId() + "'");
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e1) {
            dbError(e1);
            disconnect();
            return false;
        } catch (SQLException e) {
            dbError(e);
            disconnect();
            //e.printStackTrace();
            return false;
        }
    }

    public List<Court> getCourt() {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT court.id, type_of_court.type_of_court, prices.price FROM court JOIN type_of_court ON court.type = type_of_court.id JOIN prices ON prices.type = type_of_court.id");
            List<Court> court_type_prices = new ArrayList<>();
            while (rs.next()) {
                court_type_prices.add(new Court(rs.getInt(1), rs.getFloat(3)));
                if (rs.getString(2).equals("padel")) {
                    court_type_prices.get(court_type_prices.size() - 1).setType("padel");
                    court_type_prices.get(court_type_prices.size() - 1).setTerrain_type("null");
                } else {
                    court_type_prices.get(court_type_prices.size() - 1).setTerrain_type(rs.getString(2));
                    court_type_prices.get(court_type_prices.size() - 1).setType("tennis");
                }
            }

            rs.close();
            return court_type_prices;
        } catch (SQLException e) {
            dbError(e);
            return null;
        } finally {
            disconnect();
        }
    }

    public List<TimeSlot> getTimeSlots(Date date, int court_id) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT * FROM time_slots WHERE time_slots.id NOT IN(SELECT time_slots.id as ts FROM court JOIN reservation ON reservation.court = court.id JOIN time_slots ON time_slots.id = reservation.time_slot WHERE court.id = '" + court_id + "' AND reservation.date = '" + date + "')");
            List<TimeSlot> timeSlots = new ArrayList<>();
            int id = 1;
            while (rs.next()) {
                if (rs.getInt(1) == id) {
                    timeSlots.add(new TimeSlot(rs.getInt(1), rs.getString(2), rs.getString(3)));
                    id++;
                } else {
                    do {
                        timeSlots.add(null);
                        id++;
                    } while (rs.getInt(1) != id);
                    timeSlots.add(new TimeSlot(rs.getInt(1), rs.getString(2), rs.getString(3)));
                    id++;
                }
            }
            rs.close();
            return timeSlots;
        } catch (SQLException e) {
            dbError(e);
            return null;
        } finally {
            disconnect();
        }
    }

    public void updatePoints(int points, Client client, Statement transactionStmt) {
        try {
            if (transactionStmt == null) {
                Statement stmt = connect();
                assert stmt != null;
                stmt.executeUpdate("update client set points = '" + points + "' where id = '" + client.getId() + "'");
                disconnect();
            } else {
                transactionStmt.executeUpdate("update client set points = '" + points + "' where id = '" + client.getId() + "'");
            }
        } catch (SQLException e) {
            dbError(e);
            disconnect();
        }
    }

    public boolean makeReservation(Reservation reservation, boolean updatePoints, boolean updateWallet) {
        try {
            Statement stmt = connectTransaction();
            assert stmt != null;
            stmt.executeUpdate("INSERT INTO reservation (date, court, client, time_slot, price, isPremium) VALUES ('" + reservation.getDate() + "', '" + reservation.getCourt().getId() + "', '" + reservation.getClient().getId() + "', '" + reservation.getTime_slot().getTs() + "', '" + reservation.getPrice() + "', '" + reservation.getIsPremium() + "')");
            ResultSet rs = stmt.executeQuery("select id from reservation where date = '" + reservation.getDate() + "' and court = '" + reservation.getCourt().getId() + "' and client = '" + reservation.getClient().getId() + "' and time_slot = '" + reservation.getTime_slot().getTs() + "'");
            rs.next();
            if (reservation.getRentingKit() != null)
                stmt.executeUpdate("INSERT INTO rentingkit_reservation (reservation, renting_kit, num_of_rents) VALUES ('" + rs.getInt(1) + "', '" + reservation.getRentingKit().getId() + "', '" + reservation.getRentingKit().getNumOfRents() + "')");
            rs.close();
            if (updatePoints) updatePoints(reservation.getClient().getPoints(), reservation.getClient(), stmt);
            if (updateWallet) modifyBalance(reservation.getClient(), stmt);
            return commitTransaction();
        } catch (SQLException e) {
            dbError(e);
            return false;
        } finally {
            disconnect();
        }
    }

    public boolean deleteReservation(Reservation reservation, Client client) {
        try {
            Statement stmt = connectTransaction();
            assert stmt != null;
            updatePoints(client.getPoints(), client, stmt);
            client.getWallet().addMoney(reservation.getPrice());
            modifyBalance(client, stmt);
            stmt.executeUpdate("DELETE FROM reservation WHERE id = '" + reservation.getId() + "'");
            commitTransaction();
            return true;
        } catch (SQLException e) {
            dbError(e);
            return false;
        } finally {
            disconnect();
        }
    }


    public boolean modifyPremiumExpiration(Client client) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            Date date = getPremiumExpiration(client);
            if (date == null) return false;
            Calendar calendario = Calendar.getInstance();
            // Aggiungi un anno a date
            calendario.setTime(date);
            calendario.add(Calendar.YEAR, 1);

            // Aggiungi un giorno alla data corrente
            calendario.add(Calendar.DAY_OF_YEAR, 1);

            // Ottieni la data con un anno in più
            java.sql.Date new_date = new java.sql.Date(calendario.getTimeInMillis());
            stmt.executeUpdate("update premium_subs set end_date = '" + new_date + "' where client = '" + client.getId() + "'");
            return true;
        } catch (SQLException e) {
            dbError(e);
            return false;
        } finally {
            disconnect();
        }
    }

    public boolean modifyPremium(Client client) {
        try {
            Statement stmt = connectTransaction();
            assert stmt != null;
            stmt.executeUpdate("update client set is_premium = '" + client.getIsPremium() + "' where id = '" + client.getId() + "'");
            Calendar calendario = Calendar.getInstance();

            calendario.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Aggiungi un giorno alla data corrente
            calendario.add(Calendar.DAY_OF_YEAR, 1);

            // Aggiungi un anno a date
            calendario.add(Calendar.YEAR, 1);

            // Ottieni la data con un anno in più
            java.sql.Date date = new java.sql.Date(calendario.getTimeInMillis());
            stmt.executeUpdate("insert into premium_subs (client, end_date) values ('" + client.getId() + "', '" + date + "')");
            modifyBalance(client, stmt);
            commitTransaction();
            return true;
        } catch (SQLException e) {
            dbError(e);
            return false;
        } finally {
            disconnect();
        }
    }
}