package BusinessLogic;

import DomainModel.Customer;
import DAOs.WalletDao;
import DAOs.WalletDaoImpl;

public class WalletManager {
    private final WalletDao walletDao = new WalletDaoImpl();

    public boolean topUpWallet(float money, Customer customer) {
        customer.getWallet().addMoney(money);
        return walletDao.modifyBalance(customer, null);
    }

    public boolean withdrawalWallet(float money, Customer customer) {
        return customer.getWallet().removeMoney(money);
    }

    public float getWalletBalance(Customer customer) {
        return customer.getWallet().getBalance();
    }
}
