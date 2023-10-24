package Database;

import Context.Client;
import Context.Reservation;

import java.sql.Date;
import java.util.ArrayList;

public interface ReservationDao extends GeneralDaoSettings{
    void printClientAllReservations(int Client);
    void printAllClientFutureReservations(int Client);
    void printAllReservations();
    void printAllFutureReservations();
    Reservation getReservationById(int id);
    ArrayList<Integer> getReservationsId(int Client);
    boolean checkTestReservation(Client client, Date date);
    boolean makeReservation(Reservation reservation, boolean updatePoints, boolean updateWallet);
    boolean deleteReservation(Reservation reservation, Client client);
}
