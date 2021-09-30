package org.thebreak.roombooking.app.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.app.dao.BookingRepository;
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.enums.BookingStatusEnum;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;
import org.thebreak.roombooking.common.util.BookingUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingEmailReminderOld {

    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;
//    @Autowired
//    EmailFeign emailFeign;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Scheduled(cron = "30 0/2 * * * ?")
    public void sendReminderEmail() {

        // reminder before the day of booking
        List<Booking> bookingList = bookingRepository.findByStatus(BookingStatusEnum.UNPAID.getCode());

        if (bookingList.size() == 0) return;

        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : bookingList) {
            for(BookingTimeRange bookedTime: booking.getBookedTime()){
                if(bookedTime.getStart().getDayOfYear() - now.getDayOfYear() == 1){
                    String formattedTime = BookingUtils.emailStingDateTimeFormatter(bookedTime.getStart());
                    BookingReminderEmailBO emailBO = new BookingReminderEmailBO();
                    emailBO.setToEmailAddress(booking.getContact().getEmail());
                    emailBO.setCustomerName(booking.getContact().getName());
                    emailBO.setRoomTitle(booking.getRoom().getTitle());
                    emailBO.setStartTime(formattedTime);

                    kafkaProducerService.sendReminderEmail(emailBO);
                    System.out.println("bookedTime " + bookedTime.getStart().getDayOfYear() + ":" + bookedTime.getStart());
                }
            }
        }
    }
}
