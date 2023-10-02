package Management;

import Context.Client;
import Context.Court;
import Database.Database_management;

import java.awt.*;
import java.sql.Date;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Statement;

public class AccountManager {
    private boolean logged = false;

    private boolean startMenu = true;

    public void startMenu() {
        while (startMenu) {
            System.out.println("\nHello! Please login or register to continue.");
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice;
            Scanner scanner;
            while(true) {
                try {
                    scanner = new Scanner(System.in);
                    choice = scanner.nextInt();
                    break;
                } catch (InputMismatchException ex) {
                    System.err.println("Wrong choice format.Retry.");
                }
            }
            switch(choice) {
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
        System.out.println("1. Make a reservation\n2. Edit a reservation\n3. Delete a reservation\n4. Show all reservations\n6. Manage your wallet\n7. Logout");
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
        switch (choice) {
            case 1:
                if (client.getReservationManager() == null) {
                    if (client.getisPremium() == 1)
                        client.setReservationManager(new PremiumReservationManager());
                    else
                        client.setReservationManager(new StandardReservationManager());
                }
                Date date;
                int court = 0;
                System.out.println("Date (yyyy-mm-dd): ");
                try {
                    date = Date.valueOf(sc.next());
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
                            if (court > num_courts) {
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
                    Formatter fmt2 = new Formatter();
                    boolean[] available_slots = client.getReservationManager().getTimeSlots(fmt2, date, court);
                    while(true) {
                    System.out.println(fmt2);
                    System.out.println("Select an option:");
                    System.out.println("1. Back to Courts\n2. Choose a time slot for this Court");
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
                            int slot;
                            System.out.println("ID of desired Time Slot: ");
                            boolean valid = false;
                            while(!valid) {
                                try {
                                    slot = sc.nextInt();
                                    if(slot > available_slots.length || slot < 1) {
                                        System.err.println("Given Time Slot is wrong. Retry.");
                                        continue;
                                    }
                                    valid = true;
                                    while (!available_slots[slot - 1]) {
                                        System.err.println("Given Time Slot is not available. Retry.");
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
                            System.out.println("Reservation successful.");
                            System.out.println("Going back to Main Menu...\n");
                            break;
                        default:
                            System.err.println("Wrong choice. Going back to Court Selection...");
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
                System.out.println("Logout successful.\n");
                break;
            }
        }
    }
}


