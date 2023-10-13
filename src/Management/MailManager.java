package Management;

import com.sun.tools.javac.Main;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailManager {
    public static final String sender = "notifier.courtprenotation@gmail.com";
    public static final String pass = "tnow albs jwkx rpzt";

    private static class SMTPAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(sender, pass);
        }
    }

    private static class Email {
        private String subject;
        private String messageContent;
        private String recipient;

        private String sender;

        private void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        private void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        private void setSubject(String subject) {
            this.subject = subject;
        }

        private void setSender(String sender) {
            this.sender = sender;
        }
    }

    public boolean createAndSendEmailMessage(String recipient, String subject, String messageContent) {
        try {
            Email email = new Email();
            email.setRecipient(recipient + "");
            email.setSender(sender + "");
            email.setSubject(subject + "");
            email.setMessageContent(messageContent + "");
            sendEmailMessage(email);
            return true;
        } catch (MessagingException e) {
            System.err.println("App found an error trying sending an email. It could be related to an installed antivirus.");
            return false;
        }
    }

    private void sendEmailMessage(Email email) throws MessagingException {

        // Get system properties
        Properties props;
        props = new Properties();
        props.put("mail.smtp.user", email.sender);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        SMTPAuthenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(false);

        MimeMessage msg = new MimeMessage(session);
        msg.setText(email.messageContent);
        msg.setSubject(email.subject);
        msg.setFrom(new InternetAddress(email.sender));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.recipient));

        Transport transport = session.getTransport("smtps");
        transport.connect("smtp.gmail.com", 465, sender, pass);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }
}
