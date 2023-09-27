package Management;
import Context.*;
import java.sql.Date;

public abstract class ReservationManager {

    public static int globalId = 0;

    public abstract Reservation makeReservation(Court court, Date date, Client client, RentingKit rentingKit);

    public abstract boolean deleteReservation(Reservation reservation);

    public void editReservation(Reservation reservation){};

    public void getAllReservation(Client client){};

    public void getTimeSlots(Date date){};
}
