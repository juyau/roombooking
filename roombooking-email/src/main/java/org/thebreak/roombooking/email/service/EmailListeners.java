package org.thebreak.roombooking.email.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.model.*;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.common.util.PriceUtils;
import org.thebreak.roombooking.email.feign.BookingFeign;
import org.thebreak.roombooking.email.util.BookingMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
@Slf4j
public class EmailListeners {

    @Autowired
    private BookingMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private BookingFeign bookingFeign;


    @KafkaListener(topics = "${spring.kafka.topics.bookingSuccess}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "notificationListenerContainerFactory")
    public void sendBookingSuccess(BookingNotificationEmailBO email){
        log.info("Received email information: " + email);
        Context context = new Context();
        context.setVariable("name", email.getCustomerName());
        context.setVariable("roomTitle", email.getRoomTitle());
        context.setVariable("totalHours", email.getTotalHours());
        context.setVariable("startTime", email.getStartTime());
        context.setVariable("amount", email.getAmount());


        try {
            log.info("Start to send notification email...");
            String bookingNotification = templateEngine.process("bookingSuccessEmail", context);
            emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your room booking with theBreak", bookingNotification);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.bookingReminder}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "reminderListenerContainerFactory")
    public void sendBookingReminder(BookingReminderEmailBO email){
        log.info("Received reminder email information: " + email);
        Context context = new Context();
        context.setVariable("name", email.getCustomerName());
        context.setVariable("roomTitle", email.getRoomTitle());
        context.setVariable("startTime", email.getStartTime());

        try {
            log.info("Start to send reminder email...");
            String reminderEmail = templateEngine.process("bookingReminderEmail", context);
            emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your booking reminder", reminderEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.bookingClose}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "closeListenerContainerFactory")
    public void sendBookingClose(BookingCloseEmailBO email){
        log.info("Received close email information: " + email);
        Context context = new Context();
        context.setVariable("name", email.getCustomerName());
        context.setVariable("roomTitle", email.getRoomTitle());
        context.setVariable("startTime", email.getStartTime());

        try {
            log.info("Start to send close email...");
            String closeEmail = templateEngine.process("bookingCloseEmail", context);
            emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your booking has been closed", closeEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.paymentSuccess}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentListenerContainerFactory")
    public void sendPaymentSuccess(PaymentBO emailBO){
        // query booking info from booking application via feign
        ResponseResult<PaymentEmailBO> responseResult = bookingFeign.findPaymentEmailBObyId(emailBO.getBookingId());
        if(responseResult.getSuccess()){
            PaymentEmailBO email = responseResult.getData();

            log.info("Received payment email information: " + email);
            Context context = new Context();
            context.setVariable("name", email.getCustomerName());
            context.setVariable("roomTitle", email.getRoomTitle());
            context.setVariable("totalHours", email.getTotalHours());
            context.setVariable("startTime", email.getStartTime());
            // pay amount comes from emailBO for actual paid amount
            String strAmount = PriceUtils.formatDollarString(emailBO.getAmount());
            context.setVariable("amount", strAmount);

            try {
                log.info("Start to send payment success email...");
                String closeEmail = templateEngine.process("paymentSuccessEmail", context);
                emailSender.sendHtmlBodyEmail(email.getToEmailAddress(), "Your booking payment success", closeEmail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            log.info("get paymentEmailBO failed.");
        }
    }
}
