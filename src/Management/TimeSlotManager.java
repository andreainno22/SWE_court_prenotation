package Management;

import Context.Client;
import Context.TimeSlot;
import Database.TimeSlotDaoImpl;

import java.util.Formatter;
import java.util.List;

public class TimeSlotManager {
    private final TimeSlotDaoImpl timeSlotDao = new TimeSlotDaoImpl();

    public List<TimeSlot> printTimeSlots() {
        List<TimeSlot> timeSlots = timeSlotDao.getAllTimeSlots();
        Formatter fmt = new Formatter();
        fmt.format("%-10s%-15s%-15s\n", "ID", "START HOUR", "END HOUR");
        for (TimeSlot timeSlot : timeSlots) {
            timeSlot.printAllTimeSlots(fmt);
        }
        System.out.println(fmt);
        return timeSlots;
    }

    public void insertTimeSlot(int id, String start_time, String end_time) {
        timeSlotDao.insertTimeSlot(id, start_time, end_time);
    }

    public void deleteTimeSlot(int id) {
        List<Client> clients = timeSlotDao.deleteTimeSlot(id);
        if (clients != null)
            for (Client c : clients) {
            Utils.sendEmail(c.getEmail(), "Time slot " + id + " is not longer available.", "Time slot " + id + " is not longer available. All your reservation for this time slot are deleted and your money has been refunded. We are sorry for the inconvenience.");
        }
    }
}
