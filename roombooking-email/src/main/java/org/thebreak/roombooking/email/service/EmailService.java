package org.thebreak.roombooking.email.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.model.EmailBO;
import org.thebreak.roombooking.email.util.BookingMailSender;

import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private BookingMailSender emailSender;

    public void sendHtmlEmail(EmailBO email) {
        try {
            emailSender.sendHtmlBodyEmail(email.getTo(),email.getSubject(), email.getBody());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendSimpleTextEmail(EmailBO email){
        try {
            emailSender.sendSimpleTextEmail(email.getTo(),email.getSubject(), email.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
