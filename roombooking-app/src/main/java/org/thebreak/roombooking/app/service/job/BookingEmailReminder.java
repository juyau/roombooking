package org.thebreak.roombooking.app.service.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.app.dao.BookingRepository;
import org.thebreak.roombooking.app.feign.EmailFeign;
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.enums.BookingStatusEnum;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingEmailReminder {

    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    EmailFeign emailFeign;

    @Scheduled(cron = "15 1/1 * * * ?")
    public void checkAndCloseBooking() {

        // bookedAt > 30 min and status == unpaid; -> status set to closed; close reason: booking timeout;
        List<Booking> bookingList = bookingRepository.findByStatus(BookingStatusEnum.UNPAID.getCode());


        if (bookingList.size() == 0) return;

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.getDayOfYear());
        System.out.println(now);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");

        for (Booking booking : bookingList) {
            for(BookingTimeRange bookedTime: booking.getBookedTime()){
                if(bookedTime.getStart().getDayOfYear() - now.getDayOfYear() == 1){
                    String formatedTime = bookedTime.getStart().format(dateTimeFormatter);
                    BookingReminderEmailBO emailBO = new BookingReminderEmailBO(
                            booking.getContact().getEmail(),
                            booking.getContact().getName(),
                            booking.getRoom().getTitle(),booking.getTotalHours(),
                            formatedTime);
                    emailFeign.sendReminderEmail(emailBO);
                    System.out.println("bookedTime " + bookedTime.getStart().getDayOfYear() + ":" + bookedTime.getStart());
                }
            }
        }
    }
}
