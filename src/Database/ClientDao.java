package Database;

import Context.Client;

import java.sql.Date;
import java.sql.Statement;

public interface ClientDao extends GeneralDaoSettings {
    void deleteTestClient(String email);
    Client getClient(String email, String password);
    int insertClient(Client client);
    Date getPremiumExpiration(Client client);
    void updatePoints(int points, Client client, Statement transactionStmt);
    boolean modifyPremiumExpiration(Client client);
    boolean modifyPremium(Client client);
}
