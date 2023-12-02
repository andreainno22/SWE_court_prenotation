package DomainModel;

import BusinessLogic.CustomerReservationManager;

public class Customer extends Person{
    // Constructor for testing
    public Customer(int id, String name, String surname, String email, String password, int telephoneNumber, int isPremium, int points, Wallet wallet) {
        super(id, name, surname, email, password, telephoneNumber);
        this.points = points;
        this.isPremium = isPremium;
        this.wallet = wallet;
    }

    public Customer(int id, String name, String surname, String email, int telephoneNumber, int isPremium, int points) {
        super(id, name, surname, email, telephoneNumber);
        this.points = points;
        this.isPremium = isPremium;
    }

    // Constructor for database
    public Customer(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public void setReservationManager(CustomerReservationManager customerReservationManager) {
        this.customerReservationManager = customerReservationManager;
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

    public void setPoints(int points) {
        this.points = points;
    }

    public CustomerReservationManager getReservationManager() {
        return customerReservationManager;
    }

    public void setIsPremium(int isPremium) {
        this.isPremium = isPremium;
    }

    public Wallet getWallet() {
        return wallet;
    }

    private Wallet wallet;
    private int points;
    private int isPremium;
    private CustomerReservationManager customerReservationManager;
}
