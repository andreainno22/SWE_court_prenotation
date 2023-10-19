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
    public void printAllReservations(int Client);
    public void printAllFutureReservations(int Client);
    public Reservation getReservationById(int id);
    public ArrayList<Integer> getReservationsId(int Client);
    public boolean checkTestReservation(Client client, Date date);
    public boolean makeReservation(Reservation reservation, boolean updatePoints, boolean updateWallet);
    public boolean deleteReservation(Reservation reservation, Client client);
}
