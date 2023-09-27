package Management;

import Context.Court;
import Context.RentingKit;
import Context.Reservation;
import Context.Client;
import Database.Database_management;
import Database.TimeSlot;

import java.sql.Date;

public class PremiumReservationManager extends ReservationManager {

    @Override
    public Reservation makeReservation(Court court, Date date, Client client, RentingKit rentingKit) {
        float price = 90 * (court.getPrice() + rentingKit.getPrice()) / 100;
        if (client.getWallet().getBalance() < price)
            return null;
        else {
            // RIMUOVE PRIMA SOLDI DAL DATABASE
            client.getWallet().removeMoney(price);
            Reservation reservation = new Reservation(globalId++, court, date, client, rentingKit);
            return reservation;
        }
    }

    @Override
    public void editReservation(Reservation reservation) {

    }

    public boolean deleteReservation(Reservation reservation) {
        return false;
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
