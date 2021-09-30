package org.thebreak.roombooking.app.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.thebreak.roombooking.common.model.BookingCloseEmailBO;
import org.thebreak.roombooking.common.model.BookingNotificationEmailBO;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Value("${spring.kafka.topics.bookingSuccess}")
    String notificationTopic;

    @Value("${spring.kafka.topics.bookingReminder}")
    String reminderTopic;

    @Value("${spring.kafka.topics.bookingClose}")
    String bookingCloseTopic;

    public void sendBookingSuccess(BookingNotificationEmailBO email){
        ListenableFuture<SendResult<String, Object>> future = template.send(notificationTopic, email);
        log.info("**** sending notification email... ***");
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Notification email message failed - error {}", ex.getMessage());
            }
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Notification email message sent successful - email {}", email);
            }
        });
    }

    public void sendReminderEmail(BookingReminderEmailBO email){
        ListenableFuture<SendResult<String, Object>> future = template.send(reminderTopic, email);
        log.info("**** sending reminder email... ***");
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Reminder email message failed - error {}", ex.getMessage());
            }
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Reminder email message sent successful - email {}", email);
            }
        });
    }

    public void sendBookingCloseEmail(BookingCloseEmailBO email){
        ListenableFuture<SendResult<String, Object>> future = template.send(bookingCloseTopic, email);
        log.info("**** sending close email... ***");
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Close email message failed - error {}", ex.getMessage());
            }
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Close email message sent successful - email record {}", result.getProducerRecord());
            }
        });
    }

}
