package omms;

import java.util.Properties;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Email 
{

    public static int send(final String senderEmail, final String senderPassword, String receiverEmail, String emailSubject, String emailBody,String strhallid) 
    {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");	///587
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getDefaultInstance(props,new Authenticator() 
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try 
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject(emailSubject);
            message.setText(emailBody);
            Transport.send(message);
            
           ///JOptionPane.showMessageDialog(null,"Email sent!");
           return 1;
            
        } catch (Exception e) 
        {
            SendMail.flag =1;
            SendMail.phone.add(strhallid);
            System.err.println("Error Sending: ");
            //JOptionPane.showMessageDialog(null,"Failed!", "Email from " + senderEmail + " couldn't be sent to " + receiverEmail, JOptionPane.ERROR_MESSAGE);

        }
        
        return 0;
        
    }
    
}