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
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;
import org.thebreak.roombooking.common.util.BookingUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BookingReminderTiming {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Value("${thebreak.roombooking.pre-day-reminder-sendout-clock}")
    private int sendOutClock;

    public void startReminderTiming(String bookingId) {
        log.info("start pre-day reminder timing....");

        LocalDateTime now = LocalDateTime.now();

        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if(bookingOptional.isPresent()){
            Booking savedBooking = bookingOptional.get();
            // used to save different booking days for the booking, no repeat items
            Set<Integer> bookingDays = new HashSet<>();

            savedBooking.getBookedTime().forEach( bookingTimeRange -> {
                log.info("Each time range: {}", bookingTimeRange);
                int bookingDay = bookingTimeRange.getStart().getDayOfYear();
                log.info("bookingDay is " + bookingDay);
                // only bookings for more than 2 days in future need to send email;
                if(bookingDay - now.getDayOfYear() > 1 ){
                    log.info("bookingDay - now.getDayOfYear() >= 1");
                    // if it's a new day, add in bookingDays and send reminder email, if day already in bookingDays, not send;
                    if(!bookingDays.contains(bookingDay)){
                        bookingDays.add(bookingDay);
                        log.info("bookingDays is " + bookingDays);
                        // must new emailBO for each email, otherwise old info will be replaced;
                        BookingReminderEmailBO emailBO = new BookingReminderEmailBO();
                        emailBO.setCustomerName(savedBooking.getContact().getName());
                        emailBO.setToEmailAddress(savedBooking.getContact().getEmail());
                        emailBO.setRoomTitle(savedBooking.getRoom().getTitle());
                        String formattedStartTime = BookingUtils.emailStingDateTimeFormatter(bookingTimeRange.getStart());
                        emailBO.setStartTime(formattedStartTime);

                        int delayHours = (bookingDay - now.getDayOfYear() - 1) * 24 - now.getHour() + sendOutClock;
//                        int delayHours = bookingTimeRange.getStart().getHour() + 1 - now.getHour();

                        startReminderTimingForEachDay(emailBO,delayHours,bookingId);
                    }
                }
            });
        }
    }

    public void startReminderTimingForEachDay(BookingReminderEmailBO emailBO, int delayHours, String bookingId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        log.info("inside startReminderTimingForEachDay - delayHours is {} minutes", delayHours);
        future.completeAsync(()-> {
            try {
                // insure the booking is not closed by the time sending the reminder email
                Optional<Booking> booking = bookingRepository.findById(bookingId);
                if(booking.isPresent()){
                    if(booking.get().getStatus() == BookingStatusEnum.PAID.getCode()){
                        kafkaProducerService.sendReminderEmail(emailBO);
                        System.out.println("Reminder message sent out " + emailBO);
                    }
                }
            } catch (Throwable e){
                e.printStackTrace();
            }
            return null;
        }, CompletableFuture.delayedExecutor(delayHours, TimeUnit.MINUTES));
    }
}
