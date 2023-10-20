package Management;

import Context.*;
//import Database.DatabaseManager;
import Context.TimeSlot;
import Database.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public abstract class ReservationManager {
    protected final int reservationPoints = 10;
    protected final int giftPoints = 100;
    //protected final DatabaseManager db = new DatabaseManager();
    private final WalletManager walletManager = new WalletManager();
    private final CourtDaoImpl courtDao = new CourtDaoImpl();

    private final RentingKitDaoImpl rentingKitDao = new RentingKitDaoImpl();
    protected final ReservationDaoImpl reservationDao = new ReservationDaoImpl();
    private  final TimeSlotDaoImpl timeSlotDao = new TimeSlotDaoImpl();

    public abstract boolean makeReservation(Reservation reservation);

    public List<TimeSlot> getTimeSlots(Formatter fmt, Date date, int court) {
        //Database_management db = new Database_management();
        List<TimeSlot> time_slots = (timeSlotDao.getTimeSlots(date, court));
        System.out.println(time_slots.size());
        fmt.format("%-15s%-15s%-15s\n", "ID", "START HOUR", "END HOUR");
        for (TimeSlot timeSlot : time_slots) {
            if (timeSlot != null)
                timeSlot.printAllTimeSlots(fmt);
        }
        return time_slots;
    }

    public List<Court> getCourt(Formatter fmt, boolean showDiscount) {
        //Database_management db = new Database_management();
        List<Court> court_type_prices = courtDao.getCourt();
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
        //Database_management db = new Database_management();
        reservationDao.printAllReservations(client.getId());
    }

    public ArrayList<Integer> getReservationsId(Client client) {
        //Database_management db = new Database_management();
        return reservationDao.getReservationsId(client.getId());
    }

    public Reservation getReservationById(int id) {
        //Database_management db = new Database_management();
        return reservationDao.getReservationById(id);
    }

    public boolean deleteReservation(Reservation reservation, Client client) {
        if (reservation.getIsPremium() == 1 && reservation.getPrice() != 0)
            client.setPoints(client.getPoints() - reservationPoints);
        if (reservation.getPrice() == 0)
            client.setPoints(client.getPoints() + giftPoints);
        //Database_management db = new Database_management();
        return reservationDao.deleteReservation(reservation, client);
    }

    public void printAllFutureReservations(Client client) {
        //Database_management db = new Database_management();
        reservationDao.printAllFutureReservations(client.getId());
    }

    public RentingKit getRentingKit(String type) {
        //Database_management db = new Database_management();
        return rentingKitDao.getRentingKit(type);
    }

    protected boolean makeReservation(Reservation reservation, float price, boolean isPremium) {
        System.out.println("Final price: " + String.format("%.2f", price) + "€");
        if (walletManager.getWalletBalance(reservation.getClient()) < price) {
            System.out.println("Insufficient balance to proceed with the booking. Please add funds to your wallet!");
            return false;
        } else {
            reservation.setPrice(price);
            //Database_management db = new Database_management();
            //reservation.getClient().getWallet().removeMoney(price);
            walletManager.withdrawalWallet(price, reservation.getClient());
            return reservationDao.makeReservation(reservation, isPremium, true);
        }
    }
}
