package org.thebreak.roombooking.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.payment.model.Payment;

@Service
@Slf4j
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, Payment> template;

    @Value("${spring.kafka.topics.paymentSuccess}")
    String notificationTopic;

    public void sendPaymentNotification(Payment payment){
        template.send(notificationTopic, payment);
        log.info("sending notification email: {}" , payment);
    }

}
