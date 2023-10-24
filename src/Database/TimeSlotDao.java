package Database;

import Context.Client;
import Context.TimeSlot;

import java.sql.Date;
import java.util.List;

public interface TimeSlotDao extends GeneralDaoSettings {
    List<TimeSlot> getTimeSlots(Date date, int court_id);

    void insertTimeSlot(int id, String start_time, String end_time);

    List<Client> deleteTimeSlot(int id);

    List<TimeSlot> getAllTimeSlots();
}
