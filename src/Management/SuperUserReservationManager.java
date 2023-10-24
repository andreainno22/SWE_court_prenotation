package Management;

import Database.ReservationDaoImpl;

public class SuperUserReservationManager {
    private final ReservationDaoImpl reservationDao = new ReservationDaoImpl();
    public void printAllReservations(){
        reservationDao.printAllReservations();
    }
    public void printAllFutureReservations(){
        reservationDao.printAllFutureReservations();
    }
}
