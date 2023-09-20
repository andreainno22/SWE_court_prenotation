package Context;

public class Reservation {
    public Reservation(int id, Court court, Date date, Client client, RentingKit rentingKit) {
        this.id = id;
        this.court = court;
        this.date = date;
        this.rentingKit = rentingKit;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    private int id;
    private Court court;
    private Date date;
    private RentingKit rentingKit;
    private Client client;

}
