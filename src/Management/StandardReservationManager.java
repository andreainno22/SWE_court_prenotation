package Management;

import Context.*;
import Database.Database_management;
import Database.TimeSlot;

import java.sql.Date;

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
    public void getTimeSlots(Date date) {
        Database_management db = new Database_management();
        TimeSlot[] time_slots = db.getTimeSlots(date);
        for (TimeSlot timeSlot : time_slots) {
            System.out.println(timeSlot.toString());
        }
    }
}
