package Management;

import Context.Reservation;
import Database.ReservationDaoImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class SuperUserReservationManager {
    private final ReservationDaoImpl reservationDao = new ReservationDaoImpl();
    public void printAllTodayReservations(){
        ZoneId italyZone = ZoneId.of("Europe/Rome");
        // Crea una data nel fuso orario italiano
        LocalDate italianDate = LocalDate.now(italyZone);
        java.sql.Date italianZonedDate = Date.valueOf(italianDate);
        System.out.println("Today date is: " + italianZonedDate + "\n");
        List<Reservation> reservations = reservationDao.getAllReservationsAtDate(italianZonedDate);
        System.out.println(Utils.formatOutput(reservations));
    }
    public void printAllFutureReservations(){
        List<Reservation> reservations = reservationDao.getAllFutureReservations();
        System.out.println(Utils.formatOutput(reservations));
    }
}
