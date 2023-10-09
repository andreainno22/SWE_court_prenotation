package Context;

import java.util.Formatter;

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

    public String getStart_hour() {
        return start_hour;
    }

    public String getFinish_hour() {
        return finish_hour;
    }

    public void printAllTimeSlots(Formatter fmt) {
        fmt.format("%-15d%-15s%-15s\n", ts, start_hour, finish_hour);
        //System.out.println(start_hour + " - " + finish_hour);
    }
}