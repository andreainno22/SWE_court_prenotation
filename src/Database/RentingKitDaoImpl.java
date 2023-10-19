package Database;

import Context.RentingKit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RentingKitDaoImpl {
    @Override
    public RentingKit getRentingKit(String type) {
        try {
            Statement stmt = connect();
            assert stmt != null;
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM renting_kits WHERE type = '" + type + "'");
            resultSet.next();
            RentingKit rentingKit = new RentingKit(resultSet.getInt(1), resultSet.getString(2), resultSet.getFloat(3));
            resultSet.close();
            return rentingKit;
        } catch (SQLException e) {
            dbError(e);
        } finally {
            disconnect();
        }
        return null;
    }
}
