package Database;

import Context.Client;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ClientDaoImpl implements ClientDao {
   // private final DatabaseManager db = new DatabaseManager();
    @Override
    public void deleteTestClient(String email) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("DELETE FROM client WHERE email = '" + email + "'");
        } catch (SQLException e) {
            System.err.println("Error deleting user from database.");
            System.err.println("ERROR: " + e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public Client getClient(String email, String password) {
        try {
            Statement stmt = db.connect();
            //assert stmt != null;
            if (stmt == null) {
                return null;
            }
            ResultSet rs = stmt.executeQuery("select * from client where email = '" + email + "' and password = '" + password + "'");
            if (!rs.next()) {
                rs.close();
                db.disconnect();
                return null;
            }
            WalletDaoImpl walletDao = new WalletDaoImpl();
            Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), walletDao.getWallet(rs.getInt(1), stmt));
            rs.close();
            return client;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

    @Override
    public int insertClient(Client client) {
        Statement stmt = db.connectTransaction();
        assert stmt != null;
        try {
            stmt.executeUpdate("INSERT INTO client (name, surname, email, password, telephone_number, points, is_premium) VALUES ('" + client.getName() + "', '" + client.getSurname() + "', '" + client.getEmail() + "', '" + client.getPassword() + "', '" + client.getTelephoneNumber() + "', '" + client.getPoints() + "', '" + client.getIsPremium() + "')");
            String user = client.getEmail();
            ResultSet rs = stmt.executeQuery("select id from client where email = '" + user + "'");
            rs.next();
            int client_id = rs.getInt(1);
            stmt.executeUpdate("INSERT INTO wallet (id, balance, client) VALUES ('" + client_id + "', '" + 0 + "', '" + client_id + "')");
            stmt.executeUpdate("update client set wallet = '" + client_id + "' where id = '" + client_id + "'");
            rs.close();
            db.commitTransaction();
            return 0;
        } catch (SQLIntegrityConstraintViolationException e1) {
            return -1;
        } catch (SQLException e) {
            db.dbError(e);
            return -2;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public Date getPremiumExpiration(Client client) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select end_date from premium_subs where client = '" + client.getId() + "'");
            rs.next();
            Date date = rs.getDate(1);
            rs.close();
            return date;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

    @Override
    public void updatePoints(int points, Client client, Statement transactionStmt) {
        try {
            if (transactionStmt == null) {
                Statement stmt = db.connect();
                assert stmt != null;
                stmt.executeUpdate("update client set points = '" + points + "' where id = '" + client.getId() + "'");
                db.disconnect();
            } else {
                transactionStmt.executeUpdate("update client set points = '" + points + "' where id = '" + client.getId() + "'");
            }
        } catch (SQLException e) {
            db.dbError(e);
            db.disconnect();
        }
    }

    @Override
    public boolean modifyPremiumExpiration(Client client) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            Date date = getPremiumExpiration(client);
            if (date == null) return false;
            Calendar calendario = Calendar.getInstance();
            // Aggiungi un anno a date
            calendario.setTime(date);
            calendario.add(Calendar.YEAR, 1);

            // Aggiungi un giorno alla data corrente
            calendario.add(Calendar.DAY_OF_YEAR, 1);

            // Ottieni la data con un anno in più
            java.sql.Date new_date = new java.sql.Date(calendario.getTimeInMillis());
            stmt.executeUpdate("update premium_subs set end_date = '" + new_date + "' where client = '" + client.getId() + "'");
            return true;
        } catch (SQLException e) {
            db.dbError(e);
            return false;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public boolean modifyPremium(Client client) {
        try {
            Statement stmt = db.connectTransaction();
            assert stmt != null;
            stmt.executeUpdate("update client set is_premium = '" + client.getIsPremium() + "' where id = '" + client.getId() + "'");
            Calendar calendario = Calendar.getInstance();

            calendario.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Aggiungi un giorno alla data corrente
            calendario.add(Calendar.DAY_OF_YEAR, 1);

            // Aggiungi un anno a date
            calendario.add(Calendar.YEAR, 1);

            // Ottieni la data con un anno in più
            java.sql.Date date = new java.sql.Date(calendario.getTimeInMillis());
            stmt.executeUpdate("insert into premium_subs (client, end_date) values ('" + client.getId() + "', '" + date + "')");
            WalletDaoImpl walletDao = new WalletDaoImpl();
            walletDao.modifyBalance(client, stmt);
            db.commitTransaction();
            return true;
        } catch (SQLException e) {
            db.dbError(e);
            return false;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public List<Client> getAllClients() {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select id, name, surname, email, telephone_number, is_premium, points from client");
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
                clients.add(client);
            }
            rs.close();
            return clients;
        } catch (SQLException e) {
            db.dbError(e);
            return null;
        } finally {
            db.disconnect();
        }
    }

}
