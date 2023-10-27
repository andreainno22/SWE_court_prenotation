package Context;

import java.sql.Date;

public class Reservation {

    public Reservation(int id, Date date, TimeSlot time_slot, float price, int isPremium) {
        this.id = id;
        this.date = date;
        this.time_slot = time_slot;
        this.price = price;
        this.isPremium = isPremium;
    }

    public Reservation(float price, Client client){
        this.price = price;
        this.client = client;
    }

    public Reservation(int id, Date date, float price){
        this.id = id;
        this.date = date;
        this.price = price;
    }

    public Reservation(Client client, Date date) {
        this.client = client;
        this.date = date;
    }

    public void setIsPremium(int isPremium) {
        this.isPremium = isPremium;
    }

    public int getId() {
        return id;
    }

    public Court getCourt() {
        return court;
    }

    public void setRentingKit(RentingKit rentingKit) {
        this.rentingKit = rentingKit;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public void setTime_slot(TimeSlot time_slot) {
        this.time_slot = time_slot;
    }

    public Reservation(Client client) {
        this.client = client;
    }

    public RentingKit getRentingKit() {
        return rentingKit;
    }

    public Date getDate() {
        return date;
    }

    public TimeSlot getTime_slot() {
        return time_slot;
    }

    public Client getClient() {
        return client;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        if (price == -1) {
            if (getRentingKit() != null)
                price = (getCourt().getPrice() + getRentingKit().getTotPrice());
            else price = getCourt().getPrice();
        }
        return price;
    }

    /*public float getPrice(Client client) {
        if (client.getIsPremium() == 1)
            return getPrice() * 0.9f;
        return getPrice();
    }*/

    public int getIsPremium() {
        return isPremium;
    }

    private int id;
    private Court court;
    private Date date;
    private RentingKit rentingKit;
    private Client client;
    private TimeSlot time_slot;
    private float price = -1;
    private int isPremium;

}
