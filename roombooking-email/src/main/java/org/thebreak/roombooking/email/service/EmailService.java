package org.thebreak.roombooking.email.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.email.model.EmailBO;
import org.thebreak.roombooking.email.util.NotificationEmail;
import javax.mail.MessagingException;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private NotificationEmail emailSender;

    public void sendHtmlTextEmail(EmailBO email){

        try {
            emailSender.sendHtmlBodyEmail(email.getTo(),email.getSubject(), email.getBody());
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendSimpleTextEmail(EmailBO email){
        emailSender.sendSimpleTextEmail(email.getTo(),email.getSubject(), email.getBody());
    }
}
