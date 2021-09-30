package org.thebreak.roombooking.app.service;

import org.springframework.data.domain.Page;
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.bo.BookingBO;
import org.thebreak.roombooking.app.model.vo.BookingPreviewVO;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingPreviewVO add(BookingBO bookingBO);

    Booking findById(String id);

    Page<Booking> findPage(Integer page, Integer size);

    Page<Booking> findPageByUser(String userId, Integer page);

    Page<Booking> findPageActiveBookings(Integer page, Integer size);

    void deleteById(String id);

    Booking updateById(Booking booking);

    // if param city can is null, will get city value from db query
    List<BookingTimeRange> findFutureBookedTimesByRoom(String roomId, String city);

    List<BookingTimeRange> findBookedTimesByRoomInRange(String roomId, LocalDateTime start, LocalDateTime end);

    Booking updateStatusById(String id, int status, int paidAmount);
}
