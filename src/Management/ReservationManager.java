package Management;

import Context.*;
import Database.Database_management;
import Context.TimeSlot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public abstract class ReservationManager {
    protected final int reservationPoints = 10;

    protected final int giftPoints = 100;

    public abstract boolean makeReservation(Reservation reservation);

    public List<TimeSlot> getTimeSlots(Formatter fmt, Date date, int court) {
        Database_management db = new Database_management();
        List<TimeSlot> time_slots = (db.getTimeSlots(date, court));
        System.out.println(time_slots.size());
        fmt.format("%-15s%-15s%-15s\n", "ID", "START HOUR", "END HOUR");
        for (TimeSlot timeSlot : time_slots) {
            if (timeSlot != null)
                timeSlot.printAllTimeSlots(fmt);
        }
        return time_slots;
    }

    public List<Court> getCourt(Formatter fmt, boolean showDiscount) {
        Database_management db = new Database_management();
        List<Court> court_type_prices = db.getCourt();
        if (!showDiscount)
            fmt.format("%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE [€]");
        else
            fmt.format("%-15s%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE [€]", "YOUR PRICE (-10%) [€]");
        for (Court court_type_price : court_type_prices) {
            court_type_price.printAllCourt(fmt, showDiscount);
        }
        return court_type_prices;
    }

    public void printAllReservations(Client client) {
        Database_management db = new Database_management();
        db.printAllReservations(client.getId());
    }

    public ArrayList<Integer> getReservationsId(Client client) {
        Database_management db = new Database_management();
        return db.getReservationsId(client.getId());
    }

    public Reservation getReservationById(int id) {
        Database_management db = new Database_management();
        return db.getReservationById(id);
    }

    public boolean deleteReservation(Reservation reservation, Client client) {
        if (reservation.getIsPremium() == 1 && reservation.getPrice() != 0)
            client.setPoints(client.getPoints() - reservationPoints);
        if (reservation.getPrice() == 0)
            client.setPoints(client.getPoints() + giftPoints);
        Database_management db = new Database_management();
        return db.deleteReservation(reservation, client);
    }

    public void printAllFutureReservations(Client client) {
        Database_management db = new Database_management();
        db.printAllFutureReservations(client.getId());
    }

    public RentingKit getRentingKit(String type) {
        Database_management db = new Database_management();
        return db.getRentingKit(type);
    }

    protected boolean makeReservation(Reservation reservation, float price, boolean isPremium) {
        System.out.println("Final price: " + String.format("%.2f", price) + "€");
        if (reservation.getClient().getWallet().getBalance() < price) {
            System.out.println("Insufficient balance to proceed with the booking. Please add funds to your wallet!");
            return false;
        } else {
            reservation.setPrice(price);
            Database_management db = new Database_management();
            reservation.getClient().getWallet().removeMoney(price);
            return db.makeReservation(reservation, isPremium, true);
        }
    }
}
