import Management.AccountManager;
import Management.GeneralLogging;

public class Main {
    public static void main(String[] args) {
        GeneralLogging logger = new GeneralLogging();
        try {
            AccountManager am = new AccountManager();
            am.startMenu();
        } catch (Exception e){
            logger.log(e);
            System.err.println("App shutdown.");
        }
    }
}
