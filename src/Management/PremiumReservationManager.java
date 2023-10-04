package Management;

import Context.Client;
import Context.Reservation;
import Database.Database_management;

import java.util.Scanner;

public class PremiumReservationManager extends ReservationManager {

    private final int giftPoints = 100;

    @Override
    public boolean makeReservation(Reservation reservation) {

        if (reservation.getClient().getPoints() >= giftPoints) {
            System.out.println("Congrats! You reached " + giftPoints + " points so you are eligible for a free booking. Do you want to use these for this booking? (y/N)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.next();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                reservation.getClient().setPoints(reservation.getClient().getPoints() - giftPoints);
                Database_management db = new Database_management();
                if (db.makeReservation(reservation)) {
                    db.updatePoints(reservation.getClient().getPoints(), reservation.getClient());
                    return true;
                }
            }
        }
        float price;
        if(reservation.getRentingKit() != null)
            price = 90 * (reservation.getCourt().getPrice() + reservation.getRentingKit().getTotPrice()) / 100;
        else price = 90 * reservation.getCourt().getPrice() / 100;
        if (super.makeReservation(reservation, price)) {
            Database_management db = new Database_management();
            db.updatePoints(reservation.getClient().getPoints(), reservation.getClient());
            return true;
        }
        return false;

    }

    @Override
    public void editReservation(Reservation reservation) {
    }

    public boolean deleteReservation(int reservation, Client client) {
        if (super.deleteReservation(reservation, client)) {
            Database_management db = new Database_management();
            db.updatePoints(client.getPoints() - giftPoints, client);
            return true;
        }
        return false;
        // todo: togliere i punti della prenotazione
    }


    /*@Override
    public void getTimeSlots(Date date, int court) {
        Database_management db = new Database_management();
        List<TimeSlot> time_slots = db.getTimeSlots(date, court);
        Formatter fmt = new Formatter();
        fmt.format("%-15s%-15s%-15s\n", "ID", "START", "END");
        for (TimeSlot timeSlot : time_slots) {
            timeSlot.printAllTimeSlots(fmt);
        }
        System.out.println(fmt);
    }*/

}
