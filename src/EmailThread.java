import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import java.lang.reflect.Array;

public class EmailThread implements Runnable {

    private String type;
    private Message message;
    private Transport transport;

    public EmailThread(String type, Message message, Transport transport){
        this.type=type;
        this.message=message;
        this.transport=transport;
    }

    @Override
    public void run() {
        try {
            switch (type) {
                case "Single":
                    Address[] addrs = message.getAllRecipients();
                    message.setRecipients(Message.RecipientType.TO, null);
                    for (Address addr : addrs) {
                        message.addRecipient(Message.RecipientType.TO, addr);
                        Address[] addrArray = new Address[]{addr};
                        transport.sendMessage(this.message, addrArray);
                        message.setRecipients(Message.RecipientType.TO, null);
                    }
                    break;
                case "Group":
                    transport.sendMessage(this.message, this.message.getAllRecipients());
                    break;
                case "Scheduled":
                    break;
                case "Random":
                    break;
                case "Spam":
                    break;
            }
        }catch(MessagingException me){
            System.err.println("An error has occured");
        }
    }
}
