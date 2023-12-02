package BusinessLogic;


public class StandardCustomerReservationManager extends CustomerReservationManager {
    @Override
    public boolean makeReservation() {
        boolean isPremium = reservation.getCustomer().getIsPremium() == 1;
        return super.makeReservation(reservation.getPrice(), isPremium);
    }

}
