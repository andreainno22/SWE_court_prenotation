package Management;
import Context.*;
import Database.Database_management;
import Context.TimeSlot;
import java.sql.Date;
import java.util.Formatter;
import java.util.List;

public abstract class ReservationManager {

    public static int globalId = 0;

    public abstract boolean makeReservation(Reservation reservation);

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

    public List<Court> getCourt(Formatter fmt) {
        Database_management db = new Database_management();
        List<Court> court_type_prices = db.getCourt();
        fmt.format("%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE [€]");
        for (Court court_type_price : court_type_prices) {
            court_type_price.printAllCourt(fmt);
        }
        return court_type_prices;
    }

    public void printAllReservations(Client client) {
        Database_management db = new Database_management();
        db.printAllReservations(client.getId());
    }

    public int[] getReservationsId(Client client) {
        Database_management db = new Database_management();
        return db.getReservationsId(client.getId());
    }

    public float getReservationPrice(int reservation) {
        Database_management db = new Database_management();
        return db.getReservationPrice(reservation);
    }

    public boolean removeMoney(Client client, float price) {
        Database_management db = new Database_management();
        if(client.getWallet().removeMoney(price)) {
            db.modifyBalance(client);
            return true;
        }
        else System.out.println("Insufficient funds");
        return false;
    }

    public void setIsPremium(Client client) {
        Database_management db = new Database_management();
        client.setIsPremium(1);
        db.modifyPremium(client);
    }

    public void addMoney(Client client, float money) {
        Database_management db = new Database_management();
        client.getWallet().addMoney(money);
        db.modifyBalance(client);
    }

    public boolean deleteReservation(int reservation, Client client) {
        Database_management db = new Database_management();
        return db.deleteReservation(reservation);
    }

    public RentingKit getRentingKit(String type) {
        Database_management db = new Database_management();

        return db.getRentingKit(type);
    }
    protected boolean makeReservation(Reservation reservation, float price) {
        if (reservation.getClient().getWallet().getBalance() < price){
            System.out.println("Insufficient balance to proceed with the booking. Please add funds to your wallet!");
            return false;
        }else {
            reservation.setPrice(price);
            Database_management db = new Database_management();
            if(db.makeReservation(reservation)) {
                reservation.getClient().getWallet().removeMoney(price);
                return db.modifyBalance(reservation.getClient());
            }
        }
        return false;
    }
}
