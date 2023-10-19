package Database;

import Context.Client;

public interface ClientDao {
    public void deleteTestClient(String email);
    public Client getClient(String email, String password);
    public int insertClient(Client client);
}
