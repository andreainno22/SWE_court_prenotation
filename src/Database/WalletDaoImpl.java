package Database;

import Context.Client;
import Context.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class WalletDaoImpl implements WalletDao{
    //private final DatabaseManager db = new DatabaseManager();

    @Override
    public Wallet getWallet(int id, Statement stmt) {
        try {
            ResultSet rs = stmt.executeQuery("select * from wallet where client = '" + id + "'");
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
    public boolean modifyBalance(Client client, Statement transactionStmt) {
        try {
            if (transactionStmt == null) {
                Statement stmt = db.connect();
                assert stmt != null;
                stmt.executeUpdate("update wallet set balance = '" + client.getWallet().getBalance() + "' where id = '" + client.getWallet().getId() + "'");
                db.disconnect();
            } else {
                transactionStmt.executeUpdate("update wallet set balance = '" + client.getWallet().getBalance() + "' where id = '" + client.getWallet().getId() + "'");
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e1) {
            db.dbError(e1);
            db.disconnect();
            return false;
        } catch (SQLException e) {
            db.dbError(e);
            db.disconnect();
            //e.printStackTrace();
            return false;
        }
    }
}
