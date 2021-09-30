package org.thebreak.roombooking.app.service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Scheduled(cron = "0/20 * * * * ?")
    public void checkAndCloseBooking() {

        // bookedAt > 30 min and status == unpaid; -> status set to closed; close reason: booking timeout;
        List<Booking> bookingList = bookingRepository.findByStatus(BookingStatusEnum.UNPAID.getCode());

        if (bookingList.size() == 0) return;

        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : bookingList) {
            // close bookings older than 30 mins
            if (booking.getCreatedAt().isBefore(now.minusMinutes(3))) {
                bookingService.updateStatusById(booking.getId(), BookingStatusEnum.CLOSED.getCode(), 0);
                log.info("BookingUnpaidClosed: booking with id {} set status to closed", booking.getId());
            }
        }

    }
}
