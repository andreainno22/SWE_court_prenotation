import Management.AccountManager;
import Management.GeneralLogging;

public class Main {
    public static void main(String[] args) {
        try {
            AccountManager am = new AccountManager();
            am.startMenu();
        } catch (Exception e){
            GeneralLogging logger = new GeneralLogging();
            logger.log(e);
            System.err.println("App shutdown.");
        }
    }
}
