package DAOs;

import DomainModel.Customer;
import DomainModel.Reservation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface ReservationDao extends Dao {
    List<Reservation> getAllCustomerReservations(int customer_id);
    List<Reservation> getAllCustomerFutureReservations(int customer_id);
    List<Reservation> getAllReservationsAtDate(java.sql.Date date);
    List<Reservation> getAllFutureReservations();
    Reservation getReservationById(int id);
    ArrayList<Integer> getReservationsId(int customer_id);
    boolean checkTestReservation(Customer customer, Date date);
    boolean insertReservation(Reservation reservation, boolean updatePoints, boolean updateWallet);
    boolean deleteReservation(Reservation reservation, Customer customer);
}
