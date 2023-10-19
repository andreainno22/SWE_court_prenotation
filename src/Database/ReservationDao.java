package Database;

import Context.Client;
import Context.Reservation;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;

public interface ReservationDao {
    void printAllReservations(int Client);
    void printAllFutureReservations(int Client);
    Reservation getReservationById(int id);
    ArrayList<Integer> getReservationsId(int Client);
    boolean checkTestReservation(Client client, Date date);
    boolean makeReservation(Reservation reservation, boolean updatePoints, boolean updateWallet);
    boolean deleteReservation(Reservation reservation, Client client);
}
