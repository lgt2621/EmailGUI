import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Purpose: A class that builds the actual email and creates a thread to send the massage/s
 * Author: Liam Tyler
 */
public class Emailer {
    /**
     * Basic tools needed to build the connection, message, and send the email
     */
    private Properties properties;
    private Session session;
    private MimeMessage message;
    private Transport transport;
    private String type;

    /**
     * Constructor
     * Sets up the basic network connections for accessing Gmail's servers
     */
    public Emailer(){
        this.properties=System.getProperties();
        this.properties.put("mail.smtp.port", "587");
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.starttls.enable", "true");
        this.session=Session.getDefaultInstance(this.properties);
        this.message=new MimeMessage(this.session);
    }

    /**
     * Opens a connection from host to gmail server
     * @param from(String) the username of the user sending the email. Doesn't include @gmail.com
     * @param password(String) the user's password
     * @throws MailerException
     */
    public void connect(String from, String password)throws MailerException{

        try {
            this.transport=this.session.getTransport("smtp");
            this.transport.connect("smtp.gmail.com", from, password);

        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            throw new MailerException("Problem logging in. Invalid username or password");
        }

    }

    /**
     * Sets the type of email being sent
     * @param type(String) type of email and options
     */
    public void setType(String type){
        this.type=type;
    }

    /**
     * Sets the email address the the email is being sent to
     * @param to(String) the person or people the email will be sent to
     * @throws MailerException
     */
    public void setTo(String to) throws MailerException {
        String[] address=to.split(" ");
        try {
            for(String addr:address) {
                this.message.addRecipients(Message.RecipientType.TO, addr);
            }
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            throw new MailerException("Problem adding recipients");
        }
    }

    /**
     * Sets the subject of the email
     * @param subject(String): the message for the subject line
     * @throws MailerException
     */
    public void setSubject(String subject) throws MailerException {
        try {
            this.message.setSubject(subject);
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            throw new MailerException("Problem setting the subject line");
        }
    }

    /**
     * Sets the message of the email
     * @param text(String) the text for the email's body
     * @throws MailerException
     */
    public void setBody(String text) throws MailerException {
        try {
            this.message.setText(text);
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            throw new MailerException("Problem adding message to the email");
        }
    }

    public void send() throws MailerException {

        try{
            this.transport.sendMessage(this.message, this.message.getAllRecipients());
        }catch(MessagingException e){
            System.err.println(e.getMessage());
            throw new MailerException("Issue has occurred sending the message");
        }
    }

    /**
     * Closes the connection to google
     * @throws MailerException
     */
    public void close() throws MailerException {
        try {
            this.transport.close();
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            throw new MailerException("Problem logging out...Guess you can't leave");
        }
    }

    /**
     * checks if the connection is still open
     * @return true if open false if not
     */
    public boolean connected(){
        return this.transport.isConnected();
    }
}
