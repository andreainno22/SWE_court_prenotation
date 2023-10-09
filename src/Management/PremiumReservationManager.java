package Management;

import Context.Client;
import Context.Reservation;
import Database.Database_management;

import java.util.Scanner;

public class PremiumReservationManager extends ReservationManager {

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
                Database_management db = new Database_management();
                return db.makeReservation(reservation, true, false);
            }
        }
        float price;
        price = reservation.getPrice() * 0.9f;
        boolean isPremium = reservation.getClient().getIsPremium() == 1;
        reservation.getClient().setPoints(reservation.getClient().getPoints() + reservationPoints);
        System.out.println("You have earned " + reservationPoints + " points for this booking.");
        return super.makeReservation(reservation, price, isPremium);
        //Database_management db = new Database_management();
        //db.updatePoints(reservation.getClient().getPoints(), reservation.getClient(), );
    }


    //protected boolean deleteReservationPremium(Reservation reservation, Client client) {
    // client.setPoints(client.getPoints() - reservationPoints);
    //  return (super.deleteReservation(reservation, client));
    //  Database_management db = new Database_management();
    // db.updatePoints(client.getPoints() - giftPoints, client);
    //  return true;
    // }
    //return false;
    //}


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
