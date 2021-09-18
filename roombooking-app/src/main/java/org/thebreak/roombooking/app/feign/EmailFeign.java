package org.thebreak.roombooking.app.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thebreak.roombooking.common.model.BookingNotificationEmailBO;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;
import org.thebreak.roombooking.common.model.EmailBO;

@FeignClient(value = "roombooking-email/api/v1/email/")
public interface EmailFeign {

    @PostMapping("sendBookingNotification")
    void sendBookingNotification(@RequestBody BookingNotificationEmailBO email);

    @PostMapping("sendReminderEmail")
    void sendReminderEmail(@RequestBody BookingReminderEmailBO email);

    @PostMapping("sendText")
    void sendTextEmail(@RequestBody EmailBO email);


    @PostMapping("sendHtml")
    void sendHtmlEmail(@RequestBody EmailBO email);
}
