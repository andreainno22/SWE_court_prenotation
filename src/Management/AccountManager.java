package Management;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import Context.Client;
import Context.Reservation;
import Database.Database_management;

import java.sql.Date;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.Scanner;


public class AccountManager {
    private boolean logged = false;

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
            // todo: fare in modo che venga inviata una email a ogni azione compiuta dall'utente
            String email = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();
            System.out.println("Telephone number [0 = not provided]: ");
            int telephoneNumber = sc.nextInt();
            Client client = new Client(name, surname, email, password);
            if (telephoneNumber != 0)
                client.setTelephoneNumber(telephoneNumber);
            Database_management db = new Database_management();

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
                    System.err.println("Wrong email or password. Retry.");
                }
            }
        }
    }


    private void clientMenu(Client client) {
        System.out.println("\nHello " + client.getName() + " " + client.getSurname() + "!");
        System.out.println("Please select an option:");
        System.out.println("1. Make a reservation\n2. Edit a reservation\n3. Delete a reservation\n" +
                "4. Show all reservations\n6. Manage your wallet\n7. Upgrade as a premium client\n8. Logout");
        int choice;
        Scanner sc;
        while (true) {
            try {
                sc = new Scanner(System.in);
                choice = sc.nextInt();
                break;
            } catch (InputMismatchException ex) {
                System.err.println("Wrong choice format. Retry.");
            }
        }
        if (client.getReservationManager() == null) {
            if (client.getIsPremium() == 1)
                client.setReservationManager(new PremiumReservationManager());
            else
                client.setReservationManager(new StandardReservationManager());
        }
        switch (choice) {
            case 1:
                Date date;
                int court = 0;
                Reservation res= new Reservation();
                System.out.println("Date (yyyy-mm-dd): ");
                try {
                    // fatto controllo sul fatto che la data non sia nel passato
                    date = Date.valueOf(sc.next());
                    int compare = date.compareTo(new Date(System.currentTimeMillis()-86400000));
                    //todo: rendere non disponibili i giorni festivi con public holiday api
                    if (compare <= 0) {
                        System.err.println("You selected a past date. Retry.");
                        break;
                    }
                    res.setDate(date);
                } catch (IllegalArgumentException e) {
                    System.err.println("Wrong date format.");
                    break;
                }
                boolean court_selection = true;
                Formatter fmt = new Formatter();
                int num_courts = client.getReservationManager().getCourt(fmt);
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
                    res.setCourt_id(court);
                    Formatter fmt2 = new Formatter();
                    boolean[] available_slots = client.getReservationManager().getTimeSlots(fmt2, date, court);
                    while (true) {
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
                                    if(slot != 0) {
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
                            if(slot == 0)
                                break;
                            res.setTime_slot(slot);
                            System.out.println("How many renting kit do you want to rent? [0 = None]");
                            int rentingKits = sc.nextInt();
                            //todo: fare in modo che il kit venga aggiunto alla prenotazione
                            //TODO: fare la prenotazione
                            //todo: inserire un trigger per eliminare le prenotazioni scadute
                            court_selection = false;
                            System.out.println("Reservation successful.");
                            System.out.println("Going back to Main Menu...\n");
                            break;
                        default:
                            System.err.println("Wrong choice. Going back to Court Selection...");
                            break;
                    }
                }
                break;
            case 3:
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
                int[] ids = client.getReservationManager().getReservationsId(client);
                boolean found = false;
                for (int j : ids)
                    if (j == reservation) {
                        found = true;
                        break;
                    }
                if (found) {
                    client.getReservationManager().deleteReservation(reservation);
                    client.getReservationManager().addMoney(client, client.getReservationManager().getReservationPrice(reservation));
                    System.out.println("Reservation deleted successfully.");
                }
                else
                    System.err.println("Reservation not found.");
                break;
            case 4:
                // stampa delle prenotazioni
                client.getReservationManager().printAllReservations(client);
                break;
            default: {
                System.err.println("Wrong choice.");
                break;
            }
            case 7:
                // upgrade a premium
                if (client.getIsPremium() == 0) {
                    System.out.println("Do you want to upgrade to premium? The cost is 20€ a year and then " +
                            "you can book your court with a\n 20% discount and for every 15 bookings you will get one free![y/n]");
                    String answer = sc.next();
                    if (answer.equals("y")) {
                        if(client.getReservationManager().removeMoney(client, 20)) {
                            client.getReservationManager().setIsPremium(client);
                            System.out.println("Upgrade successful.");
                        }
                    } else if (answer.equals("n")) {
                        System.out.println("Upgrade aborted.");
                    } else {
                        System.err.println("Wrong choice. Going back to Main Menu...");
                    }
                } else {
                    System.err.println("You are already a premium client. Going back to Main Menu...");
                }
                break;
            case 8: {
                logged = false;
                System.out.println("Logout successful.\n");
                break;
            }
        }
    }
}


