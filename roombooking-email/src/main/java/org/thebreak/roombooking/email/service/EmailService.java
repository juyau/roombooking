package org.thebreak.roombooking.email.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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

//    @KafkaListener(topics = "${spring.kafka.topic.test}")
//    public void test(String msg){
//        System.out.println("***************** received email topic ************");
//        System.out.println(msg);
//    }

    @KafkaListener(topics = "${spring.kafka.topics.bookingEmailNotification}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "notificationListenerContainerFactory")
    public void sendBookingNotification(BookingNotificationEmailBO email){
        System.out.println("Received email information: " + email);
        Context context = new Context();
        context.setVariable("name", email.getCustomerName());
        context.setVariable("roomTitle", email.getRoomTitle());
        context.setVariable("totalHours", email.getTotalHours());
        context.setVariable("startTime", email.getStartTime());
        context.setVariable("amount", email.getAmount());

        try {
            System.out.println("Start to send notification email...");
            String bookingNotification = templateEngine.process("bookingNotificationEmail", context);
            emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your room booking with theBreak", bookingNotification);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.bookingEmailReminder}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "reminderListenerContainerFactory")
    public void sendBookingReminder(BookingReminderEmailBO email){
        System.out.println("Received reminder email information: " + email);
        Context context = new Context();
        context.setVariable("name", email.getCustomerName());
        context.setVariable("roomTitle", email.getRoomTitle());
        context.setVariable("totalHours", email.getTotalHours());
        context.setVariable("startTime", email.getStartTime());

        try {
            System.out.println("Start to send reminder email...");
            String reminderEmail = templateEngine.process("bookingReminderEmail", context);
            emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your booking reminder", reminderEmail);
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
