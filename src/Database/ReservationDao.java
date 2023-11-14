package Database;

import Context.Client;
import Context.Reservation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface ReservationDao extends Dao {
    List<Reservation> getAllClientReservations(int client_id);
    List<Reservation> getAllClientFutureReservations(int client_id);
    List<Reservation> getAllReservationsAtDate(java.sql.Date date);
    List<Reservation> getAllFutureReservations();
    Reservation getReservationById(int id);
    ArrayList<Integer> getReservationsId(int client_id);
    boolean checkTestReservation(Client client, Date date);
    boolean insertReservation(Reservation reservation, boolean updatePoints, boolean updateWallet);
    boolean deleteReservation(Reservation reservation, Client client);
}
