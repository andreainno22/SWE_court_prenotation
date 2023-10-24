package Database;

import Context.SuperUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SuperUserDaoImpl implements SuperUserDao {
    public SuperUser getSuperUser(String email, String password)  {
        try {
            Statement stmt = db.connect();
            assert stmt != null;
            ResultSet rs = stmt.executeQuery("select * from super_user where email = '" + email + "' and password = '" + password + "'");
            if (!rs.next()) {
                System.err.println("Wrong email or password. Retry.");
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
}
