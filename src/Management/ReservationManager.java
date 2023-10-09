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

    public void getAllReservation(Client client) {
    }

    public boolean[] getTimeSlots(Formatter fmt, Date date, int court) {
        boolean[] id_slots = new boolean[14];
        Database_management db = new Database_management();
        List<TimeSlot> time_slots = (db.getTimeSlots(date, court));
        fmt.format("%-15s%-15s%-15s\n", "ID", "START HOUR", "END HOUR");
        for (TimeSlot timeSlot : time_slots) {
            timeSlot.printAllTimeSlots(fmt);
            id_slots[timeSlot.getTs() - 1] = true;
        }
        return id_slots;
    }

    public List<Court> getCourt(Formatter fmt, boolean showDiscount) {
        Database_management db = new Database_management();
        List<Court> court_type_prices = db.getCourt();
        if(!showDiscount)
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

    //public float getReservationPrice(int reservation) {
    //   Database_management db = new Database_management();
    //  return db.getReservationPrice(reservation);
    //}

    /*public boolean removeMoney(Client client, float price) {
        Database_management db = new Database_management();
        if (client.getWallet().removeMoney(price)) {
            db.modifyBalance(client, null);
            return true;
        } else System.out.println("Insufficient funds");
        return false;
    }*/

    /*public boolean addMoney(Client client, float money) {
        Database_management db = new Database_management();
        client.getWallet().addMoney(money);
        return db.modifyBalance(client, null);
    }*/

    public boolean deleteReservation(Reservation reservation, Client client) {
        if (reservation.getIsPremium() == 1 && reservation.getPrice() != 0)
            client.setPoints(client.getPoints() - reservationPoints);
        if (reservation.getPrice() == 0)
            client.setPoints(client.getPoints() + giftPoints);
        Database_management db = new Database_management();
        return db.deleteReservation(reservation, client);
    }

    public RentingKit getRentingKit(String type) {
        Database_management db = new Database_management();
        return db.getRentingKit(type);
    }

    protected boolean makeReservation(Reservation reservation, float price, boolean isPremium) {
        System.out.println("Final price: " + price + "€");
        if (reservation.getClient().getWallet().getBalance() < price) {
            System.out.println("Insufficient balance to proceed with the booking. Please add funds to your wallet!");
            return false;
        } else {
            reservation.setPrice(price);
            Database_management db = new Database_management();
            reservation.getClient().getWallet().removeMoney(price);
            return db.makeReservation(reservation, isPremium, true);
            //return db.modifyBalance(reservation.getClient());
        }
    }
}
