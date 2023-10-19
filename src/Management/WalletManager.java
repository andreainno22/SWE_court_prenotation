package Management;

import Context.Client;
import Database.DatabaseManager;

public class WalletManager {
    private final DatabaseManager db = new DatabaseManager();

    public boolean topUpWallet(float money, Client client) {
        client.getWallet().addMoney(money);
        return db.modifyBalance(client, null);
    }

    public boolean withdrawalWallet(float money, Client client){
        return client.getWallet().removeMoney(money);
    }

    public float getWalletBalance(Client client){
        return client.getWallet().getBalance();
    }
}
