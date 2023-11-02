package Management;

import Context.Client;
//import Database.DatabaseManager;
import Database.WalletDaoImpl;

public class WalletManager {
    private final WalletDaoImpl walletDao = new WalletDaoImpl();
    public boolean topUpWallet(float money, Client client) {
        client.getWallet().addMoney(money);
        return walletDao.modifyBalance(client, null);
    }

    public boolean withdrawalWallet(float money, Client client){
        return client.getWallet().removeMoney(money);
    }

    public float getWalletBalance(Client client){
        return client.getWallet().getBalance();
    }
}
