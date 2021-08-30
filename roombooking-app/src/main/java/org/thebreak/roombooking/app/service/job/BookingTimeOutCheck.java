package org.thebreak.roombooking.app.service.job;

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
public class BookingTimeOutCheck {

    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;

    @Scheduled(cron = "0/10 * * * * ?")
    public void checkAndCloseBooking() {
//        System.out.println("checkAndCloseBooking job");

        // bookedAt > 30 min and status == unpaid; -> status set to closed; close reason: booking timeout;
        List<Booking> bookingList = bookingRepository.findByStatus(BookingStatusEnum.UNPAID.getCode());

        if (bookingList.size() == 0) return;

        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : bookingList) {
            System.out.println(booking.getCreatedAt());
            System.out.println(booking.getStatus());
            // close bookings older than 2days
            if (booking.getCreatedAt().isBefore(now.minusMinutes(1))) {
                bookingService.updateStatusById(booking.getId(), BookingStatusEnum.CLOSED.getCode(), 1L);
                System.out.println("checkAndCloseBooking: set status to closed");
            }
        }
    }
}
