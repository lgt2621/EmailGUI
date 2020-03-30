/**
 * Purpose: A special exception to allow me to create error messages for the GUI to display
 * Author: Liam Tyler
 */
public class MailerException extends Exception{

    /**
     * MailerException constructor
     * @param error(String) the error message
     */
    public MailerException(String error){
        super(error);
    }
}
