package DAOs;

import DomainModel.Customer;
import DomainModel.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class WalletDaoImpl implements WalletDao {
    @Override
    public Wallet getWallet(int id, Statement stmt) {
        try {
            ResultSet rs = stmt.executeQuery("select * from wallet where customer = '" + id + "'");
            rs.next();
            Wallet wallet = new Wallet(rs.getInt(1), rs.getFloat(2));
            rs.close();
            return wallet;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }
    @Override
    public void insertWallet(int customer_id, Statement transactionStmt) {
        try {
            if (transactionStmt == null) {
                Statement stmt = db.connect();
                assert stmt != null;
                stmt.executeUpdate("INSERT INTO wallet (id, balance, customer) VALUES ('" + customer_id + "', '" + 0 + "', '" + customer_id + "')");
                db.disconnect();
            } else {
                transactionStmt.executeUpdate("INSERT INTO wallet (id, balance, customer) VALUES ('" + customer_id + "', '" + 0 + "', '" + customer_id + "')");
            }
        } catch (SQLIntegrityConstraintViolationException e1) {
            db.dbError(e1);
            db.disconnect();
        } catch (SQLException e) {
            db.dbError(e);
            db.disconnect();
        }
    }

    @Override
    public boolean modifyBalance(Customer customer, Statement transactionStmt) {
        try {
            if (transactionStmt == null) {
                Statement stmt = db.connect();
                assert stmt != null;
                stmt.executeUpdate("update wallet set balance = '" + customer.getWallet().getBalance() + "' where id = '" + customer.getWallet().getId() + "'");
                db.disconnect();
            } else {
                transactionStmt.executeUpdate("update wallet set balance = '" + customer.getWallet().getBalance() + "' where id = '" + customer.getWallet().getId() + "'");
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e1) {
            db.dbError(e1);
            db.disconnect();
            return false;
        } catch (SQLException e) {
            db.dbError(e);
            db.disconnect();
            return false;
        }
    }
}
