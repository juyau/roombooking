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

    String dollarString = "49.90";
    int totalHours = 2;
    String roomTitle = "Big party room";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
    String formatedTime = "28 Aug 2021 8:15AM";
    String username = "Samuel";
    String html = "<p>Hi, <span style='font-weight: bold'>" + "username" + "</span></p>\n" +
            "    <p>Thank you for booking at TheBreak!</p>\n" +
            "    <p>Your booking for: </p>\n" +
            "    <div style='background-color: lightskyblue; border-left: 4px solid black; padding: 8px 16px'><p>Room: <span>" + "roomTitle" + "</span></p>\n" +
            "    <p>Total of <span style='font-weight: bold'> " + "totalHours" + " hours</span> starting at <span style='font-weight: bold'>" + "formatedTime" + "</span> with total amount of <span style='color: darkred'>$" + dollarString + "</span></p></div>\n" +
            "    <p>is successful, please process the payment within 30 minutes. Unpaid bookings will be cancelled automatically after 30 minutes.</p>" +
            "</br>" +
            "<p>TheBreak Room booking team</p>";

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
