package Management;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private Utils() {} // private constructor to prevent instantiation;
    public static String getDateTimeUTC() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Instant instant = Instant.now();
        ZoneId zone = ZoneId.of("UTC");
        return instant.atZone(zone).format(dtf);
    }

    public static void sendEmail(String email, String subject, String text) {
        MailManager mailManager = new MailManager();
        if (mailManager.createAndSendEmailMessage(email, subject, text))
            System.out.println("A confirmation email has been sent.");
        else System.err.println("Error sending the email.");
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        // Crea un oggetto Pattern basato sulla regex
        Pattern pattern = Pattern.compile(regex);

        // Crea un oggetto Matcher per confrontare l'indirizzo email con il modello
        Matcher matcher = pattern.matcher(email);

        // Restituisce true se l'indirizzo email corrisponde al modello regex
        return matcher.matches();
    }
}
