package Database;

import Context.Client;
import Context.Reservation;
import Context.TimeSlot;
import Context.Wallet;
import Management.ClientReservationManager;
import Management.PremiumClientReservationManager;
import Management.StandardClientReservationManager;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDaoImpl implements TimeSlotDao {
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
    public List<Reservation> deleteTimeSlot(int id) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select client.*, wallet.balance, reservation.price from client, reservation, wallet where wallet.client = client.id and client.id = reservation.client and reservation.time_slot = '" + id + "'");
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                Client c = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), new Wallet(rs.getInt(9), rs.getFloat(10) + rs.getFloat(11)));
                reservations.add(new Reservation(rs.getFloat(11), c));
            }
            rs.close();
            db.disconnect();
            Statement stmt1 = db.connectTransaction();
            WalletDaoImpl walletDao = new WalletDaoImpl();
            ClientDaoImpl clientDao = new ClientDaoImpl();
            for (Reservation r : reservations) {
                if(r.getPrice() != 0)
                   walletDao.modifyBalance(r.getClient(), stmt1);
                else {
                    int points = r.getClient().getPoints() + 100;
                    clientDao.updatePoints(points, r.getClient(), stmt1);
                }
            }
            stmt1.executeUpdate("DELETE FROM time_slots WHERE id = " + id);
            db.commitTransaction();
            return reservations;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

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
