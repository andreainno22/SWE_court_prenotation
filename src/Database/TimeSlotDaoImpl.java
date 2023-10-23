package Database;

import Context.TimeSlot;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDaoImpl implements TimeSlotDao{
    //private final DatabaseManager db = new DatabaseManager();

    @Override
    public List<TimeSlot> getTimeSlots(Date date, int court_id) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT * FROM time_slots WHERE time_slots.id NOT IN(SELECT time_slots.id as ts FROM court JOIN reservation ON reservation.court = court.id JOIN time_slots ON time_slots.id = reservation.time_slot WHERE court.id = '" + court_id + "' AND reservation.date = '" + date + "')");
            List<TimeSlot> timeSlots = new ArrayList<>();
            int id = 1;
            while (rs.next()) {
                if (rs.getInt(1) != id) {
                    do {
                        timeSlots.add(null);
                        id++;
                    } while (rs.getInt(1) != id);
                }
                timeSlots.add(new TimeSlot(rs.getInt(1), rs.getString(2), rs.getString(3)));
                id++;
            }
            rs.close();
            return timeSlots;
        } catch (SQLException e) {
            db.dbError(e);
            return null;
        } finally {
            db.disconnect();
        }
    }
}
