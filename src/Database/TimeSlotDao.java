package Database;

import Context.TimeSlot;

import java.sql.Date;
import java.util.List;

public interface TimeSlotDao extends GeneralDaoSettings{
    List<TimeSlot> getTimeSlots(Date date, int court_id);
}
