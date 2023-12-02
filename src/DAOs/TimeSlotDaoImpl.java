package DAOs;

import DomainModel.Customer;
import DomainModel.Reservation;
import DomainModel.TimeSlot;
import DomainModel.Wallet;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDaoImpl implements TimeSlotDao {
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
    public void insertTimeSlot(TimeSlot timeSlot) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("INSERT INTO time_slots VALUES (" + timeSlot.getId() + ", '" + timeSlot.getStart_hour() + "', '" + timeSlot.getFinish_hour() + "')");
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
            ResultSet rs = stmt.executeQuery("select customer.*, wallet.balance, reservation.price from customer, reservation, wallet where wallet.customer = customer.id and customer.id = reservation.customer and reservation.time_slot = '" + id + "'");
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                Customer c = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), new Wallet(rs.getInt(9), rs.getFloat(10) + rs.getFloat(11)));
                reservations.add(new Reservation(rs.getFloat(11), c));
            }
            rs.close();
            db.disconnect();
            Statement stmt1 = db.connectTransaction();
            WalletDao walletDao = new WalletDaoImpl();
            CustomerDao customerDao = new CustomerDaoImpl();
            for (Reservation r : reservations) {
                if(r.getPrice() != 0)
                   walletDao.modifyBalance(r.getCustomer(), stmt1);
                else {
                    int points = r.getCustomer().getPoints() + 100;
                    customerDao.updatePoints(points, r.getCustomer(), stmt1);
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
