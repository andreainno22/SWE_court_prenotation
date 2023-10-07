package Management;

import Database.Database_management;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GeneralLogging {

    private final Logger logger = Logger.getLogger(Database_management.class.getName());

    private FileHandler fh = null;
    SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
    private String FILENAME = System.getProperty("user.dir") + "/logs/GeneralLogFile_"
            + format.format(Calendar.getInstance().getTime()) + ".log";

    private File file = new File(FILENAME);

    public GeneralLogging() {
        //just to make our log file nicer :)
        try {
            file.createNewFile();
            fh = new FileHandler(FILENAME);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        logger.setUseParentHandlers(false);
    }
    public void log(Exception e) {
        System.err.println("App encountered an error. See log file for more information.");
        //logging.logger.log(Level.SEVERE, "Exception: " + e);
        logger.severe("Exception: " + e);
    }
}
