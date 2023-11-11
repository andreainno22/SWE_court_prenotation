package Database;

import Context.Reservation;
import Context.TimeSlot;

import java.sql.Date;
import java.util.List;

public interface TimeSlotDao extends Dao {
    List<TimeSlot> getTimeSlots(Date date, int court_id);
    void insertTimeSlot(int id, String start_time, String end_time);
    List<Reservation> deleteTimeSlot(int id);
    List<TimeSlot> getAllTimeSlots();
}
