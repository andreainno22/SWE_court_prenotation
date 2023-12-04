package DAOs;

import DomainModel.Court;
import DomainModel.Reservation;

import java.util.List;

public interface CourtDao extends Dao {
    List<Court> getCourts();
    void insertCourt(int id, String type);
    List<Reservation> deleteCourt(int id);
    String[] getTypes();
    void updatePrice(String id, float price);
    float getPrice(String type);
}