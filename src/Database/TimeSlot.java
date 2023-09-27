package Database;

public class TimeSlot {
    int id;
    String start_hour;
    String finish_hour;
    int ts;

    public TimeSlot(int id, String start_hour, String finish_hour, int ts) {
        this.id = id;
        this.start_hour = start_hour;
        this.finish_hour = finish_hour;
        this.ts = ts;
    }

    public int getTs() {
        return ts;
    }

    public void printAllTimeSlots() {
        System.out.println(" start_hour: " + start_hour + " finish_hour: " + finish_hour + " court: " + id);
    }
}