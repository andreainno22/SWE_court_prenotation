package Database;

import Context.Client;
import Context.Court;
import Context.Reservation;
import Context.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourtDaoImpl implements CourtDao {
    // private final DatabaseManager db = new DatabaseManager();

    @Override
    public List<Court> getCourts() {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT court.id, type_of_court.type_of_court, type_of_court.price FROM court JOIN type_of_court ON court.type = type_of_court.id");
            List<Court> court_type_prices = new ArrayList<>();
            while (rs.next()) {
                court_type_prices.add(new Court(rs.getInt(1), rs.getFloat(3)));
                if (rs.getString(2).equals("padel")) {
                    court_type_prices.get(court_type_prices.size() - 1).setType("padel");
                    court_type_prices.get(court_type_prices.size() - 1).setTerrain_type("null");
                } else {
                    court_type_prices.get(court_type_prices.size() - 1).setTerrain_type(rs.getString(2));
                    court_type_prices.get(court_type_prices.size() - 1).setType("tennis");
                }
            }

            rs.close();
            return court_type_prices;
        } catch (SQLException e) {
            db.dbError(e);
            return null;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public void insertCourt(int id, String type) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("INSERT INTO court VALUES ((SELECT id FROM type_of_court WHERE type_of_court = '" + type + "'), " + id + ")");
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public List<Reservation> deleteCourt(int id) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select client.*, wallet.balance, reservation.price from client, reservation, wallet where wallet.client = client.id and client.id = reservation.client and reservation.court = '" + id + "'");
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                Client c = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), new Wallet(rs.getInt(9), rs.getFloat(10) + rs.getFloat(11)));
                reservations.add(new Reservation(rs.getFloat(11), c));
            }
            rs.close();
            db.disconnect();
            Statement stmt1 = db.connectTransaction();
            WalletDaoImpl walletDao = new WalletDaoImpl();
            ClientDaoImpl clientDao = new ClientDaoImpl();
            for (Reservation r : reservations) {
                if(r.getPrice() != 0)
                    walletDao.modifyBalance(r.getClient(), stmt1);
                else {
                    int points = r.getClient().getPoints() + 100;
                    clientDao.updatePoints(points, r.getClient(), stmt1);
                }
            }
            stmt1.executeUpdate("DELETE FROM court WHERE id = " + id);
            db.commitTransaction();
            return reservations;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

    @Override
    public String[] getTypes() {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT type_of_court FROM type_of_court");
            ArrayList<String> types = new ArrayList<>();
            while (rs.next()) {
                types.add(rs.getString(1));
            }
            rs.close();
            return types.toArray(new String[0]);
        } catch (SQLException e) {
            db.dbError(e);
            return null;
        } finally {
            db.disconnect();
        }
    }

    @Override
    public void updatePrice(String type, float price) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("UPDATE type_of_court SET price = " + price + " WHERE type_of_court = '" + type + "'");
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public float getPrice(String type){
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("SELECT price FROM type_of_court WHERE type_of_court = '" + type + "'");
            rs.next();
            float price = rs.getFloat(1);
            rs.close();
            return price;
        } catch (SQLException e) {
            db.dbError(e);
            return -1;
        } finally {
            db.disconnect();
        }
    }
}
