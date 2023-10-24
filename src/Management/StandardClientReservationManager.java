package Management;


public class StandardClientReservationManager extends ClientReservationManager {
    @Override
    public boolean makeReservation() {
        boolean isPremium = reservation.getClient().getIsPremium() == 1;
        return super.makeReservation(reservation.getPrice(), isPremium);
    }

}
