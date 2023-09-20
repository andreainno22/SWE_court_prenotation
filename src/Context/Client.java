package Context;

import Management.ReservationManager;

import java.util.Map;

public class Client {
    public Client(String name, String surname, String email, String password, int telephoneNumber, int id, ReservationManager reservationManager) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.wallet = new Wallet();
        this.telephoneNumber = telephoneNumber;
        this.id = id;
        this.points = 0;
        this.isPremium = false;
        this.reservationManager = reservationManager;
    }

    void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }

    void makeReservation(Date date, Court court, RentingKit rentingKit, int numOfRent) {
        Reservation r = reservationManager.makeReservation(court, date, this, rentingKit);
        reservations.put(r.getId(), r);
    }

    void deleteReservation(int id) {
        Reservation reservation = reservations.get(id);
        if (reservationManager.deleteReservation(reservation))
            reservations.remove(id);
    }

    public Wallet getWallet() {
        return wallet;
    }

    private String name;
    private String surname;
    private String email;
    private String password;
    private Wallet wallet;
    private int telephoneNumber;
    private int id;
    private int points;
    private boolean isPremium;
    private ReservationManager reservationManager;
    Map<Integer, Reservation> reservations;
}
