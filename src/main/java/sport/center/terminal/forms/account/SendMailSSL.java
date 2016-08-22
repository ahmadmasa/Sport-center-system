package sport.center.terminal.forms.account;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Asendar
 */
public class SendMailSSL {

    /**
     * @param code
     * @param Email
     */
    public static void sendEmail(String code, String Email) {

        try {
            final String fromEmail = "asendar.codes@gmail.com"; //requires valid gmail id
            final String password = "abdullah1$$"; // correct password for gmail id
            final String toEmail = Email; // can be any email id 

            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };
            Session session = Session.getInstance(props, auth);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            System.out.println("Mail Check 2");

            message.setSubject("Asendar Codes Password Reset");

            String body = "Hello,<br><br>Someone tried to reset your password for Sport Center Management Terminal,<br> If this is you please copy"
                    + " and past this code :" + code + " in the application to reset your password.<br><br><br>";

            message.setContent("<html>" + body + "</html>", "text/html; charset=utf-8");

            System.out.println("Mail Check 3");

            Transport.send(message);
            System.out.println("Mail Sent");
        } catch (Exception ex) {
            System.out.println("Mail fail");
            System.out.println(ex);
        }
    }

    /**
     * @param Email
     * @return
     */
    public static boolean sendWelcomeEmail(String Email) {

        try {
            final String fromEmail = "asendar.codes@gmail.com"; //requires valid gmail id
            final String password = "abdullah1$$"; // correct password for gmail id
            final String toEmail = Email; // can be any email id 

            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };
            Session session = Session.getInstance(props, auth);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            System.out.println("Mail Check 2");

            message.setSubject("Welcome");

            message.setText("Welcome to Terminal,\nYou have successfuly created an account,\nyou will be able to sign in as soon as one of our managers "
                    + "accept your request, please be patient.\n\nBest Regards,\nAsendar Codes");

            System.out.println("Mail Check 3");

            Transport.send(message);
            System.out.println("Mail Sent");
        } catch (Exception ex) {
            System.out.println("Mail fail");
            System.out.println(ex);

            return false;
        }

        return true;
    }

    /**
     * @param Email
     * @return
     */
    public static boolean sendApproveEmail(String Email) {

        try {
            final String fromEmail = "asendar.codes@gmail.com"; //requires valid gmail id
            final String password = "abdullah1$$"; // correct password for gmail id
            final String toEmail = Email; // can be any email id 

            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };
            Session session = Session.getInstance(props, auth);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            System.out.println("Mail Check 2");

            message.setSubject("Approved");

            message.setText("Hello,\n\nPlease note that you have been successfully approved,"
                    + " you can now login into the terminal using your email and password.");

            System.out.println("Mail Check 3");

            Transport.send(message);
            System.out.println("Mail Sent");
        } catch (Exception ex) {
            System.out.println("Mail fail");
            System.out.println(ex);

            return false;
        }

        return true;
    }
}
