package DomainModel;

import java.sql.Date;

public class Reservation {

    public Reservation (int id, Date date, Court court, TimeSlot time_slot, float price, RentingKit rentingKit) {
        this.id = id;
        this.date = date;
        this.court = court;
        this.rentingKit = rentingKit;
        this.time_slot = time_slot;
        this.price = price;
    }
    public Reservation(int id, Date date, TimeSlot time_slot, float price, int isPremium) {
        this.id = id;
        this.date = date;
        this.time_slot = time_slot;
        this.price = price;
        this.isPremium = isPremium;
    }

    public Reservation(float price, Customer customer){
        this.price = price;
        this.customer = customer;
    }

    public Reservation(int id, Date date, float price){
        this.id = id;
        this.date = date;
        this.price = price;
    }

    public Reservation(Customer customer, Date date) {
        this.customer = customer;
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

    public Reservation(Customer customer) {
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
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

    public int getIsPremium() {
        return isPremium;
    }

    private int id;
    private Court court;
    private Date date;
    private RentingKit rentingKit;
    private Customer customer;
    private TimeSlot time_slot;
    private float price = -1;
    private int isPremium;

}
