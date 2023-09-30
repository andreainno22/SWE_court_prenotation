package Management;

import Context.Client;
import Context.Court;
import Database.Database_management;

import java.awt.*;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Statement;

public class AccountManager {
    private boolean logged = false;

    private boolean startMenu = true;

    public void startMenu() {
        while (startMenu) {
            Scanner sc = new Scanner(System.in);
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice = sc.nextInt();
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
            System.out.println("Telephone number [0 = not provided]: ");
            int telephoneNumber = sc.nextInt();
            Client client = new Client(name, surname, email, password);
            if (telephoneNumber != 0)
                client.setTelephoneNumber(telephoneNumber);
            Database_management db = new Database_management();

            while (!valid) {
                if (db.insertClient(client) == -1) {
                    sc.nextLine();
                    System.out.println("Type another email: ");
                    email = sc.nextLine();
                    client.setEmail(email);
                } else {
                    valid = true;
                }
            }
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
                    System.out.println("Login successful\n");
                    while (logged) {
                        clientMenu(client);
                    }
                    logged = true;
                    startMenu = true;
                } else {
                    System.err.println("Wrong email or password. Retry.");
                }
            }
        }
    }


    private void clientMenu(Client client) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Make a reservation\n2. Edit a reservation\n3. Delete a reservation\n4. Show all reservations\n6. Manage your wallet\n7. Logout");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                if (client.getReservationManager() == null) {
                    if (client.getisPremium() == 1)
                        client.setReservationManager(new PremiumReservationManager());
                    else
                        client.setReservationManager(new StandardReservationManager());
                }
                Date date;
                int court;
                System.out.println("Date (yyyy-mm-dd): ");
                try {
                    date = Date.valueOf(sc.next());
                } catch (IllegalArgumentException e) {
                    System.err.println("Wrong date format.");
                    break;
                }
                boolean court_selection = true;
                while (court_selection) {
                    client.getReservationManager().getCourt();
                    System.out.println("Select a Court [0 = Back to Main Menu]: ");
                    try {
                        court = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.err.println("Wrong court format.");
                        break;
                    }
                    if (court == 0) {
                        break;
                    }
                    boolean[] available_slots = client.getReservationManager().getTimeSlots(date, court);
                    System.out.println("1. Back to Courts\n2. Choose a time slot for this Court\n");
                    int choice2 = sc.nextInt();
                    switch (choice2) {
                        case 1:
                            //back = true;
                            break;
                        case 2:
                            int slot;
                            System.out.println("ID of desired Time Slot: ");
                            boolean valid = false;
                            while(!valid) {
                                try {
                                    slot = sc.nextInt();
                                    valid = true;
                                    while (!available_slots[slot - 1]) {
                                        System.err.println("Given Time Slot is wrong or not available. Retry.");
                                        System.out.println("ID of desired Time Slot: ");
                                        slot = sc.nextInt();
                                    }
                                } catch (InputMismatchException e) {
                                    System.err.println("Wrong input format. Retry");
                                    break;
                                }
                            }
                            //TODO: fare la prenotazione
                            court_selection = false;
                            break;
                        default:
                            System.err.println("Wrong choice.");
                            break;
                    }
                }
                break;
            default: {
                System.err.println("Wrong choice.");
                break;
            }
            case 7: {
                logged = false;
                break;
            }
        }
    }
}


