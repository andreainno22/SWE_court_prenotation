package Management;

import Context.*;

public class StandardReservationManager extends ReservationManager {
    @Override
    public Reservation makeReservation(Court court, Date date, Client client, RentingKit rentingKit) {
        float price = court.getPrice() + rentingKit.getPrice();
        if (client.getWallet().getBalance() < price)
            return null;
        else {
            Reservation reservation = new Reservation(globalId++, court, date, client, rentingKit);
            client.getWallet().removeMoney(price);
            return reservation;
        }
    }

    @Override
    public boolean deleteReservation(Reservation reservation) {
        return true;
    }

    @Override
    public void editReservation(Reservation reservation) {

    }

    @Override
    public void getAllReservation(Client client) {

    }
}
