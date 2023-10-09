package Management;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import Context.Client;
import Context.Court;
import Context.RentingKit;
import Context.Reservation;
import Database.Database_management;

import java.sql.Date;


public class AccountManager {
    private boolean logged = false;

    private Date isPremiumDate;
    private boolean startMenu = true;

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        // Crea un oggetto Pattern basato sulla regex
        Pattern pattern = Pattern.compile(regex);

        // Crea un oggetto Matcher per confrontare l'indirizzo email con il modello
        Matcher matcher = pattern.matcher(email);

        // Restituisce true se l'indirizzo email corrisponde al modello regex
        return matcher.matches();
    }


    public void startMenu() {
        while (startMenu) {
            System.out.println("\nHello! Please login or register to continue.");
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice;
            Scanner scanner;
            while (true) {
                try {
                    scanner = new Scanner(System.in);
                    choice = scanner.nextInt();
                    break;
                } catch (InputMismatchException ex) {
                    System.err.println("Wrong choice format.Retry.");
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
            Scanner sc = new Scanner(System.in);
            System.out.println("Name: ");
            String name = sc.nextLine();
            System.out.println("Surname: ");
            String surname = sc.nextLine();
            System.out.println("Email: ");
            String email = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();
            Client client = new Client(name, surname, email, password);
            Database_management db = new Database_management();
            int telephoneNumber;
            boolean telephoneNumberValid = false;
            while (!telephoneNumberValid) {
                try {
                    System.out.println("Telephone number [0 = not provided]: ");
                    sc = new Scanner(System.in);
                    telephoneNumber = sc.nextInt();
                    if (telephoneNumber != 0)
                        client.setTelephoneNumber(telephoneNumber);
                    telephoneNumberValid = true;
                } catch (InputMismatchException e) {
                    System.err.println("Wrong telephone number format. Retry.");
                }
            }
            while (!valid) {
                // fatto controllo sulla validità dell'email
                if (!isValidEmail(email) || db.insertClient(client) == -1) {
                    System.err.println("Email already used or wrong email format. Retry.");
                    System.out.println("Type another email: ");
                    email = sc.next();
                    client.setEmail(email);
                } else {
                    valid = true;
                }
            }
            MailManager mailManager = new MailManager();
            if (mailManager.createAndSendEmailMessage(client.getEmail(), "Registration successful", "Hi, " + client.getName() + " " + client.getSurname() + ", welcome to Court prenotation manager." + " Thank you for registering to our service!"))
                System.out.println("A confirmation email has been sent.");
            else
                System.err.println("Cannot send a confirmation email.");
            System.out.println("Registration successful.");
            System.out.println("You can now login.\n");
            startMenu = true;
        }
    }

    public void loginAccount() {
        while (!startMenu) {
            while (!logged) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Email: ");
                String email = sc.nextLine();
                System.out.println("Password: ");
                String password = sc.nextLine();
                Database_management db = new Database_management();
                Client client = db.getClient(email, password);
                if (client != null) {
                    logged = true;
                    System.out.println("Login successful.\n");
                    while (logged) {
                        clientMenu(client);
                    }
                    startMenu = true;
                    return;
                } else {
                    System.out.println("Login Failed. Retry? (Y/n)");
                    sc = new Scanner(System.in);
                    String value = sc.next();
                    if (value.equalsIgnoreCase("no") || value.equalsIgnoreCase("n")) {
                        startMenu = true;
                        return;
                    }
                }
            }
        }
    }

    public Client updateClient(Client client) {
        Database_management db = new Database_management();
        return db.getClient(client.getEmail(), client.getPassword());
    }

    public boolean topUpWallet(Client client, float money) {
        Database_management db = new Database_management();
        client.getWallet().addMoney(money);
        return db.modifyBalance(client, null);
    }

    private void clientMenu(Client client) {
        client = updateClient(client);
        System.out.println("\nHello " + client.getName() + " " + client.getSurname() + "!");
        if (client.getIsPremium() == 0) {
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
        Scanner sc;

        if (client.getIsPremium() == 0)
            client.setReservationManager(new StandardReservationManager());
        else
            client.setReservationManager(new PremiumReservationManager());

        while (true) {
            try {
                sc = new Scanner(System.in);
                choice = sc.nextInt();
                break;
            } catch (InputMismatchException ex) {
                System.err.println("Wrong choice format. Retry.");
            }
        }
        switch (choice) {
            case 1:
                Date date;
                int court = 0;
                Reservation res = new Reservation(client);
                System.out.println("Date (yyyy-mm-dd): ");
                try {
                    // fatto controllo sul fatto che la data non sia nel passato
                    date = Date.valueOf(sc.next());
                    int compare = date.compareTo(new Date(System.currentTimeMillis() - 86400000));
                    //todo: rendere non disponibili i giorni festivi con public holiday api
                    if (compare <= 0) {
                        System.err.println("You selected a past date. Retry.");
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
                List<Court> courts = client.getReservationManager().getCourt(fmt, client.getIsPremium() == 1);
                int num_courts = courts.size();
                while (court_selection) {
                    System.out.println("Available Courts: ");
                    System.out.println(fmt);
                    System.out.println("Select a Court [0 = Back to Main Menu]: ");
                    while (true) {
                        sc = new Scanner(System.in);
                        try {
                            court = sc.nextInt();
                            if (court > num_courts || court < 0) {
                                System.err.println("You selected a wrong Court. Retry.");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.err.println("Wrong court format. Retry.");
                        }
                    }
                    if (court == 0) {
                        break;
                    }
                    // aggiunta di court a reservation
                    res.setCourt(courts.get(court - 1));
                    Formatter fmt2 = new Formatter();
                    boolean[] available_slots = client.getReservationManager().getTimeSlots(fmt2, date, court);
                    while (true) {
                        System.out.println("Available Time Slots: ");
                        System.out.println(fmt2);
                        System.out.println("Select an option:");
                        System.out.println("1. Back to Court Selection\n2. Choose a time slot for this Court");
                        try {
                            choice = sc.nextInt();
                            break;
                        } catch (InputMismatchException e) {
                            System.err.println("Wrong choice format. Going back to Court Selection...");
                            break;
                        }
                    }
                    switch (choice) {
                        case 1:
                            //back = true;
                            break;
                        case 2:
                            int slot = 0;
                            System.out.println("ID of desired Time Slot [0 = Go Back to Court Selection]: ");
                            boolean valid = false;
                            while (!valid) {
                                try {
                                    slot = sc.nextInt();
                                    if (slot > available_slots.length || slot < 0) {
                                        System.err.println("Given Time Slot is wrong. Retry.");
                                        continue;
                                    }
                                    valid = true;
                                    if (slot != 0) {
                                        while (!available_slots[slot - 1]) {
                                            System.err.println("Given Time Slot is not available. Retry.");
                                            System.out.println("ID of desired Time Slot: ");
                                            slot = sc.nextInt();
                                        }
                                    }
                                } catch (InputMismatchException e) {
                                    System.err.println("Wrong input format. Retry");
                                    break;
                                }
                            }
                            if (slot == 0)
                                break;

                            // aggiunta time slot a reservation
                            res.setTime_slot(slot);
                            RentingKit rentingKit = client.getReservationManager().getRentingKit(res.getCourt().getType());
                            if(client.getIsPremium()==0)
                                System.out.println("How many renting kit do you want to rent? [Unit price = " + rentingKit.getUnitPrice() + "€] [0 = None]");
                            else
                                System.out.println("How many renting kit do you want to rent? [Unit price = " + rentingKit.getUnitPrice() + "€. Your price (-10%) = " + rentingKit.getUnitPrice()*0.9 + "€] [0 = None]");
                            int numOfRent = sc.nextInt();

                            // aggiunta renting kit a reservation
                            if (numOfRent > 0) {
                                rentingKit.setNumOfRents(numOfRent);
                                res.setRentingKit(rentingKit);
                            } else if (numOfRent == 0)
                                res.setRentingKit(null);
                            else {
                                System.err.println("Operation aborted.");
                                break;
                            }

                            // aggiunta della prenotazione al database
                            System.out.println("Subtotal price: " + res.getPrice() + "€");
                            System.out.println("Making reservation...");
                            if (client.getReservationManager().makeReservation(res)) {
                                System.out.println("Reservation successful.");
                                MailManager mailManager = new MailManager();
                                if (mailManager.createAndSendEmailMessage(client.getEmail(), "Confirmation of reservation", "Your reservation has been made.\nDate and time of reservation: " + res.getDate() + " (UTC).\nCourt: " + res.getCourt().getId() +  "\nTime slot: " + res.getTime_slot() + "\nThank you for choosing us!"))
                                    System.out.println("A confirmation email has been sent.");
                                else
                                    System.err.println("Cannot send a confirmation email.");

                            } else System.err.println("Reservation failed.");
                            //todo: inserire un trigger per eliminare le prenotazioni scadute
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
                client.getReservationManager().printAllReservations(client);
                System.out.println("ID of reservation to delete: ");
                int reservation = 0;
                boolean valid = false;
                while (!valid)
                    try {
                        reservation = sc.nextInt();
                        valid = true;
                    } catch (InputMismatchException e) {
                        System.err.println("Wrong ID format. Retry.");
                    }
                ArrayList<Integer> ids = client.getReservationManager().getReservationsId(client);
                boolean found = false;
                for (int j : ids)
                    if (j == reservation) {
                        found = true;
                        break;
                    }
                if (found) {
                    Reservation reserv = client.getReservationManager().getReservationById(reservation);
                    if (client.getReservationManager().deleteReservation(reserv, client)) {
                        //client.getReservationManager().addMoney(client, client.getReservationManager().getReservationPrice(reservation));
                        System.out.println("Reservation deleted successfully.");
                    } else System.err.println("Error during deletion.");
                } else
                    System.err.println("Reservation not found.");
                break;
            case 3:
                // stampa delle prenotazioni
                client.getReservationManager().printAllReservations(client);
                break;
            case 4:
                // gestione del portafoglio
                System.out.println("Your balance is: " + client.getWallet().getBalance() + "€");
                System.out.println("Do you want to add money? [y/N]");
                String choice2 = sc.next();
                if (choice2.equalsIgnoreCase("y") || choice2.equalsIgnoreCase("yes")) {
                    float money;
                    try {
                        System.out.println("How much money do you want to add?");
                        money = sc.nextFloat();
                    } catch (InputMismatchException e) {
                        System.err.println("Wrong input format. Going back to Main Menu...");
                        break;
                    }
                    if (topUpWallet(client, money)) {
                        System.out.println("Money added successfully.");
                        String dateTime = getDateTimeUTC();
                        MailManager mailManager = new MailManager();
                        if (mailManager.createAndSendEmailMessage(client.getEmail(), "Confirmation of transaction", "Your wallet has been topped up.\nDate and time of transaction: " + dateTime + " (UTC).\nAmount: " + money + "€\nThank you for choosing us!"))
                            System.out.println("A confirmation email has been sent.");
                        else
                            System.err.println("Cannot send a confirmation email.");
                    } else
                        System.out.println("Transaction failed.");
                } else {
                    System.out.println("Operation aborted.");
                    System.err.println("Going back to Main Menu...");
                }
                break;
            case 5:
                if (client.getIsPremium() == 0) { // upgrade to premium
                    System.out.println("Do you want to upgrade to premium? The cost is 20€ for one year and then " +
                            "you can book your court with a\n 10% discount and you unlock a points system for getting bookings for free![y/N]");
                    String answer = sc.next();
                    if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                        if (setIsPremium(client)) {
                            System.out.println("Upgrade successful.");
                        } else {
                            System.err.println("Upgrade failed.");
                        }
                    } else {
                        System.out.println("Upgrade aborted.");
                        System.err.println("Going back to Main Menu...");
                    }
                } else { // manage premium subscription
                    showPremiumExpiration(client);
                    System.out.println("Do you want to renew your subscription? [y/N]");
                    sc = new Scanner(System.in);
                    String answer = sc.next();
                    if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                        if (renewPremium(client)) {
                            System.out.println("Renewal successful.");
                        } else {
                            System.err.println("Renewal failed.");
                        }
                    } else {
                        System.out.println("Renewal aborted.");
                        System.err.println("Going back to Main Menu...");
                    }
                }
                break;
            case 6:
                // gestione dei punti
                System.out.println("Your have: " + client.getPoints() + " points.");
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

    private boolean setIsPremium(Client client) {
        Database_management db = new Database_management();

        if (client.getWallet().removeMoney(20)) {
            client.setIsPremium(1);
            return db.modifyPremium(client);
        } else {
            System.err.println("Insufficient funds.");
            return false;
        }
    }

    private boolean renewPremium(Client client) {
        Database_management db = new Database_management();
        if (client.getWallet().removeMoney(20))
            return db.modifyPremiumExpiration(client);
        else {
            System.err.println("Insufficient funds.");
            return false;
        }
    }

    private void showPremiumExpiration(Client client) {
        Database_management db = new Database_management();
        isPremiumDate = db.getPremiumExpiration(client);
        System.out.println("Your premium subscription will expire on: " + isPremiumDate);
    }

    private String getDateTimeUTC() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Instant instant = Instant.now();
        ZoneId zone = ZoneId.of("UTC");
        String dateTime = instant.atZone(zone).format(dtf);
        return dateTime;
    }

}


