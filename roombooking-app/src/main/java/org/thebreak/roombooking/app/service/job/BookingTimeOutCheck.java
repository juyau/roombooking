package org.thebreak.roombooking.app.service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.app.dao.BookingRepository;
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.enums.BookingStatusEnum;
import org.thebreak.roombooking.app.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BookingTimeOutCheck {

    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;

    // 1. create a variable, list of bookingId in payment process;
    // 2. kafka listener, add to list of all payment in process;

    // define a method "addPaymentInProcess" to add bookingId into a list here;
    // in make payment service, when send out the nonce, call the method to add the payment process here;
    // before close a booking, check if the bookingId in the process list, if yes, ignore and not close
    // if not, close booking;

    // a method listen to payment success, and remove from the list for paid bookings;

    @Scheduled(cron = "0/20 * * * * ?")
    public void checkAndCloseBooking() {

        // bookedAt > 30 min and status == unpaid; -> status set to closed; close reason: booking timeout;
        List<Booking> bookingList = bookingRepository.findByStatus(BookingStatusEnum.UNPAID.getCode());

        if (bookingList.size() == 0) return;

        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : bookingList) {
            // close bookings older than 30 mins
            if (booking.getCreatedAt().isBefore(now.minusMinutes(3))) {
                bookingService.updateStatusById(booking.getId(), BookingStatusEnum.CLOSED.getCode(), null);
                log.info("BookingUnpaidClosed: booking with id {} set status to closed", booking.getId());
            }
        }

    }
}
