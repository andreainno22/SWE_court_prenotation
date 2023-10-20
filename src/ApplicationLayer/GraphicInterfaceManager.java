package ApplicationLayer;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.*;

import Management.*;
import de.jollyday.*;
import Context.*;
import Database.ClientDaoImpl;

import java.sql.Date;

public class GraphicInterfaceManager {
    private boolean logged = false;
    private boolean startMenu = true;
    private final Scanner sc = new Scanner(System.in);
    //private Client client;
    private final AccountManager accountManager = new AccountManager();
    private final WalletManager walletManager = new WalletManager();

    public void startMenu() {
        while (startMenu) {
            System.out.println("\nHello! Please login or register to continue.");
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice;
            while (true) {
                try {
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (InputMismatchException ex) {
                    System.err.println("Wrong choice format.Retry.");
                    sc.nextLine();
                }
            }
            switch (choice) {
                case 1:
                    startMenu = false;
                    loginAccount();
                    break;
                case 2:
                    startMenu = false;
                    registerAccount();
                    break;
                case 3:
                    startMenu = false;
                    break;
                default:
                    System.err.println("Wrong choice.");
                    break;
            }
        }
    }

    public void registerAccount() {
        while (!startMenu) {
            boolean valid = false;
            System.out.println("Name: ");
            String name = sc.nextLine();
            System.out.println(name);
            System.out.println("Surname: ");
            String surname = sc.nextLine();
            System.out.println("Email: ");
            String email = sc.nextLine();
            System.out.println(email);
            System.out.println("Password: ");
            String password = sc.nextLine();
            Client newClient = new Client(name, surname, email, password);
            //Database_management db = new Database_management();
            int telephoneNumber;
            boolean telephoneNumberValid = false;
            while (!telephoneNumberValid) {
                try {
                    System.out.println("Telephone number [0 = not provided]: ");
                    telephoneNumber = sc.nextInt();
                    sc.nextLine();
                    if (telephoneNumber != 0) newClient.setTelephoneNumber(telephoneNumber);
                    telephoneNumberValid = true;
                } catch (InputMismatchException e) {
                    System.err.println("Wrong telephone number format. Retry.");
                    sc.nextLine();
                }
            }
            while (!valid) {
                // fatto controllo sulla validità dell'email
                int result;
                if (!Utils.isValidEmail(email) || (result = accountManager.register(newClient)) == -1) {
                    System.err.println("Email already used or wrong email format. Retry.");
                    System.out.println("Type another email: [0 = Go Back]");
                    email = sc.nextLine();
                    if (email.equals("0")) break;
                    newClient.setEmail(email);
                } else if(result == 0){
                    valid = true;
                } else {
                    System.err.println("Error during registration. Retry.");
                    break;
                }
            }
            if (valid) {
                Utils.sendEmail(newClient.getEmail(), "Registration successful", "Hi, " + newClient.getName() + " " + newClient.getSurname() + "!\nWelcome to Court Prenotation Manager." + "\nThank you for registering to our service!");
                System.out.println("Registration successful.");
                System.out.println("You can now login.\n");
                startMenu = true;
            } else {
                startMenu = true;
                System.out.println("Registration cancelled.");
            }
        }
    }

    public void loginAccount() {
        while (!startMenu) {
            while (!logged) {
                System.out.println("Email: ");
                String email = sc.nextLine();
                System.out.println("Password: ");
                String password = sc.nextLine();
                //Database_management db = new Database_management();
                accountManager.login(email, password);
                if (accountManager.client != null) {
                    logged = true;
                    System.out.println("Login successful.\n");
                    while (logged) {
                        clientMenu();
                    }
                    startMenu = true;
                    return;
                } else {
                    System.out.println("Login Failed. Retry? (Y/n)");
                    String value = sc.nextLine();
                    if (value.equalsIgnoreCase("no") || value.equalsIgnoreCase("n")) {
                        startMenu = true;
                        return;
                    }
                }
            }
        }
    }

    private void clientMenu() {
        accountManager.updateClient();
        System.out.println("\nHello " + accountManager.client.getName() + " " + accountManager.client.getSurname() + "!");
        if (accountManager.client.getIsPremium() == 0) {
            System.out.println("You are not subscribed to Premium.");
            System.out.println("Please select an option:");
            System.out.println("""
                    1. Make a reservation
                    2. Delete a reservation
                    3. Show all reservations
                    4. Manage your wallet
                    5. Upgrade as a premium client
                    6. Your points
                    7. Logout""");
        } else {
            System.out.println("You are subscribed to Premium.");
            System.out.println("Please select an option:");
            System.out.println("""
                    1. Make a reservation
                    2. Delete a reservation
                    3. Show all reservations
                    4. Manage your wallet
                    5. Manage your premium subscription
                    6. Your points
                    7. Logout""");
        }
        int choice;

        if (accountManager.client.getIsPremium() == 0) accountManager.client.setReservationManager(new StandardReservationManager());
        else accountManager.client.setReservationManager(new PremiumReservationManager());

        while (true) {
            try {
                choice = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException ex) {
                System.err.println("Wrong choice format. Retry.");
                sc.nextLine();
            }
        }
        switch (choice) {
            case 1:
                Date date;
                int court = 0;
                Reservation res = new Reservation(accountManager.client);
                System.out.println("Date (yyyy-mm-dd): ");
                try {
                    // fatto controllo sul fatto che la data non sia nel passato
                    date = Date.valueOf(sc.nextLine());
                    ZoneId italyZone = ZoneId.of("Europe/Rome");
                    // Crea una data nel fuso orario italiano
                    LocalDate italianDate = LocalDate.now(italyZone);
                    java.util.Date italianZonedDate = Date.from(italianDate.atStartOfDay().atZone(italyZone).toInstant());

                    if (!date.after(italianZonedDate)) {
                        System.err.println("You can book at least for tomorrow. Retry.");
                        break;
                    }
                    HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.ITALY);
                    if (holidayManager.isHoliday(date.toLocalDate())) {
                        System.err.println("You can't book on a holiday. Retry.");
                        break;
                    }
                    // aggiunta data a reservation
                    res.setDate(date);
                } catch (IllegalArgumentException e) {
                    System.err.println("Wrong date format.");
                    break;
                }
                boolean court_selection = true;
                Formatter fmt = new Formatter();
                List<Court> courts = accountManager.client.getReservationManager().getCourt(fmt, accountManager.client.getIsPremium() == 1);
                int num_courts = courts.size();
                while (court_selection) {
                    System.out.println("Available Courts: ");
                    System.out.println(fmt);
                    System.out.println("Select a Court [0 = Back to Main Menu]: ");
                    while (true) {
                        try {
                            court = sc.nextInt();
                            sc.nextLine();
                            if (court > num_courts || court < 0) {
                                System.err.println("You selected a wrong Court. Retry.");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.err.println("Wrong court format. Retry.");
                            sc.nextLine();
                        }
                    }
                    if (court == 0) {
                        break;
                    }
                    // aggiunta di court a reservation
                    res.setCourt(courts.get(court - 1));
                    Formatter fmt2 = new Formatter();
                    List<TimeSlot> available_slots = accountManager.client.getReservationManager().getTimeSlots(fmt2, date, court);
                    while (true) {
                        System.out.println("Available Time Slots: ");
                        System.out.println(fmt2);
                        System.out.println("Select an option:");
                        System.out.println("1. Back to Court Selection\n2. Choose a time slot for this Court");
                        try {
                            choice = sc.nextInt();
                            sc.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.err.println("Wrong choice format. Going back to Court Selection...");
                            sc.nextLine();
                            break;
                        }
                    }
                    switch (choice) {
                        case 1:
                            break;
                        case 2:
                            int slot = 0;
                            System.out.println("ID of desired Time Slot [0 = Go Back to Court Selection]: ");
                            boolean valid = false;
                            while (!valid) {
                                try {
                                    slot = sc.nextInt();
                                    sc.nextLine();
                                    if (slot > available_slots.size() || slot < 0) {
                                        System.err.println("Given Time Slot is wrong. Retry.");
                                        continue;
                                    }
                                    valid = true;
                                    if (slot != 0) {
                                        while (available_slots.get(slot - 1) == null) {
                                            System.err.println("Given Time Slot is not available. Retry.");
                                            System.out.println("ID of desired Time Slot: ");
                                            slot = sc.nextInt();
                                            sc.nextLine();
                                        }
                                    }
                                } catch (InputMismatchException e) {
                                    System.err.println("Wrong input format. Retry");
                                    sc.nextLine();
                                    break;
                                }
                            }
                            if (slot == 0) break;

                            // aggiunta time slot a reservation
                            TimeSlot ts = available_slots.get(slot - 1);
                            res.setTime_slot(ts);
                            RentingKit rentingKit = accountManager.client.getReservationManager().getRentingKit(res.getCourt().getType());
                            if (accountManager.client.getIsPremium() == 0)
                                System.out.println("How many " + rentingKit.getType() + " kits do you want to rent? [Unit price = " + rentingKit.getUnitPrice() + "€] [0 = None]");
                            else
                                System.out.println("How many " + rentingKit.getType() + " kits do you want to rent? [Unit price = " + rentingKit.getUnitPrice() + "€. Your price (-10%) = " + rentingKit.getUnitPrice() * 0.9 + "€] [0 = None]");
                            boolean rentIsValid = false;
                            while (!rentIsValid) {
                                try {
                                    int numOfRent = sc.nextInt();
                                    sc.nextLine();
                                    rentIsValid = true;
                                    // aggiunta renting kit a reservation
                                    if (numOfRent > 0) {
                                        rentingKit.setNumOfRents(numOfRent);
                                        res.setRentingKit(rentingKit);
                                    } else if (numOfRent == 0) res.setRentingKit(null);
                                } catch (InputMismatchException e) {
                                    System.err.println("Wrong input format. Retry");
                                    sc.nextLine();
                                }
                            }
                            // aggiunta della prenotazione al database
                            System.out.println("Subtotal price: " + String.format("%.2f", res.getPrice(accountManager.client)) + "€");
                            System.out.println("Making reservation...");
                            if (accountManager.client.getReservationManager().makeReservation(res)) {
                                System.out.println("Reservation successful.");
                                Utils.sendEmail(accountManager.client.getEmail(), "Confirmation of reservation", "Your reservation has been made.\nDate of reservation: " + res.getDate() + "\nCourt: " + res.getCourt().getId() + "\nTime slot: " + res.getTime_slot().getStart_hour() + "-" + res.getTime_slot().getFinish_hour() + "\nThank you for choosing us!");
                            } else System.err.println("Reservation failed.");
                            court_selection = false;
                            System.out.println("Going back to Main Menu...\n");
                            break;
                        default:
                            System.err.println("Wrong choice. Going back to Court Selection...");
                            break;
                    }
                }
                break;
            case 2:
                // gestione della cancellazione della prenotazione
                accountManager.client.getReservationManager().printAllFutureReservations(accountManager.client);
                System.out.println("Note: you can delete your reservation by the day before the booking date!");
                System.out.println("ID of reservation to delete: [0 to go back] ");
                int reservation = 0;
                boolean valid = false;
                while (!valid) try {
                    reservation = sc.nextInt();
                    sc.nextLine();
                    valid = true;
                } catch (InputMismatchException e) {
                    System.err.println("Wrong ID format. Retry.");
                    sc.nextLine();
                }
                if (reservation == 0) break;
                ArrayList<Integer> ids = accountManager.client.getReservationManager().getReservationsId(accountManager.client);
                boolean found = false;
                for (int j : ids)
                    if (j == reservation) {
                        found = true;
                        break;
                    }
                if (found) {
                    Reservation reserv = accountManager.client.getReservationManager().getReservationById(reservation);
                    if (accountManager.client.getReservationManager().deleteReservation(reserv, accountManager.client)) {
                        System.out.println("Reservation deleted successfully.");
                        Utils.sendEmail(accountManager.client.getEmail(), "Cancellation of reservation", "Your reservation has been cancelled.\nDate and time of reservation: " + reserv.getDate() + "\nCourt: " + reserv.getCourt().getId() + "\nTime slot: " + reserv.getTime_slot().getStart_hour() + "-" + reserv.getTime_slot().getFinish_hour() + "\nThank you for choosing us!");
                    } else System.err.println("Error during deletion.");
                } else System.err.println("Reservation not found or non-cancellable.");
                break;
            case 3:
                // stampa delle prenotazioni
                accountManager.client.getReservationManager().printAllReservations(accountManager.client);
                break;
            case 4:
                // gestione del portafoglio
                System.out.println("Your balance is: " + accountManager.client.getWallet().getBalance() + "€");
                System.out.println("Do you want to add money? [y/N]");
                String choice2 = sc.nextLine();
                if (choice2.equalsIgnoreCase("y") || choice2.equalsIgnoreCase("yes")) {
                    float money;
                    try {
                        System.out.println("How much money do you want to add?");
                        money = sc.nextFloat();
                        sc.nextLine();
                    } catch (InputMismatchException e) {
                        System.err.println("Wrong input format. Going back to Main Menu...");
                        break;
                    }
                    if (walletManager.topUpWallet(money, accountManager.client)) {
                        System.out.println("Money added successfully.");
                        String dateTime = Utils.getDateTimeUTC();
                        Utils.sendEmail(accountManager.client.getEmail(), "Confirmation of transaction", "Your wallet has been topped up.\nDate and time of transaction: " + dateTime + " (UTC).\nAmount: " + money + "€\nThank you for choosing us!");
                    } else System.out.println("Transaction failed.");
                } else {
                    System.out.println("Operation aborted.");
                    System.err.println("Going back to Main Menu...");
                }
                break;
            case 5:
                if (accountManager.client.getIsPremium() == 0) { // upgrade to premium
                    System.out.println("Do you want to upgrade to premium? The cost is 20€ for one year and then " + "you can book your court with a\n 10% discount and you unlock a points system for getting bookings for free![y/N]");
                    String answer = sc.nextLine();
                    if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                        if (accountManager.setIsPremium(accountManager.client)) {
                            System.out.println("Upgrade successful.");
                            Utils.sendEmail(accountManager.client.getEmail(), "Premium Subscription", "Your account has been upgraded to Premium.\nThank you for choosing us!");
                        } else {
                            System.err.println("Upgrade failed.");
                        }
                    } else {
                        System.out.println("Upgrade aborted.");
                        System.out.println("Going back to Main Menu...");
                    }
                } else {
                    // manage premium subscription
                    accountManager.showPremiumExpiration(accountManager.client);
                    System.out.println("Do you want to renew your subscription? [y/N]");
                    String answer = sc.nextLine();
                    if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                        if (accountManager.renewPremium(accountManager.client)) {
                            System.out.println("Renewal successful.");
                            Utils.sendEmail(accountManager.client.getEmail(), "Premium Subscription", "Your premium subscription has been renewed.\nThank you for choosing us!");
                        } else {
                            System.err.println("Renewal failed.");
                        }
                    } else {
                        System.out.println("Renewal aborted.");
                        System.out.println("Going back to Main Menu...");
                    }
                }
                break;
            case 6:
                // gestione dei punti
                System.out.println("Your have: " + accountManager.client.getPoints() + " points.");
                break;
            case 7: {
                logged = false;
                System.out.println("Logout successful.\n");
                break;
            }
            default: {
                System.err.println("Wrong choice.");
                break;
            }
        }
    }
}