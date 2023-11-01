package Management;

import Database.ReservationDaoImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class SuperUserReservationManager {
    private final ReservationDaoImpl reservationDao = new ReservationDaoImpl();
    public void printAllTodayReservations(){
        ZoneId italyZone = ZoneId.of("Europe/Rome");
        // Crea una data nel fuso orario italiano
        LocalDate italianDate = LocalDate.now(italyZone);
        java.sql.Date italianZonedDate = Date.valueOf(italianDate);
        System.out.println("Today date is: " + italianZonedDate + "\n");
        reservationDao.printAllReservationsAtDate(italianZonedDate);
    }
    public void printAllFutureReservations(){
        reservationDao.printAllFutureReservations();
    }
}
