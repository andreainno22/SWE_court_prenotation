package DAOs;

import DomainModel.Customer;
import DomainModel.Wallet;

import java.sql.Statement;

public interface WalletDao extends Dao {
    Wallet getWallet(int id, Statement stmt);
    void insertWallet(int customer_id, Statement transactionStmt);
    boolean modifyBalance(Customer customer, Statement transactionStmt);
}
