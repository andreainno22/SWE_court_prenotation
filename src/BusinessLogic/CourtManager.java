package BusinessLogic;

import DomainModel.Court;
import DomainModel.Reservation;
import DAOs.CourtDao;
import DAOs.CourtDaoImpl;

import java.util.Formatter;
import java.util.List;

public class CourtManager {
    private final CourtDao courtDao = new CourtDaoImpl();

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
        List<Reservation> reservations = courtDao.deleteCourt(id);
        if (reservations != null)
            for (Reservation r : reservations) {
                if (r.getPrice() == 0)
                    Utils.sendEmail(r.getCustomer().getEmail(), "Important update for your reservation", "Court " + id + " is not longer available. Your reservation for this court is deleted and your gift points used are refunded. We are sorry for the inconvenience.");
                else
                    Utils.sendEmail(r.getCustomer().getEmail(), "Important update for your reservation", "Court " + id + " is not longer available. Your reservation for this court is deleted and your money has been refunded. We are sorry for the inconvenience.");
            }
    }

    public void updatePrice(String type, float price) {
        courtDao.updatePrice(type, price);
    }
}