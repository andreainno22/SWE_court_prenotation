package DomainModel;

import java.util.Formatter;

public class TimeSlot {
    String start_hour;
    String finish_hour;
    int id;

    public TimeSlot(int ts, String start_hour, String finish_hour) {
        this.start_hour = start_hour;
        this.finish_hour = finish_hour;
        this.id = ts;
    }

    public TimeSlot(String start_hour, String finish_hour) {
        this.start_hour = start_hour;
        this.finish_hour = finish_hour;
    }

    public int getId() {
        return id;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public String getFinish_hour() {
        return finish_hour;
    }

    public void printAllTimeSlots(Formatter fmt) {
        fmt.format("%-15d%-15s%-15s\n", id, start_hour, finish_hour);
    }
}