package DAOs;

import DomainModel.Reservation;
import DomainModel.TimeSlot;

import java.sql.Date;
import java.util.List;

public interface TimeSlotDao extends Dao {
    List<TimeSlot> getTimeSlots(Date date, int court_id);
    void insertTimeSlot(TimeSlot timeSlot);
    List<Reservation> deleteTimeSlot(int id);
    List<TimeSlot> getAllTimeSlots();
}
