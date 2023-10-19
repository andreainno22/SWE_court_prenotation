package Management;

import Context.Reservation;
import Database.ReservationDaoImpl;

import java.text.DecimalFormat;
import java.util.Scanner;

public class PremiumReservationManager extends ReservationManager {
    private final float discount = 0.9f;

    @Override
    public boolean makeReservation(Reservation reservation) {

        reservation.setIsPremium(1);
        if (reservation.getClient().getPoints() >= giftPoints) {
            System.out.println("Congrats! You reached " + giftPoints + " points so you are eligible for a free booking. Do you want to use these for this booking? (y/N)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.next();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                System.out.println("You have used " + giftPoints + " points for this booking.");
                System.out.println("Final price: 0€");
                reservation.getClient().setPoints(reservation.getClient().getPoints() - giftPoints);
                reservation.setPrice(0);
                //Database_management db = new Database_management();
                ReservationDaoImpl reservationDao = new ReservationDaoImpl();
                return reservationDao.makeReservation(reservation, true, false);
            }
        }
        float price;
        DecimalFormat df = new DecimalFormat("#.##");
        price = reservation.getPrice() * discount;
        df.format(price);
        boolean isPremium = reservation.getClient().getIsPremium() == 1;
        reservation.getClient().setPoints(reservation.getClient().getPoints() + reservationPoints);
        System.out.println("You will earn " + reservationPoints + " points for this booking.");
        return super.makeReservation(reservation, price, isPremium);

    }
}
