package org.thebreak.roombooking.app.service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.app.dao.BookingRepository;
import org.thebreak.roombooking.app.kafka.KafkaProducerService;
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.enums.BookingStatusEnum;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.common.model.BookingCloseEmailBO;
import org.thebreak.roombooking.common.util.BookingUtils;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BookingCloseTiming {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Value("${thebreak.roombooking.payment-close-time-in-min}")
    private long closeTime;

    @Value("${thebreak.roombooking.close-booking-wait-time-in-second}")
    private long closeWaitTime;

    public void startCloseTiming(String bookingId) {

        log.info("start 15 minutes close booking timing.");
        System.out.println("closeWaitTime is " + closeWaitTime);
        System.out.println("closeTime is " + closeTime);

        CompletableFuture<BookingCloseEmailBO> future = new CompletableFuture<>();

        future.completeAsync(()-> {
            try {
                log.info("before repository findbyId {}", bookingId);
                Optional<Booking> byId = bookingRepository.findById(bookingId);
                if(byId.isPresent()){
                    log.info("found a booking by id {}", bookingId);
                    Booking savedBooking = byId.get();
                    if(savedBooking.getStatus() == BookingStatusEnum.UNPAID.getCode()){
                        bookingService.updateStatusById(bookingId, BookingStatusEnum.CLOSED.getCode(), 0);
                        BookingCloseEmailBO emailBO = new BookingCloseEmailBO();
                        emailBO.setRoomTitle(savedBooking.getRoom().getTitle());
                        String formattedStartTime = BookingUtils.emailStingDateTimeFormatter(savedBooking.getBookedTime().get(0).getStart());
                        emailBO.setStartTime(formattedStartTime);
                        emailBO.setCustomerName(savedBooking.getContact().getName());
                        emailBO.setToEmailAddress(savedBooking.getContact().getEmail());
                        System.out.println("close emailBO message to be sent out " + emailBO);
                        return emailBO;
                    }
                }
                return null;
            } catch (Throwable e){
                e.printStackTrace();
                return null;
            }
        }, CompletableFuture.delayedExecutor(closeTime, TimeUnit.MINUTES))
                .thenAccept(emailBO -> {
                    if (emailBO == null) {
                        // delay 5 seconds to send email
                        log.info("cannot find the booking， booking already paid or closed.");
                    } else {
                        log.info("call delay 5s method");
                        delayTenSecondAndSendMessage(emailBO);
                    }
                });
    }

    public void delayTenSecondAndSendMessage(BookingCloseEmailBO emailBO) {

        CompletableFuture<Object> future = new CompletableFuture<>();
        future.completeAsync(()-> {
            // must use try catch, or it will not work
            try {
                log.info("sending close email message to kafka with bookingId {}", emailBO);
                kafkaProducerService.sendBookingCloseEmail(emailBO);
                return null;
            } catch (Throwable e){
                e.printStackTrace();
                return null;
            }
        }, CompletableFuture.delayedExecutor(closeWaitTime, TimeUnit.SECONDS));
    }
}
