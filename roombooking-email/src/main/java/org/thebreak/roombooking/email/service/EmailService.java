package org.thebreak.roombooking.email.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.model.BookingNotificationEmailBO;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;
import org.thebreak.roombooking.common.model.EmailBO;
import org.thebreak.roombooking.email.util.BookingMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private BookingMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendBookingNotification(BookingNotificationEmailBO email){
        Context context = new Context();
        context.setVariable("name", email.getCustomerName());
        context.setVariable("roomTitle", email.getRoomTitle());
        context.setVariable("totalHours", email.getTotalHours());
        context.setVariable("startTime", email.getStartTime());
        context.setVariable("amount", email.getAmount());

        try {
            String bookingNotification = templateEngine.process("bookingNotificationEmail", context);
            emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your room booking with theBreak", bookingNotification);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendBookingReminder(BookingReminderEmailBO email){
        Context context = new Context();
        context.setVariable("name", email.getCustomerName());
        context.setVariable("roomTitle", email.getRoomTitle());
        context.setVariable("totalHours", email.getTotalHours());
        context.setVariable("startTime", email.getStartTime());

        try {
            String bookingNotification = templateEngine.process("bookingReminderEmail", context);
            emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your booking reminder", bookingNotification);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

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
