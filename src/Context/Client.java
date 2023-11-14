package Context;

import Management.ClientReservationManager;

public class Client extends Person{
    // Constructor for testing
    public Client(int id, String name, String surname, String email, String password, int telephoneNumber, int isPremium, int points, Wallet wallet) {
        super(id, name, surname, email, password, telephoneNumber);
        this.points = points;
        this.isPremium = isPremium;
        this.wallet = wallet;
    }

    public Client(int id, String name, String surname, String email, int telephoneNumber, int isPremium, int points) {
        super(id, name, surname, email, telephoneNumber);
        this.points = points;
        this.isPremium = isPremium;
    }

    // Constructor for database
    public Client(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public void setReservationManager(ClientReservationManager clientReservationManager) {
        this.clientReservationManager = clientReservationManager;
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

    public ClientReservationManager getReservationManager() {
        return clientReservationManager;
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
    private ClientReservationManager clientReservationManager;
}
