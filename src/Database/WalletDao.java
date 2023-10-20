package Database;

import Context.Client;
import Context.Wallet;

import java.sql.Statement;

public interface WalletDao extends GeneralDaoSettings {
    Wallet getWallet(int id, Statement stmt);

    boolean modifyBalance(Client client, Statement transactionStmt);
}
