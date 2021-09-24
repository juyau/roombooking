package org.thebreak.roombooking.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.model.BookingNotificationEmailBO;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Value("${spring.kafka.topics.bookingEmailNotification}")
    String notificationTopic;

    @Value("${spring.kafka.topics.bookingEmailReminder}")
    String reminderTopic;

    public void sendBookingNotification(BookingNotificationEmailBO email){
        template.send(notificationTopic, email);
        log.info("sending notification email: {}" , email);
    }

    public void sendReminderEmail(BookingReminderEmailBO email){
        template.send(reminderTopic, email);
        log.info("reminder email sent.");
    }

}
