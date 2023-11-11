package Database;

import Context.Client;

import java.sql.Date;
import java.sql.Statement;
import java.util.List;

public interface ClientDao extends Dao {
    void deleteTestClient(String email);
    int insertClient(Client client);
    Client getClient(String email, String password);
    Date getPremiumExpiration(Client client);
    void updatePoints(int points, Client client, Statement transactionStmt);
    boolean modifyPremiumExpiration(Client client);
    boolean modifyPremium(Client client);
    List<Client> getAllClients();
}
