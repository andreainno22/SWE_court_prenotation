package Management;


public class StandardReservationManager extends ReservationManager {
    @Override
    public boolean makeReservation() {
        boolean isPremium = reservation.getClient().getIsPremium() == 1;
        return super.makeReservation(reservation.getPrice(), isPremium);
    }

}
