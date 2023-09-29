package Management;

import Context.*;
import Database.Court_type_price;
import Database.Database_management;
import Database.TimeSlot;

import java.sql.Date;
import java.util.List;

public class StandardReservationManager extends ReservationManager {
    @Override
    public Reservation makeReservation(Court court, Date date, Client client, RentingKit rentingKit) {
        float price = court.getPrice() + rentingKit.getPrice();
        if (client.getWallet().getBalance() < price)
            return null;
        else {
            Reservation reservation = new Reservation(globalId++, court, date, client, rentingKit);
            client.getWallet().removeMoney(price);
            return reservation;
        }
    }

    @Override
    public boolean deleteReservation(Reservation reservation) {
        return true;
    }

    @Override
    public void editReservation(Reservation reservation) {

    }

    @Override
    public void getAllReservation(Client client) {

    }

    @Override
    public void getTimeSlots(Date date, int court) {
        Database_management db = new Database_management();
        List<TimeSlot> time_slots = (db.getTimeSlots(date, court));
        for (TimeSlot timeSlot : time_slots) {
            timeSlot.printAllTimeSlots();
        }
    }

    @Override
    public void getCourt() {
        Database_management db = new Database_management();
        List<Court_type_price> court_type_prices = db.getCourt();
        for (Court_type_price court_type_price : court_type_prices) {
            court_type_price.printAllCourt();
        }
    }
}
