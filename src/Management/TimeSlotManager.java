package Management;

import Context.Reservation;
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
        timeSlotDao.insertTimeSlot(new TimeSlot(id, start_time, end_time));
    }

    public void deleteTimeSlot(int id) {
        List<Reservation> reservations = timeSlotDao.deleteTimeSlot(id);
        if (reservations != null)
            for (Reservation r : reservations) {
                if (r.getPrice() == 0)
                    Utils.sendEmail(r.getClient().getEmail(), "Important update for your reservation", "Time slot " + id + " is not longer available. Your reservation for this time slot is deleted and your gift points used are refunded. We are sorry for the inconvenience.");
                else
                    Utils.sendEmail(r.getClient().getEmail(), "Important update for your reservation", "Time slot " + id + " is not longer available. Your reservation for this time slot is deleted and your money has been refunded. We are sorry for the inconvenience.");
            }
    }
}
