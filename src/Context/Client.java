package Context;

import Management.ReservationManager;

import java.util.Map;

public class Client {
    public Client(String name, String surname, String email, String password, int telephoneNumber, int id, ReservationManager reservationManager) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.points = 0;
        this.isPremium = 0;
        this.reservationManager = reservationManager;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public int getisPremium() {
        return isPremium;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    void setPremium(int isPremium) {
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
    private int isPremium;
    private ReservationManager reservationManager;
    Map<Integer, Reservation> reservations;
}
