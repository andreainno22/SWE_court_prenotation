package Database;

import Context.Client;
import Context.TimeSlot;
import Context.Wallet;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDaoImpl implements TimeSlotDao {
    //private final DatabaseManager db = new DatabaseManager();
    private final ReservationDaoImpl reservationDao = new ReservationDaoImpl();

    @Override
    public List<TimeSlot> getTimeSlots(Date date, int court_id) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT * FROM time_slots WHERE time_slots.id NOT IN(SELECT time_slots.id as ts FROM court JOIN reservation ON reservation.court = court.id JOIN time_slots ON time_slots.id = reservation.time_slot WHERE court.id = '" + court_id + "' AND reservation.date = '" + date + "')");
            List<TimeSlot> timeSlots = new ArrayList<>();
            int id = 1;
            while (rs.next()) {
                if (rs.getInt(1) != id) {
                    do {
                        timeSlots.add(null);
                        id++;
                    } while (rs.getInt(1) != id);
                }
                timeSlots.add(new TimeSlot(rs.getInt(1), rs.getString(2), rs.getString(3)));
                id++;
            }
            rs.close();
            return timeSlots;
        } catch (SQLException e) {
            db.dbError(e);
            return null;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public void insertTimeSlot(int id, String start_time, String end_time) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("INSERT INTO time_slots VALUES (" + id + ", '" + start_time + "', '" + end_time + "')");
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public List<Client> deleteTimeSlot(int id) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select client.*, wallet.balance, reservation.price from client, reservation, wallet where wallet.client = client.id and client.id = reservation.client and reservation.time_slot = '" + id + "'");
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), new Wallet(rs.getInt(9), rs.getFloat(10) + rs.getFloat(11))));
            }
            rs.close();
            db.disconnect();
            Statement stmt1 = db.connectTransaction();
            WalletDaoImpl walletDao = new WalletDaoImpl();
            for (Client client : clients) {
                walletDao.modifyBalance(client, stmt1);
            }
            stmt1.executeUpdate("DELETE FROM time_slots WHERE id = " + id);
            db.commitTransaction();
            return clients;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
        //todo: ricontrollare se funziona tutto quando il db parte
    }//soldi 9927.1

    @Override
    public List<TimeSlot> getAllTimeSlots() {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT * FROM time_slots");
            List<TimeSlot> timeSlots = new ArrayList<>();
            while (rs.next()) {
                timeSlots.add(new TimeSlot(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            rs.close();
            return timeSlots;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }
}
