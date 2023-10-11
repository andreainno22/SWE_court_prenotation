package Management;

import Context.*;

public class StandardReservationManager extends ReservationManager {
    @Override
    public boolean makeReservation(Reservation reservation) {
        boolean isPremium = reservation.getClient().getIsPremium() == 1;
        return super.makeReservation(reservation, reservation.getPrice(), isPremium);
    }

}
