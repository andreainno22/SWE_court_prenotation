package Database;

import Context.Client;
import Context.Reservation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface ReservationDao extends Dao {
    List<Reservation> getAllClientReservations(int Client);
    List<Reservation> getAllClientFutureReservations(int Client);
    List<Reservation> getAllReservationsAtDate(java.sql.Date date);
    List<Reservation> getAllFutureReservations();
    Reservation getReservationById(int id);
    ArrayList<Integer> getReservationsId(int Client);
    boolean checkTestReservation(Client client, Date date);
    boolean insertReservation(Reservation reservation, boolean updatePoints, boolean updateWallet);
    boolean deleteReservation(Reservation reservation, Client client);
}
