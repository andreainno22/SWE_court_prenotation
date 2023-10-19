package Database;

import Context.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class ClientDaoImpl implements ClientDao {
    @Override

    public void deleteTestClient(String email) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            stmt.executeUpdate("DELETE FROM client WHERE email = '" + email + "'");
        } catch (SQLException e) {
            System.err.println("Error deleting user from database.");
            System.err.println("ERROR: " + e);
        } finally {
            disconnect();
        }
    }

    @Override
    public Client getClient(String email, String password) {
        try {
            Statement stmt = connect();
            //assert stmt != null;
            if (stmt == null) {
                return null;
            }
            ResultSet rs = stmt.executeQuery("select * from client where email = '" + email + "' and password = '" + password + "'");
            if (!rs.next()) {
                System.err.println("Wrong email or password. Retry.");
                rs.close();
                disconnect();
                return null;
            }
            Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), getWallet(rs.getInt(1), stmt));
            rs.close();
            return client;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }

    @Override
    public int insertClient(Client client) {
        Statement stmt = connectTransaction();
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
            commitTransaction();
            return 0;
        } catch (SQLIntegrityConstraintViolationException e1) {
            return -1;
        } catch (SQLException e) {
            dbError(e);
            return -2;
        } finally {
            disconnect();
        }
    }

}
