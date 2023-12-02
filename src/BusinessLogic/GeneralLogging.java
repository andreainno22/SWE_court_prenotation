package BusinessLogic;

//import Database.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GeneralLogging {

    private final Logger logger = Logger.getLogger("GeneralLog");

    SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");

    public GeneralLogging() {
        //just to make our log file nicer :)
        FileHandler fh = null;
        try {
            String FILENAME = System.getProperty("user.dir") + "/logs/GeneralLogFile_"
                    + format.format(Calendar.getInstance().getTime()) + ".log";
            File file = new File(FILENAME);
            if(!file.createNewFile())
                throw new IOException("Unable to create log file at specified path.");
            fh = new FileHandler(FILENAME);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Objects.requireNonNull(fh).setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        logger.setUseParentHandlers(false);
    }

    public void log(Exception e) {
        System.err.println("App encountered an error. See log file for more information.");
        //logging.logger.log(Level.SEVERE, "Exception: " + e);
        logger.severe("Exception: " + e);
    }
}
