package ApplicationLayer;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.*;

import Management.*;
import de.jollyday.*;
import Context.*;

import java.sql.Date;

public class GraphicInterfaceManager {
    private boolean logged = false;
    private boolean startMenu = true;
    private final Scanner sc = new Scanner(System.in);
    //private Client client;
    private final AccountManager accountManager = new AccountManager();
    private final WalletManager walletManager = new WalletManager();
    private final CourtManager courtManager = new CourtManager();
    private final TimeSlotManager timeSlotManager = new TimeSlotManager();
    private final SuperUserReservationManager superUserReservationManager = new SuperUserReservationManager();

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
                case 1 -> {
                    startMenu = false;
                    loginAccount();
                }
                case 2 -> {
                    startMenu = false;
                    registerAccount();
                }
                case 3 -> startMenu = false;
                default -> System.err.println("Wrong choice.");
            }
        }
    }

    private void registerAccount() {
        while (!startMenu) {
            boolean valid = false;
            System.out.println("Name: ");
            String name = sc.nextLine();
            System.out.println("Surname: ");
            String surname = sc.nextLine();
            System.out.println("Email: ");
            String email = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();
            //Client newClient = new Client(name, surname, email, password);
            //Database_management db = new Database_management();
            int telephoneNumber = 0;
            boolean telephoneNumberValid = false;
            while (!telephoneNumberValid) {
                try {
                    System.out.println("Telephone number [0 = not provided]: ");
                    telephoneNumber = sc.nextInt();
                    sc.nextLine();
                    telephoneNumberValid = true;
                } catch (InputMismatchException e) {
                    System.err.println("Wrong telephone number format. Retry.");
                    sc.nextLine();
                }
            }
            while (!valid) {
                // fatto controllo sulla validità dell'email
                int result;
                if (!Utils.isValidEmail(email) || accountManager.checkSuperUser(email) || (result = accountManager.register(name, surname, email, password, telephoneNumber)) == -1) {
                    System.err.println("Email already used or wrong email format. Retry.");
                    System.out.println("Type another email: [0 = Go Back]");
                    email = sc.nextLine();
                    if (email.equals("0")) break;
                    //newClient.setEmail(email);
                } else if (result == 0) {
                    valid = true;
                } else {
                    System.err.println("Error during registration. Retry.");
                    break;
                }
            }
            if (valid) {
                Utils.sendEmail(email, "Registration successful", "Hi, " + name + " " + surname + "!\nWelcome to Court Prenotation Manager." + "\nThank you for registering to our service!");
                System.out.println("Registration successful.");
                System.out.println("You can now login.\n");
                startMenu = true;
            } else {
                startMenu = true;
                System.out.println("Registration cancelled.");
            }
        }
    }

    private void loginAccount() {
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
                } else if (accountManager.superUser != null) {
                    logged = true;
                    System.out.println("Login successful.\n");
                    while (logged) {
                        superUserMenu();
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

    private void caseMakeReservation() {
        int choice;
        Date date;
        int court;
        //Reservation res = new Reservation(accountManager.client);
        System.out.println("Date (yyyy-mm-dd): ");
        try {
            // controllo sul fatto che la data non sia nel passato o presente
            date = Date.valueOf(sc.nextLine());
            ZoneId italyZone = ZoneId.of("Europe/Rome");
            // Crea una data nel fuso orario italiano
            LocalDate italianDate = LocalDate.now(italyZone);
            java.util.Date italianZonedDate = Date.from(italianDate.atStartOfDay().atZone(italyZone).toInstant());

            if (!date.after(italianZonedDate)) {
                System.err.println("You can book at least for tomorrow.");
                //break;
                return;
            }
            HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.ITALY);
            if (holidayManager.isHoliday(date.toLocalDate())) {
                System.err.println("You can't book on a holiday. Retry.");
                //break;
                return;
            }
            // aggiunta data a reservation
            //res.setDate(date);
            accountManager.client.getReservationManager().createReservation(accountManager.client, date);
        } catch (IllegalArgumentException e) {
            System.err.println("Wrong date format.");
            //break;
            return;
        }
        boolean court_selection = true;
        Formatter fmt = new Formatter();
        int num_courts = accountManager.client.getReservationManager().getCourts(fmt);
        while (court_selection) {
            System.out.println("Courts: ");
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
            //res.setCourt(courts.get(court - 1));
            accountManager.client.getReservationManager().setReservationCourt(court);
            Formatter fmt2 = new Formatter();
            boolean[] available_slots = accountManager.client.getReservationManager().getTimeSlots(fmt2, date, court);
            System.out.println("Available Time Slots: ");
            System.out.println(fmt2);
            System.out.println("Select an option:");
            System.out.println("1. Back to Court Selection\n2. Choose a time slot for this Court");
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.err.println("Wrong choice format. Going back to Main Menu...");
                sc.nextLine();
                break;
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
                    //TimeSlot ts = available_slots.get(slot - 1);
                    //res.setTime_slot(ts);
                    accountManager.client.getReservationManager().setReservationTimeSlot(slot);
                    //RentingKit rentingKit = accountManager.client.getReservationManager().getRentingKit(res.getCourt().getType());
                    accountManager.client.getReservationManager().getRentingKitInfo();
                    if (accountManager.client.getIsPremium() == 0)
                        System.out.println("How many " + accountManager.client.getReservationManager().getRentingKit().getType() + " kits do you want to rent? [Unit price = " + accountManager.client.getReservationManager().getRentingKit().getUnitPrice() + "€] [0 = None]");
                    else
                        System.out.println("How many " + accountManager.client.getReservationManager().getRentingKit().getType() + " kits do you want to rent? [Unit price = " + accountManager.client.getReservationManager().getRentingKit().getUnitPrice() + "€. Your price (-10%) = " + accountManager.client.getReservationManager().getRentingKit().getUnitPrice() * 0.9 + "€] [0 = None]");
                    boolean rentIsValid = false;
                    while (!rentIsValid) {
                        try {
                            int numOfRent = sc.nextInt();
                            sc.nextLine();
                            rentIsValid = true;
                            // aggiunta renting kit a reservation
                            //rentingKit.setNumOfRents(numOfRent);
                            //res.setRentingKit(rentingKit);
                            accountManager.client.getReservationManager().setReservationRentingKit(numOfRent);
                        } catch (InputMismatchException e) {
                            System.err.println("Wrong input format. Retry");
                            sc.nextLine();
                        }
                    }
                    // aggiunta della prenotazione al database
                    System.out.println("Subtotal price (no discounts): " + String.format("%.2f", accountManager.client.getReservationManager().getReservation().getPrice()) + "€");
                    System.out.println("Making reservation...");
                    if (accountManager.client.getReservationManager().makeReservation()) {
                        System.out.println("Reservation successful.");
                        Utils.sendEmail(accountManager.client.getEmail(), "Confirmation of reservation", "Your reservation has been made.\nDate of reservation: " + accountManager.client.getReservationManager().getReservation().getDate() + "\nCourt: " + accountManager.client.getReservationManager().getReservation().getCourt().getId() + "\nTime slot: " + accountManager.client.getReservationManager().getReservation().getTime_slot().getStart_hour() + "-" + accountManager.client.getReservationManager().getReservation().getTime_slot().getFinish_hour() + "\nThank you for choosing us!");
                    } else System.err.println("Reservation failed.");
                    court_selection = false;
                    System.out.println("Going back to Main Menu...\n");
                    break;
                default:
                    System.err.println("Wrong choice. Going back to Court Selection...");
                    break;
            }
        }
        //break;
    }

    private void caseDeleteReservation() {
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
        if (reservation == 0) return;
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
    }

    private void caseWalletManagement() {
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
                return;
            }
            if (walletManager.topUpWallet(money, accountManager.client)) {
                System.out.println("Money added successfully.");
                String dateTime = Utils.getDateTimeUTC();
                Utils.sendEmail(accountManager.client.getEmail(), "Confirmation of transaction", "Your wallet has been topped up.\nDate and time of transaction: " + dateTime + " (UTC).\nAmount: " + money + "€\nThank you for choosing us!");
            } else System.out.println("Transaction failed.");
        } else {
            System.out.println("Operation aborted.");
            System.out.println("Going back to Main Menu...");
        }
    }

    private void casePremiumUpgradeRenewal() {
        if (accountManager.client.getIsPremium() == 0) { // upgrade to premium
            System.out.println("""
                    Do you want to upgrade to premium?\s
                    You will pay 20€ for one year and then you will have a
                    10% discount for every prenotation and you unlock a points system
                    for getting bookings for free every 100 points accumulated!
                    [y/N]""");
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
            accountManager.showPremiumExpiration();
            System.out.println("Do you want to renew your subscription? [y/N]");
            String answer = sc.nextLine();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                if (accountManager.renewPremium()) {
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
    }

    private void clientMenu() {
        accountManager.updateClient();
        System.out.println("\nHello " + accountManager.client.getName() + " " + accountManager.client.getSurname() + "!");
        if (accountManager.client.getIsPremium() == 0) {
            System.out.println("You are not subscribed to Premium.\nPlease select an option:");
            System.out.println("""
                    1. Make a reservation
                    2. Delete a reservation
                    3. Show all reservations
                    4. Manage your wallet
                    5. Upgrade as a premium client
                    6. Your points
                    7. Logout""");
        } else {
            System.out.println("You are subscribed to Premium.\nPlease select an option:");
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

        /*if (accountManager.client.getIsPremium() == 0) accountManager.client.setReservationManager(new StandardReservationManager());
        else accountManager.client.setReservationManager(new PremiumReservationManager());*/

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
            case 1 -> caseMakeReservation();
            case 2 -> caseDeleteReservation();
            case 3 ->
                // stampa delle prenotazioni
                    accountManager.client.getReservationManager().printAllReservations(accountManager.client);
            case 4 -> caseWalletManagement();
            case 5 -> casePremiumUpgradeRenewal();
            case 6 ->
                // visualizzazione dei punti
                    System.out.println("You have " + accountManager.client.getPoints() + " points.");
            case 7 -> {
                logged = false;
                System.out.println("Logout successful.\n");
            }
            default -> System.err.println("Wrong choice.");
        }
    }

    private void superUserMenu() {
        System.out.println("\nHello " + accountManager.superUser.getName() + " " + accountManager.superUser.getSurname() + "!");
        System.out.println("You are super user.\nPlease select an option:");
        System.out.println("""
                1. Insert a court
                2. Delete a court
                3. Modify price of a court
                4. Insert a time slot
                5. Delete a time slot
                6. See all reservations
                7. See all future reservations
                8. See all clients       
                9. Logout""");
        int choice;

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
            case 1 -> caseInsertCourt();
            case 2 -> caseDeleteCourt();
            case 3 -> caseModifyCourtTypePrice();
            case 4 -> caseInsertTimeSlot();
            case 5 -> caseDeleteTimeSlot();
            case 6 -> superUserReservationManager.printAllReservations();
            case 7 -> superUserReservationManager.printAllFutureReservations();
            case 8 -> accountManager.printAllClients();
            case 9 -> {
                logged = false;
                System.out.println("Logout successful.\n");
            }
            default -> System.err.println("Wrong choice.");
        }
    }

    private void caseInsertCourt() {
        List<Court> courts = courtManager.printCourts();
        System.out.println("Insert new court id: (press a letter to go back) ");
        int id = 0;
        String type = "";
        boolean valid = false;
        while (!valid) {
            try {
                id = sc.nextInt();
                for (Court court : courts) {
                    if (court.getId() == id) {
                        System.err.println("Court already exists. Retry.");
                        System.out.println("Insert court id: ");
                        id = sc.nextInt();
                        while (id < 0) {
                            System.err.println("Wrong id format. Retry.");
                            System.out.println("Insert court id: ");
                            id = sc.nextInt();
                        }
                    }
                }
                String[] types = courtManager.getTypes();
                System.out.println("Insert court type: ");
                type = sc.next();
                while (!valid) {
                    for (String t : types) {
                        if (t.equals(type)) {
                            valid = true;
                            break;
                        }
                    }
                    if (!valid) {
                        System.err.println("Wrong type format. Retry.");
                        System.out.println("Insert court type: ");
                        type = sc.nextLine();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Back to menu...");
                sc.nextLine();
                break;
            }
        }
        courtManager.insertCourt(id, type);
    }

    private void caseDeleteCourt() {
        courtManager.deleteCourt(findCourt());
    }

    private int findCourt() {
        List<Court> courts = courtManager.printCourts();
        System.out.println("Insert court id: (press a letter to go back) ");
        int id = 0;
        boolean found = false;
        while (!found) {
            try {
                id = sc.nextInt();
                for (Court court : courts) {
                    if (court.getId() == id) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.err.println("Court not found. Retry.");
                    System.out.println("Insert court id: ");
                    id = sc.nextInt();
                    while (id < 0) {
                        System.err.println("Wrong id format. Retry.");
                        System.out.println("Insert court id: ");
                        id = sc.nextInt();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Back to menu...");
                sc.nextLine();
                break;
            }
        }
        return id;
    }

    private void caseModifyCourtTypePrice() {
        courtManager.printCourts();
        String[] types = courtManager.getTypes();
        System.out.println("Insert type: ");
        String type = "";
        boolean found = false;
        while (!found) {
                type = sc.next();
                for (String t : types) {
                    if (t.equals(type)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.err.println("Type not found. Retry.");
                    System.out.println("Insert type: ");
                    type = sc.nextLine();
                }
        }
        System.out.println("Insert new price: (press a letter to go back) ");
        float price = 0;
        boolean valid = false;
        while (!valid)
            try {
                price = sc.nextFloat();
                while (price < 0) {
                    System.err.println("Wrong price format. Retry.");
                    System.out.println("Insert new price: ");
                    price = sc.nextFloat();
                }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Back to menu...");
                sc.nextLine();
                break;
            }
        courtManager.updatePrice(type, price);
    }

    private void caseInsertTimeSlot() {
        List<TimeSlot> timeSlots = timeSlotManager.printTimeSlots();
        int start_hour = 0;
        int end_hour = 0;
        int id = 0;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.println("Insert time slot id: (press a letter to go back)");
                id = sc.nextInt();
                for (TimeSlot timeSlot : timeSlots) {
                    if (timeSlot.getId() == id) {
                        System.err.println("Id already exists. Retry");
                        System.out.println("Insert time slot id: ");
                        id = sc.nextInt();
                        while (id < 0) {
                            System.err.println("Wrong id format. Retry.");
                            System.out.println("Insert time slot id: ");
                            id = sc.nextInt();
                        }
                    }
                }
                    System.out.println("Insert start hour: ");
                    start_hour = sc.nextInt();
                    for (TimeSlot ts : timeSlots) {
                        if (start_hour == Integer.parseInt(ts.getStart_hour())) {
                            System.err.println("This start hour already exists. Retry.");
                            System.out.println("Insert start hour ");
                            start_hour = sc.nextInt();
                            while (start_hour < 0) {
                                System.err.println("Wrong id format. Retry.");
                                System.out.println("Insert start hour: ");
                                start_hour = sc.nextInt();
                            }
                        }
                    }
                    System.out.println("Insert end hour: ");
                    end_hour = sc.nextInt();
                    for (TimeSlot ts2 : timeSlots) {
                        if (end_hour == Integer.parseInt(ts2.getFinish_hour())) {
                            System.err.println("This end hour already exists. Retry.");
                            System.out.println("Insert end hour ");
                            end_hour = sc.nextInt();
                            while (end_hour < 0) {
                                System.err.println("Wrong id format. Retry.");
                                System.out.println("Insert end hour: ");
                                end_hour = sc.nextInt();
                            }
                        }
                    }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Back to menu...");
                sc.nextLine();
                break;
            }
        }
        timeSlotManager.insertTimeSlot(id, String.valueOf(start_hour), String.valueOf(end_hour));
    }

    private void caseDeleteTimeSlot() {
        List<TimeSlot> timeSlots = timeSlotManager.printTimeSlots();
        System.out.println("Insert time slot id: (press a letter to go back)");
        int id = 0;
        boolean found = false;
        while (!found) {
            try {
                id = sc.nextInt();
                for (TimeSlot timeSlot : timeSlots) {
                    if (timeSlot.getId() == id) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.err.println("Time slot not found. Retry.");
                    System.out.println("Insert time slot id: ");
                    id = sc.nextInt();
                    while (id < 0) {
                        System.err.println("Wrong id format. Retry.");
                        System.out.println("Insert time slot id: ");
                        id = sc.nextInt();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Back to menu...");
                sc.nextLine();
                break;
            }
        }
        timeSlotManager.deleteTimeSlot(id);
    }
}
