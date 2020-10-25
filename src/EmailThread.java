import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Random;

public class EmailThread implements Runnable {

    private String type;
    private Message message;
    private Emailer mailer;

    public EmailThread(String type, Message message, Emailer mailer){
        this.type=type;
        this.message=message;
        this.mailer=mailer;
    }

    @Override
    public void run() {
        try {
            String[] tokens=type.split(" ");
            switch (tokens[0]) {
                case "Single":
                    Address[] addrs = message.getAllRecipients();
                    message.setRecipients(Message.RecipientType.TO, null);
                    for (Address addr : addrs) {
                        message.addRecipient(Message.RecipientType.TO, addr);
                        Address[] addrArray = new Address[]{addr};
                        synchronized (mailer) {
                            mailer.reconnect();
                            mailer.getTransport().sendMessage(this.message, addrArray);
                        }
                        message.setRecipients(Message.RecipientType.TO, null);
                    }
                    break;
                case "Group":
                    synchronized (mailer) {
                        mailer.reconnect();
                        mailer.getTransport().sendMessage(this.message, this.message.getAllRecipients());
                    }
                    break;
                case "Scheduled":
                    System.out.println(tokens[1]);
                    Thread.sleep(Integer.parseInt(tokens[1]));
                    synchronized(mailer) {
                        mailer.reconnect();
                        mailer.getTransport().sendMessage(this.message, this.message.getAllRecipients());
                    }
                    break;

                case "Random":
                    Random rando=new Random();
                    int reps=Integer.parseInt(tokens[1]);
                    for(int i=0; i<reps; i++){
                        synchronized(mailer) {
                            mailer.reconnect();
                            mailer.getTransport().sendMessage(this.message, this.message.getAllRecipients());
                        }
                        int downTime=(rando.nextInt(50)*60000)+600000;
                        System.out.println(downTime);
                        Thread.sleep(downTime);
                    }

                    break;
                case "Spam":
                    break;
            }
        }catch(MessagingException me){
            System.err.println("An error has occurred\n"+me);

        } catch (InterruptedException e) {
            System.err.println("Sleep has been interrupted");
        }
    }
}
