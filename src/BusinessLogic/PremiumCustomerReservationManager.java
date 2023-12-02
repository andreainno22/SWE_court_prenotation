package BusinessLogic;

import java.text.DecimalFormat;
import java.util.Scanner;

public class PremiumCustomerReservationManager extends CustomerReservationManager {
    private final float discount = 0.9f;
    @Override
    public boolean makeReservation() {
        reservation.setIsPremium(1);
        if (reservation.getCustomer().getPoints() >= giftPoints) {
            System.out.println("Congrats! You reached " + giftPoints + " points so you are eligible for a free booking. Do you want to use these for this booking? (y/N)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.next();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                System.out.println("You have used " + giftPoints + " points for this booking.");
                System.out.println("Final price: 0€");
                reservation.getCustomer().setPoints(reservation.getCustomer().getPoints() - giftPoints);
                reservation.setPrice(0);
                return reservationDao.insertReservation(reservation, true, false);
            }
        }
        float price;
        DecimalFormat df = new DecimalFormat("#.##");
        price = reservation.getPrice() * discount;
        df.format(price);
        boolean isPremium = reservation.getCustomer().getIsPremium() == 1;
        System.out.println("You will earn " + reservationPoints + " points for this booking.");
        reservation.getCustomer().setPoints(reservation.getCustomer().getPoints() + reservationPoints);
        return super.makeReservation(price, isPremium);
    }
}
