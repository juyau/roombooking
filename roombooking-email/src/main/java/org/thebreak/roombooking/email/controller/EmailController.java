package org.thebreak.roombooking.email.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.model.BookingNotificationEmailBO;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;
import org.thebreak.roombooking.common.model.EmailBO;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.email.service.EmailService;

import javax.mail.MessagingException;

@RestController
@CrossOrigin
@OpenAPIDefinition(info = @Info(title = "Email Controller", description = "Controller for email service"))
@RequestMapping(value = "api/v1/email")
public class EmailController {
    // test text
    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/sendBookingNotification")
    public ResponseResult<?> sendBookingNotification(@RequestBody BookingNotificationEmailBO email) {
        emailService.sendBookingNotification(email);
        return ResponseResult.success();
    }

    @PostMapping(value = "/sendReminderEmail")
    public ResponseResult<?> sendReminderEmail(@RequestBody BookingReminderEmailBO email) {
        emailService.sendBookingReminder(email);
        return ResponseResult.success();
    }

    @PostMapping(value = "/sendText")
    public ResponseResult<?> sendTextEmail(@RequestBody EmailBO email) {
        emailService.sendSimpleTextEmail(email);
        return ResponseResult.success();
    }


    @PostMapping(value = "/sendHtml")
    public ResponseResult<?> sendHtmlEmail(@RequestBody EmailBO email) throws MessagingException {
        emailService.sendHtmlEmail(email);
        return ResponseResult.success();
    }

}
