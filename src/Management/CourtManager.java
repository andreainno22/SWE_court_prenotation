package Management;

import Context.Client;
import Context.Court;
import Database.CourtDaoImpl;

import java.util.Formatter;
import java.util.List;

public class CourtManager {
    private final CourtDaoImpl courtDao = new CourtDaoImpl();

    public List<Court> printCourts() {
        List<Court> courts;
        Formatter fmt = new Formatter();
        fmt.format("%-15s%-15s%-15s%-15s\n", "ID", "TYPE", "PRICE [€]", "DISCOUNTED PRICE [€]");
        courts = courtDao.getCourts();
        for (Court court : courts) {
            court.printAllCourts(fmt, true);
        }
        System.out.println(fmt);
        return courts;
    }

    public String[] getTypes() {
        return courtDao.getTypes();
    }

    public void insertCourt(int id, String type) {
        courtDao.insertCourt(id, type);
    }

    public void deleteCourt(int id) {
        List<Client> clients = courtDao.deleteCourt(id);
        if (clients != null)
            for (Client c : clients) {
                Utils.sendEmail(c.getEmail(), "Court " + id + " is not longer available.", "Court" + id + " is not longer available. All your reservation for this court are deleted and your money has been refunded. We are sorry for the inconvenience.");
            }
    }

    public void updatePrice(String type, float price) {
        courtDao.updatePrice(type, price);
    }
}
