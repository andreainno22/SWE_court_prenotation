package Management;

import Context.*;
import Database.ClientDaoImpl;
import Database.SuperUserDaoImpl;

import java.sql.Date;

public class AccountManager {
    public Client client;
    public SuperUser superUser;
    private final ClientDaoImpl clientDao = new ClientDaoImpl();
    private final WalletManager walletManager = new WalletManager();
    private final SuperUserDaoImpl superUserDao = new SuperUserDaoImpl();

    public int register(String name, String surname, String email, String password, int telephoneNumber) {
        Client newClient = new Client(name, surname, email, password);
        if (telephoneNumber != 0)
            newClient.setTelephoneNumber(telephoneNumber);
        return clientDao.insertClient(newClient);
    }

    public void updateClient() {
        //Database_management db = new Database_management();
        client = clientDao.getClient(this.client.getEmail(), this.client.getPassword());

        if (client.getIsPremium() == 0) client.setReservationManager(new StandardClientReservationManager());
        else client.setReservationManager(new PremiumClientReservationManager());

    }

    public void login(String email, String password) {
        client = clientDao.getClient(email, password);
        superUser = superUserDao.getSuperUser(email, password);
    }

    public boolean setIsPremium(Client client) {
        //Database_management db = new Database_management();

        if (walletManager.withdrawalWallet(20, client)) {
            client.setIsPremium(1);
            return clientDao.modifyPremium(client);
        } else {
            System.err.println("Insufficient funds.");
            return false;
        }
    }

    public boolean renewPremium() {
        //Database_management db = new Database_management();
        if (walletManager.withdrawalWallet(20, client)) return clientDao.modifyPremiumExpiration(client);
        else {
            System.err.println("Insufficient funds.");
            return false;
        }
    }

    public void showPremiumExpiration() {
        //Database_management db = new Database_management();
        Date isPremiumDate = clientDao.getPremiumExpiration(client);
        System.out.println("Your premium subscription will expire on: " + isPremiumDate);
    }

    public boolean checkSuperUser(String email) {
        return superUserDao.checkSuperUser(email);
    }

    public void printAllClients() {
        clientDao.getAllClients();
    }
}


