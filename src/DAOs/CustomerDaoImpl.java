package DAOs;

import DomainModel.Customer;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public void deleteTestCustomer(String email) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("DELETE FROM customer WHERE email = '" + email + "'");
        } catch (SQLException e) {
            System.err.println("Error deleting user from database.");
            System.err.println("ERROR: " + e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public Customer getCustomer(String email, String password) {
        try {
            Statement stmt = db.connect();
            if (stmt == null) {
                return null;
            }
            ResultSet rs = stmt.executeQuery("select * from customer where email = '" + email + "' and password = '" + password + "'");
            if (!rs.next()) {
                rs.close();
                db.disconnect();
                return null;
            }
            WalletDaoImpl walletDao = new WalletDaoImpl();
            Customer customer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), walletDao.getWallet(rs.getInt(1), stmt));
            rs.close();
            return customer;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

    @Override
    public int insertCustomer(Customer customer) {
        Statement stmt = db.connectTransaction();
        assert stmt != null;
        try {
            stmt.executeUpdate("INSERT INTO customer (name, surname, email, password, telephone_number, points, is_premium) VALUES ('" + customer.getName() + "', '" + customer.getSurname() + "', '" + customer.getEmail() + "', '" + customer.getPassword() + "', '" + customer.getTelephoneNumber() + "', '" + customer.getPoints() + "', '" + customer.getIsPremium() + "')");
            String user = customer.getEmail();
            ResultSet rs = stmt.executeQuery("select id from customer where email = '" + user + "'");
            rs.next();
            int customer_id = rs.getInt(1);
            WalletDao walletDao = new WalletDaoImpl();
            walletDao.insertWallet(customer_id, stmt);
            stmt.executeUpdate("update customer set wallet = '" + customer_id + "' where id = '" + customer_id + "'");
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
    public Date getPremiumExpiration(Customer customer) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select end_date from premium_subs where customer = '" + customer.getId() + "'");
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
    public void updatePoints(int points, Customer customer, Statement transactionStmt) {
        try {
            if (transactionStmt == null) {
                Statement stmt = db.connect();
                assert stmt != null;
                stmt.executeUpdate("update customer set points = '" + points + "' where id = '" + customer.getId() + "'");
                db.disconnect();
            } else {
                transactionStmt.executeUpdate("update customer set points = '" + points + "' where id = '" + customer.getId() + "'");
            }
        } catch (SQLException e) {
            db.dbError(e);
            db.disconnect();
        }
    }

    @Override
    public boolean modifyPremiumExpiration(Customer customer) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            Date date = getPremiumExpiration(customer);
            if (date == null) return false;
            Calendar calendario = Calendar.getInstance();
            // Aggiungi un anno a date
            calendario.setTime(date);
            calendario.add(Calendar.YEAR, 1);

            // Aggiungi un giorno alla data corrente
            calendario.add(Calendar.DAY_OF_YEAR, 1);

            // Ottieni la data con un anno in più
            java.sql.Date new_date = new java.sql.Date(calendario.getTimeInMillis());
            stmt.executeUpdate("update premium_subs set end_date = '" + new_date + "' where customer = '" + customer.getId() + "'");
            return true;
        } catch (SQLException e) {
            db.dbError(e);
            return false;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public boolean modifyPremium(Customer customer) {
        try {
            Statement stmt = db.connectTransaction();
            assert stmt != null;
            stmt.executeUpdate("update customer set is_premium = '" + customer.getIsPremium() + "' where id = '" + customer.getId() + "'");
            Calendar calendario = Calendar.getInstance();

            calendario.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Aggiungi un giorno alla data corrente
            calendario.add(Calendar.DAY_OF_YEAR, 1);

            // Aggiungi un anno a date
            calendario.add(Calendar.YEAR, 1);

            // Ottieni la data con un anno in più
            java.sql.Date date = new java.sql.Date(calendario.getTimeInMillis());
            stmt.executeUpdate("insert into premium_subs (customer, end_date) values ('" + customer.getId() + "', '" + date + "')");
            WalletDaoImpl walletDao = new WalletDaoImpl();
            walletDao.modifyBalance(customer, stmt);
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
    public List<Customer> getAllCustomers() {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select id, name, surname, email, telephone_number, is_premium, points from customer");
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
                customers.add(customer);
            }
            rs.close();
            return customers;
        } catch (SQLException e) {
            db.dbError(e);
            return null;
        } finally {
            db.disconnect();
        }
    }

}
