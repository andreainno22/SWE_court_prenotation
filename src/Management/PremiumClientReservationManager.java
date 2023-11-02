package Management;

import java.text.DecimalFormat;
import java.util.Scanner;

public class PremiumClientReservationManager extends ClientReservationManager {

    @Override
    public boolean makeReservation() {
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
                return reservationDao.makeReservation(reservation, true, false);
            }
        }
        float price;
        DecimalFormat df = new DecimalFormat("#.##");
        //Premium Discount 10%
        float discount = 0.9f;
        price = reservation.getPrice() * discount;
        df.format(price);
        boolean isPremium = reservation.getClient().getIsPremium() == 1;
        System.out.println("You will earn " + reservationPoints + " points for this booking.");
        reservation.getClient().setPoints(reservation.getClient().getPoints() + reservationPoints);
        return super.makeReservation(price, isPremium);
    }
}
