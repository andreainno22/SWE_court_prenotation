package Database;

public class TimeSlot {
    String start_hour;
    String finish_hour;
    int ts;

    public TimeSlot(int ts, String start_hour, String finish_hour) {
        this.start_hour = start_hour;
        this.finish_hour = finish_hour;
        this.ts = ts;
    }

    public int getTs() {
        return ts;
    }

    public void printAllTimeSlots() {
        System.out.println(start_hour + " - " + finish_hour);
    }
}