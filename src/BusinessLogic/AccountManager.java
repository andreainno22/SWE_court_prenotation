package BusinessLogic;

import DAOs.CustomerDaoImpl;
import DomainModel.*;
import DAOs.CustomerDao;
import DAOs.SuperUserDao;
import DAOs.SuperUserDaoImpl;

import java.sql.Date;
import java.util.Formatter;
import java.util.List;

public class AccountManager {
    public Customer customer;
    public SuperUser superUser;
    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final WalletManager walletManager = new WalletManager();
    private final SuperUserDao superUserDao = new SuperUserDaoImpl();

    public int register(String name, String surname, String email, String password, int telephoneNumber) {
        Customer newCustomer = new Customer(name, surname, email, password);
        if (telephoneNumber != 0)
            newCustomer.setTelephoneNumber(telephoneNumber);
        return customerDao.insertCustomer(newCustomer);
    }

    public void updateCustomer() {
        customer = customerDao.getCustomer(this.customer.getEmail(), this.customer.getPassword());

        if (customer.getIsPremium() == 0) customer.setReservationManager(new StandardCustomerReservationManager());
        else customer.setReservationManager(new PremiumCustomerReservationManager());

    }

    public void login(String email, String password) {
        customer = customerDao.getCustomer(email, password);
        superUser = superUserDao.getSuperUser(email, password);
    }

    public boolean setIsPremium() {
        if (walletManager.withdrawalWallet(20, customer)) {
            customer.setIsPremium(1);
            return customerDao.modifyPremium(customer);
        } else {
            System.err.println("Insufficient funds.");
            return false;
        }
    }

    public boolean renewPremium() {
        if (walletManager.withdrawalWallet(20, customer)) return customerDao.modifyPremiumExpiration(customer);
        else {
            System.err.println("Insufficient funds.");
            return false;
        }
    }

    public void showPremiumExpiration() {
        Date isPremiumDate = customerDao.getPremiumExpiration(customer);
        System.out.println("Your premium subscription will expire on: " + isPremiumDate);
    }

    public boolean checkSuperUser(String email) {
        return superUserDao.checkSuperUser(email);
    }

    public void printAllCustomers() {
        List<Customer> customers = customerDao.getAllCustomers();
        if (customers == null)
            return;
        Formatter fmt = new Formatter();
        fmt.format("%-15s%-15s%-15s%-30s%-20s%-15s%-15s\n", "ID", "NAME", "SURNAME", "EMAIL", "TELEPHONE NUMBER", "IS PREMIUM", "POINTS");
        for (Customer customer : customers)
            fmt.format("%-15s%-15s%-15s%-30s%-20s%-15s%-15s\n", customer.getId(), customer.getName(), customer.getSurname(), customer.getEmail(), customer.getTelephoneNumber(), (customer.getIsPremium() == 1), customer.getPoints());
        System.out.println(fmt);
    }

}


