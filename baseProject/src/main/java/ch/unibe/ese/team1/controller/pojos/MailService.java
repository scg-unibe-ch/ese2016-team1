package ch.unibe.ese.team1.controller.pojos;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
/**
 * This class alerts the given private e-mail addresses of the Users.
 *
 */
public class MailService {
	 

    /**
     * Send the email via SMTP using StartTLS and SSL
     */
    public void sendEmail(String address, int textId, String link) {
  
        // Create all the needed properties
        Properties connectionProperties = new Properties();
        // SMTP host
        connectionProperties.put("mail.smtp.host", "smtp.gmail.com");
        // Is authentication enabled
        connectionProperties.put("mail.smtp.auth", "true");
        // Is StartTLS enabled
        connectionProperties.put("mail.smtp.starttls.enable", "true");
        // SSL Port
        connectionProperties.put("mail.smtp.socketFactory.port", "465");
        // SSL Socket Factory class
        connectionProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // SMTP port, the same as SSL port :)
        connectionProperties.put("mail.smtp.port", "465");
         
        System.out.print("Creating the session...");
         
        // Create the session
        Session session = Session.getDefaultInstance(connectionProperties,
                new javax.mail.Authenticator() {    // Define the authenticator
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("gretar.geiser@gmail.com","Team1Team1");
                    }
                });
         
        System.out.println("done!");
         
        // Create and send the message
        try {
            // Create the message 
            Message message = new MimeMessage(session);
            // Set sender
            message.setFrom(new InternetAddress("gretar.geiser@gmail.com"));
            // Set the recipients
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
            // Set message subject
            message.setSubject("SwissHomes news");
            // Set message text
            if(textId==0){
            	message.setText("Hello " + address +"\nWelcome on SwissHomes. We want to thank you for creating a new SwissHomes-account.\n " +link);
            }
            if(textId==1){
            	message.setText("Hello " + address +"\nYou have recieved a new message on your SwissHomes account.\n " +link);
            }
            if(textId==2){
            	message.setText("Hello " + address +"\nYou have been outbidden on SwissHomes.\n " +link);
            }
            if(textId==3){
            	message.setText("Hello " + address +"\nYour SwissHomes-auction has a new highest Bidder, Congratulations!\n " +link);
            }
            if(textId==4){
            	message.setText("Hello " + address +"\nYour SwissHomes-auction been bought out, Congratulations!\n " +link);
            }
            if(textId==5){
            	message.setText("Hello " + address +"\nThe SwissHomes-auction you were leading has been bought out, I'm sorry!\n " +link);
            }
            if(textId==6){
            	message.setText("Hello " + address +"\nYour SwissHomes-auction has ended!\n " +link);
            }
            if(textId==7){
            	message.setText("Hello " + address +"\nConratulations\nYou have won this SwissHomes-auction!\n " +link);
            }
             
            System.out.print("Sending message...");
            // Send the message
            Transport.send(message);
             
            System.out.println("done!");
             
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
     
}