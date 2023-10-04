package Context;
import java.sql.Date;

public class Reservation {
    public Reservation(int id, Court court, Date date, Client client, RentingKit rentingKit) {
        this.id = id;
        //this.court = court;
        this.date = date;
        this.rentingKit = rentingKit;
        this.client = client;
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

    public void setTime_slot(int time_slot) {
        this.time_slot = time_slot;
    }

    public Reservation(){

    }

    public int getId() {
        return id;
    }

    private int id;
    private Court court;
    private Date date;
    private RentingKit rentingKit;
    private Client client;
    private int time_slot;

}