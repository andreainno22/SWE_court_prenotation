package DAOs;

import DomainModel.SuperUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SuperUserDaoImpl implements SuperUserDao {

    @Override
    public void insertSuperUser(SuperUser superUser) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("INSERT INTO super_user (name, surname, email, password, telephone_number) VALUES ('" + superUser.getName() + "', '" + superUser.getSurname() + "', '" + superUser.getEmail() + "', '" + superUser.getPassword() + "', '" + superUser.getTelephoneNumber() + "')");
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
    }

    @Override
    public SuperUser getSuperUser(String email, String password) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select * from super_user where email = '" + email + "' and password = '" + password + "'");
            if (!rs.next()) {
                rs.close();
                db.disconnect();
                return null;
            }
            return new SuperUser(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return null;
    }

    @Override
    public boolean checkSuperUser(String email) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select * from super_user where email = '" + email + "'");
            if (!rs.next()) {
                rs.close();
                db.disconnect();
                return false;
            }
            rs.close();
            db.disconnect();
            return true;
        } catch (SQLException e) {
            db.dbError(e);
        } finally {
            db.disconnect();
        }
        return false;
    }

    @Override
    public void deleteSuperUser(String email) {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            stmt.executeUpdate("DELETE FROM super_user WHERE email = '" + email + "'");
        } catch (SQLException e) {
            System.err.println("Error deleting user from database.");
            System.err.println("ERROR: " + e);
        } finally {
            db.disconnect();
        }
    }
}
