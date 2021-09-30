package org.thebreak.roombooking.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.thebreak.roombooking.common.model.PaymentBO;

@Service
@Slf4j
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Value("${spring.kafka.topics.paymentSuccess}")
    String paymentTopic;

    public void sendPaymentSuccess(PaymentBO paymentBO){
        ListenableFuture<SendResult<String, Object>> future = template.send(paymentTopic, paymentBO);
        log.info("**** sending payment success message ***");
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Payment message failed - error {}", ex.getMessage());
            }
            @Override
            public void onSuccess(SendResult<String, Object> result) {

                log.info("payment message sent successfully." + result.getProducerRecord().toString());
            }
        });
    }

}
