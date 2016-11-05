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
    public void sendEmail(String address, int textId) {
  
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
            message.setSubject("Flatfindr news");
            // Set message text
            if(textId==0){
            	message.setText("Welcome on Flatfindr. We want to thank you for creating a new Flatfindr-account.");
            }
            if(textId==1){
            	message.setText("You have recieved a new message on your Flatfindr account.");
            }
            if(textId==2){
            	message.setText("You have been outbidden on Flatfindr.");
            }
            if(textId==3){
            	message.setText("Your Flatfindr-auction has a new highest Bidder, Congratulations!");
            }
            if(textId==4){
            	message.setText("Your Flatfindr-auction been bought out, Congratulations!");
            }
            if(textId==5){
            	message.setText("The Flatfindr-auction you were leading has been bought out, I'm sorry!");
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