import Context.Client;
import Database.Database_management;

public class Main {
    public static void main(String[] args) {
        Client andre = new Client("andre", "inno", "andreinno2218@gmail.com", "andre", 123, 0, null);
        Database_management db = new Database_management();
        db.insertClient(andre);
    }
}
