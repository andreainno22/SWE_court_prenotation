package Database;

import Context.Client;
import Context.Court;

import java.util.List;

public interface CourtDao extends GeneralDaoSettings{
    List<Court> getCourts();
    void insertCourt(int id, String type);
    List<Client> deleteCourt(int id);
    String[] getTypes();
    void updatePrice(String id, float price);
    float getPrice(String type);
}
