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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCourt_id(int court_id) {
        this.court_id = court_id;
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
    //private Court court;
    private int court_id;
    private Date date;
    private RentingKit rentingKit;
    private Client client;
    private int time_slot;

}
