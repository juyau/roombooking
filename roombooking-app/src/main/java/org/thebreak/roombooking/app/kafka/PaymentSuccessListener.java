package org.thebreak.roombooking.app.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.app.model.enums.BookingStatusEnum;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.app.service.job.BookingReminderTiming;
import org.thebreak.roombooking.common.model.PaymentBO;

@Service
@Slf4j
public class PaymentSuccessListener {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingReminderTiming bookingReminderTiming;

    @KafkaListener(topics = "${spring.kafka.topics.paymentSuccess}",
            groupId = "updateBooking",
            containerFactory = "paymentListenerContainerFactory")
    public void updateBookingPaymentStatus(PaymentBO paymentBO){
        log.info("Update booking status - Received payment information: " + paymentBO);

        try {
            bookingService.updateStatusById(paymentBO.getBookingId(), BookingStatusEnum.PAID.getCode(), paymentBO.getAmount());
            log.info("payment status updated for bookingId {}", paymentBO.getBookingId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.paymentSuccess}",
            groupId = "setReminder",
            containerFactory = "paymentListenerContainerFactory")
    public void setPreDayReminderTiming(PaymentBO paymentBO){
        log.info("Set pre-day reminder timing - Received payment information: " + paymentBO);
        try {
            bookingReminderTiming.startReminderTiming(paymentBO.getBookingId());
            log.info("Reminder started for bookingId {}", paymentBO.getBookingId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
