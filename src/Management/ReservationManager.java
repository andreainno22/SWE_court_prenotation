package Management;
import Context.*;
import Database.Court_type_price;
import Database.Database_management;
import Database.TimeSlot;

import java.sql.Date;
import java.util.Formatter;
import java.util.List;

public abstract class ReservationManager {

    public static int globalId = 0;

    public abstract Reservation makeReservation(Court court, Date date, Client client, RentingKit rentingKit);

    public abstract boolean deleteReservation(Reservation reservation);

    public void editReservation(Reservation reservation){};

    public void getAllReservation(Client client){};

    public boolean[] getTimeSlots(Formatter fmt, Date date, int court){
        boolean[] id_slots = new boolean[14];
        Database_management db = new Database_management();
        List<TimeSlot> time_slots = (db.getTimeSlots(date, court));
        fmt.format("%-15s%-15s%-15s\n", "ID", "START", "END");
        for (TimeSlot timeSlot : time_slots) {
            timeSlot.printAllTimeSlots(fmt);
            id_slots[timeSlot.getTs()-1] = true;
        }
        return id_slots;
    };

    public int getCourt(Formatter fmt) {
        Database_management db = new Database_management();
        List<Court_type_price> court_type_prices = db.getCourt();
        fmt.format("%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE");
        int num_of_courts = 0;
        for (Court_type_price court_type_price : court_type_prices) {
            court_type_price.printAllCourt(fmt);
            num_of_courts++;
        }
        return num_of_courts;
    }
}
