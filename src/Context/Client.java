package Context;

import Management.ReservationManager;

import java.util.Map;

import java.sql.Date;

public class Client {
    // Constructor for testing
    public Client(int id, String name, String surname, String email, String password, int telephoneNumber, int isPremium, int points, Wallet wallet) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.telephoneNumber = telephoneNumber;
        this.points = points;
        this.isPremium = isPremium;
        this.wallet = wallet;
    }

    // Constructor for database
    public Client(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
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

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setReservationManager(ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
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

    public int getIsPremium() {
        return isPremium;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    public void setIsPremium(int isPremium) {
        this.isPremium = isPremium;
    }

    /*void makeReservation(Date date, Court court, RentingKit rentingKit, int numOfRent) {
        Reservation r = reservationManager.makeReservation(court, date, this, rentingKit);
        reservations.put(r.getId(), r);
    }*/

    /*void deleteReservation(int id) {
        Reservation reservation = reservations.get(id);
        if (reservationManager.deleteReservation(reservation))
            reservations.remove(id);
    }*/

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
