package Management;

import Context.*;

public class StandardReservationManager extends ReservationManager {
    @Override
    public boolean makeReservation(Reservation reservation) {
        float price = reservation.getCourt().getPrice();
        if(reservation.getRentingKit() != null)
            price += reservation.getRentingKit().getTotPrice();
        boolean isPremium = reservation.getClient().getIsPremium() == 1;
        return super.makeReservation(reservation, price, isPremium);
    }
    @Override
    public boolean deleteReservation(int reservation, Client client) {
        return super.deleteReservation(reservation, client);
    }


    @Override
    public void getAllReservation(Client client) {

    }

    /*@Override
    public void getTimeSlots(Date date, int court) {
        Database_management db = new Database_management();
        List<TimeSlot> time_slots = (db.getTimeSlots(date, court));
        Formatter fmt = new Formatter();
        fmt.format("%-15s%-15s%-15s\n", "ID", "START", "END");
        for (TimeSlot timeSlot : time_slots) {
            timeSlot.printAllTimeSlots(fmt);
        }
        System.out.println(fmt);
    }*/

    /*@Override
    public void getCourt() {
        Database_management db = new Database_management();
        List<Court_type_price> court_type_prices = db.getCourt();
        Formatter fmt = new Formatter();
        fmt.format("%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE");
        for (Court_type_price court_type_price : court_type_prices) {
            court_type_price.printAllCourt(fmt);
        }
        System.out.println(fmt);
    }*/
}
