package org.thebreak.roombooking.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.Room;
import org.thebreak.roombooking.app.model.enums.RoomAvailableTypeEnum;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.util.BookingUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingTimeValidator {

    @Autowired
    private BookingService bookingService;

    public int validateBookingTimeAndGetTotalHours(List<BookingTimeRange> bookingTimeList, Room room){

        int totalBookedHours = 0;

        for (BookingTimeRange bookingTimeRange : bookingTimeList) {

            LocalDateTime start = bookingTimeRange.getStart();
            LocalDateTime end = bookingTimeRange.getEnd();

            // 0. check end time must later than start time
            if (!end.isAfter(start)) {
                CustomException.cast(CommonCode.BOOKING_END_BEFORE_START);
                System.out.println(end);
            }

            // 1. check start or end time must later than current time
            if (!BookingUtils.checkBookingTimeAfterNow(start, room.getCity())) {
                CustomException.cast(CommonCode.BOOKING_TIME_EARLIER_THAN_NOW);
            }
            if (!BookingUtils.checkBookingTimeAfterNow(end, room.getCity())) {
                CustomException.cast(CommonCode.BOOKING_TIME_EARLIER_THAN_NOW);
            }

            // 2. check start or end time matches available type (weekend or weekday)
            boolean isWeekendType = room.getAvailableType() == RoomAvailableTypeEnum.WEEKEND.getCode();
            if (isWeekendType) {
                // cast exception if type is weekend but booking time is weekday
                if (!BookingUtils.checkIsWeekend(start) || !BookingUtils.checkIsWeekend(end)) {
                    CustomException.cast(CommonCode.BOOKING_WEEKEND_ONLY);
                }
            } else {
                // cast exception if type is weekday but booking time is weekend
                if (BookingUtils.checkIsWeekend(start) || BookingUtils.checkIsWeekend(end)) {
                    CustomException.cast(CommonCode.BOOKING_WEEKDAY_ONLY);
                }
            }

            // 2.1 check against reserved dates in the room
            List<LocalDate> reservedDates = room.getReservedDates();
            if(reservedDates.contains(start.toLocalDate())){
                CustomException.cast(CommonCode.BOOKING_DATE_RESERVED);
            };

            // 2.1 check against hour range
            if(room.getHourRange() == null){
                int startHour = 8;
                int endHour = 18;
                System.out.println(start.getHour());
                System.out.println(end.getHour());
                if(start.getHour() < startHour || end.getHour() > endHour){
                    CustomException.cast(CommonCode.BOOKING_OUT_OF_RANGE);
                }
            } else {
                if(start.isBefore(room.getHourRange().getStart()) || end.isAfter(room.getHourRange().getEnd())){
                    CustomException.cast(CommonCode.BOOKING_OUT_OF_RANGE);
                }
            }

            // 3. check start or end time must be in quarter
            if (!BookingUtils.checkTimeIsQuarter(start) || !BookingUtils.checkTimeIsQuarter(end)) {
                CustomException.cast(CommonCode.BOOKING_TIME_QUARTER_ONLY);
            }

            // 4. check booking time must be hourly and at least one hour
            if (!BookingUtils.checkDurationInHour(start, end)) {
                CustomException.cast(CommonCode.BOOKING_TIME_HOURLY_ONLY);
            }

            // 5. check booking time has not been booked by other users.
            List<BookingTimeRange> futureBookedTimesByRoom = bookingService.findFutureBookedTimesByRoom(room.getId(), room.getCity());
            for (BookingTimeRange timeRange : futureBookedTimesByRoom) {
                // check start time is not between the booked time
                if (start.isAfter(timeRange.getStart()) && start.isBefore(timeRange.getEnd())) {
                    CustomException.cast(CommonCode.BOOKING_TIME_ALREADY_TAKEN);
                }
                // check end time is not between the booked time
                if (end.isAfter(timeRange.getStart()) && end.isBefore(timeRange.getEnd())) {
                    CustomException.cast(CommonCode.BOOKING_TIME_ALREADY_TAKEN);
                }
                // check the booking time range is not covering any booked time
                if (start.isBefore(timeRange.getStart()) && end.isAfter(timeRange.getEnd())) {
                    CustomException.cast(CommonCode.BOOKING_TIME_ALREADY_TAKEN);
                }
                // check the booking time range is not inside any other booked time
                if (start.isAfter(timeRange.getStart()) && end.isBefore(timeRange.getEnd())) {
                    CustomException.cast(CommonCode.BOOKING_TIME_ALREADY_TAKEN);
                }
                // check the booking time is exactly the same as any other booked time
                if (start.isEqual(timeRange.getStart()) || end.isEqual(timeRange.getEnd())) {
                    CustomException.cast(CommonCode.BOOKING_TIME_ALREADY_TAKEN);
                }
            }

            // get hourly duration of each booking
            int bookingHours = BookingUtils.getBookingHours(start, end);
            totalBookedHours += bookingHours;
        }
        return totalBookedHours;
    }
}
