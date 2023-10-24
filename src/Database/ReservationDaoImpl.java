package Database;

import Context.Client;
import Context.Court;
import Context.Reservation;
import Context.TimeSlot;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Formatter;

public class ReservationDaoImpl implements ReservationDao {

    //private final DatabaseManager db = new DatabaseManager();
    @Override
    public void printClientAllReservations(int Client) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select reservation.id, reservation.date, court, start_hour, finish_hour, reservation.price, case when num_of_rents is null then '0' else num_of_rents end as num_of_rentingkit from (reservation join time_slots on reservation.time_slot = time_slots.id) left join rentingkit_reservation rr on rr.reservation = reservation.id where client = '" + Client + "'");
            Formatter fmt = new Formatter();
            formatOutput(fmt, rs);
            System.out.println(fmt);
            rs.close();
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    private void formatOutput(Formatter fmt, ResultSet rs) throws SQLException{
        fmt.format("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "ID", "DATE", "COURT", "START TIME", "END TIME", "PRICE [€]", "NUMBER OF RENTING KITS");
        while (rs.next()) {
            fmt.format("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", rs.getInt(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getFloat(6), rs.getInt(7));
        }
    }

    @Override
    public void printAllReservations() {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select reservation.id, reservation.date, court, start_hour, finish_hour, reservation.price, case when num_of_rents is null then '0' else num_of_rents end as num_of_rentingkit from (reservation join time_slots on reservation.time_slot = time_slots.id) left join rentingkit_reservation rr on rr.reservation = reservation.id");

            Formatter fmt = new Formatter();
            formatOutput(fmt, rs);
            System.out.println(fmt);
            rs.close();
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public void printAllFutureReservations() {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            //Date today = new Date(Calendar.getInstance().getTimeInMillis());
            ZoneId italyZone = ZoneId.of("Europe/Rome");
            // Crea una data nel fuso orario italiano
            LocalDate today = LocalDate.now(italyZone);
            //Date today = Date.from(italianDate.atStartOfDay().atZone(italyZone).toInstant());
            ResultSet rs = stmt.executeQuery("select reservation.id, reservation.date, court, start_hour, finish_hour, reservation.price, case when num_of_rents is null then '0' else num_of_rents end as num_of_rentingkit from (reservation join time_slots on reservation.time_slot = time_slots.id) left join rentingkit_reservation rr on rr.reservation = reservation.id where date > '" + today + "'");
            Formatter fmt = new Formatter();
            formatOutput(fmt, rs);
            System.out.println(fmt);
            rs.close();
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public void printAllClientFutureReservations(int Client) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            //Date today = new Date(Calendar.getInstance().getTimeInMillis());
            ZoneId italyZone = ZoneId.of("Europe/Rome");
            // Crea una data nel fuso orario italiano
            LocalDate today = LocalDate.now(italyZone);
            //Date today = Date.from(italianDate.atStartOfDay().atZone(italyZone).toInstant());
            ResultSet rs = stmt.executeQuery("select reservation.id, reservation.date, court, start_hour, finish_hour, reservation.price, case when num_of_rents is null then '0' else num_of_rents end as num_of_rentingkit from (reservation join time_slots on reservation.time_slot = time_slots.id) left join rentingkit_reservation rr on rr.reservation = reservation.id where client = '" + Client + "' and date > '" + today + "'");
            Formatter fmt = new Formatter();
            formatOutput(fmt, rs);
            System.out.println(fmt);
            rs.close();
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public Reservation getReservationById(int id) {
        try {
            Statement stmt1 = db.connect();
            assert stmt1 != null;
            Statement stmt2 = db.newStatement();
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
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

    @Override
    public ArrayList<Integer> getReservationsId(int Client) {
        Statement stmt = db.connect();
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
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

    @Override
    public boolean checkTestReservation(Client client, Date date) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select * from reservation where client = '" + client.getId() + "' and date = '" + date + "'");
            if (!rs.next()) {
                rs.close();
                db.disconnect();
                return false;
            }
            rs.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Reservation not found or other generic SQLException.");
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            db.disconnect();
        }
        return false;
    }

    @Override
    public boolean makeReservation(Reservation reservation, boolean updatePoints, boolean updateWallet) {
        try {
            Statement stmt = db.connectTransaction();
            assert stmt != null;
            stmt.executeUpdate("INSERT INTO reservation (date, court, client, time_slot, price, isPremium) VALUES ('" + reservation.getDate() + "', '" + reservation.getCourt().getId() + "', '" + reservation.getClient().getId() + "', '" + reservation.getTime_slot().getId() + "', '" + reservation.getPrice() + "', '" + reservation.getIsPremium() + "')");
            ResultSet rs = stmt.executeQuery("select id from reservation where date = '" + reservation.getDate() + "' and court = '" + reservation.getCourt().getId() + "' and client = '" + reservation.getClient().getId() + "' and time_slot = '" + reservation.getTime_slot().getId() + "'");
            rs.next();
            if (reservation.getRentingKit() != null)
                stmt.executeUpdate("INSERT INTO rentingkit_reservation (reservation, renting_kit, num_of_rents) VALUES ('" + rs.getInt(1) + "', '" + reservation.getRentingKit().getId() + "', '" + reservation.getRentingKit().getNumOfRents() + "')");
            rs.close();
            if (updatePoints){
                ClientDaoImpl clientDao = new ClientDaoImpl();
                clientDao.updatePoints(reservation.getClient().getPoints(), reservation.getClient(), stmt);
            }
            if (updateWallet) {
                WalletDaoImpl walletDao = new WalletDaoImpl();
                walletDao.modifyBalance(reservation.getClient(), stmt);
            }
            return db.commitTransaction();
        } catch (SQLException e) {
            db.dbError(e);
            return false;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public boolean deleteReservation(Reservation reservation, Client client) {
        try {
            Statement stmt = db.connectTransaction();
            assert stmt != null;
            ClientDaoImpl clientDao = new ClientDaoImpl();
            clientDao.updatePoints(client.getPoints(), client, stmt);
            client.getWallet().addMoney(reservation.getPrice());
            WalletDaoImpl walletDao = new WalletDaoImpl();
            walletDao.modifyBalance(client, stmt);
            stmt.executeUpdate("DELETE FROM reservation WHERE id = '" + reservation.getId() + "'");
            db.commitTransaction();
            return true;
        } catch (SQLException e) {
            db.dbError(e);
            return false;
        } finally {
            db.disconnect();
        }
    }
}
