package Management;

import Context.*;
//import Database.DatabaseManager;
import Context.TimeSlot;
import Database.*;
import java.util.*;
import java.sql.Date;

public abstract class ClientReservationManager {
    protected final int reservationPoints = 10;
    private List<Court> court_type_prices;
    private List<TimeSlot> time_slots;

    private RentingKit rentingKit;
    protected final int giftPoints = 100;
    //protected final DatabaseManager db = new DatabaseManager();
    private final WalletManager walletManager = new WalletManager();
    private final CourtDaoImpl courtDao = new CourtDaoImpl();
    private final RentingKitDaoImpl rentingKitDao = new RentingKitDaoImpl();
    protected final ReservationDaoImpl reservationDao = new ReservationDaoImpl();
    private  final TimeSlotDaoImpl timeSlotDao = new TimeSlotDaoImpl();

    protected Reservation reservation;
    public abstract boolean makeReservation();

    public void createReservation(Client client, Date date){
        reservation = new Reservation(client, date);
    }

    public void setReservationCourt(int courtId){
        Court court = court_type_prices.get(courtId - 1);
        reservation.setCourt(court);
    }

    public void setReservationTimeSlot(int timeSlot){
        TimeSlot ts = time_slots.get(timeSlot - 1);
        reservation.setTime_slot(ts);
    }

    public RentingKit getRentingKit(){
        return rentingKit;
    }

    public Reservation getReservation(){
        return reservation;
    }

    public void getRentingKitInfo(){
        rentingKit = rentingKitDao.getRentingKit(reservation.getCourt().getType());
    }

    public void setReservationRentingKit(int numOfRentingKits){
        if(numOfRentingKits == 0) {
            reservation.setRentingKit(null);
        }else{
            rentingKit.setNumOfRents(numOfRentingKits);
            reservation.setRentingKit(rentingKit);
        }

    }

    public void getTimeSlots(Formatter fmt, Date date, int court) {
        //Database_management db = new Database_management();
        time_slots = timeSlotDao.getTimeSlots(date, court);
        //System.out.println(time_slots.size());
        fmt.format("%-15s%-15s%-15s\n", "ID", "START HOUR", "END HOUR");
        for (TimeSlot timeSlot : time_slots) {
            if (timeSlot != null)
                timeSlot.printAllTimeSlots(fmt);
        }
    }

    public boolean verifyValidTimeSlot(int timeslot_id){
        if(timeslot_id <= 0)
            return false;
        int[] timeslots_ids = new int[time_slots.size()];
        int scan = 0;
        for(TimeSlot timeSlot : time_slots){
            if(timeSlot != null)
                timeslots_ids[scan] = timeSlot.getId();
            scan++;
        }
        Arrays.sort(timeslots_ids);
        int result = Arrays.binarySearch(timeslots_ids, timeslot_id);
        return (result >= 0);
    }

    public void getCourts(Formatter fmt) {
        //Database_management db = new Database_management();
        if (reservation.getClient().getIsPremium() == 0)
            fmt.format("%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE [€]");
        else
            fmt.format("%-15s%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE [€]", "YOUR PRICE (-10%) [€]");
        court_type_prices = courtDao.getCourts();
        for (Court court_type_price : court_type_prices) {
            court_type_price.printAllCourts(fmt, reservation.getClient().getIsPremium() == 1);
        }
    }

    public boolean verifyValidCourt(int court_id){
        if(court_id <= 0)
            return false;
        int[] court_ids = new int[court_type_prices.size()];
        int scan = 0;
        for(Court court : court_type_prices){
            court_ids[scan] = court.getId();
            scan++;
        }
        Arrays.sort(court_ids);
        int result = Arrays.binarySearch(court_ids, court_id);
        return (result >= 0);
    }

    public void printAllReservations(Client client) {
        //Database_management db = new Database_management();
        reservationDao.printAllClientReservations(client.getId());
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
        reservationDao.printAllClientFutureReservations(client.getId());
    }

    /*public RentingKit getRentingKit(String type) {
        //Database_management db = new Database_management();
        return rentingKitDao.getRentingKit(type);
    }*/

    protected boolean makeReservation(float price, boolean isPremium) {
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
