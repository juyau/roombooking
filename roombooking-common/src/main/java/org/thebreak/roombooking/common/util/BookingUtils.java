package org.thebreak.roombooking.common.util;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class BookingUtils {

    public static String getStringCapitalized(String str) {
        return str.trim().substring(0, 1).toUpperCase() + str.trim().substring(1).toLowerCase();
    }

    public static LocalDateTime getNowAtZonedCity(String city) {
        String cityCapitalized = city.trim().substring(0, 1).toUpperCase() + city.trim().substring(1).toLowerCase();
        String targetCityZone = "Australia/" + cityCapitalized;
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(targetCityZone));
        return zonedDateTime.toLocalDateTime();
    }

    public static Boolean checkBookingTimeAfterNow(LocalDateTime time, String city) {
        String cityCapitalized = city.trim().substring(0, 1).toUpperCase() + city.trim().substring(1).toLowerCase();
        String targetCityZone = "Australia/" + cityCapitalized;
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(targetCityZone));
        return time.isAfter(zonedDateTime.toLocalDateTime());
    }

    public static Boolean checkIsWeekend(LocalDateTime time) {
        DayOfWeek day = time.getDayOfWeek();
        return day.name().equals("SATURDAY") || day.name().equals("SUNDAY");
    }


    public static Boolean checkTimeIsQuarter(LocalDateTime time) {
        return time.getMinute() == 0 || time.getMinute() == 15 || time.getMinute() == 30 || time.getMinute() == 45;
    }

    public static Boolean checkDurationInHour(LocalDateTime start, LocalDateTime end) {
        if (start.getMinute() == end.getMinute()) {
            return ChronoUnit.HOURS.between(start, end) >= 1;
        } else {
            return false;
        }
    }

    public static int getBookingHours(LocalDateTime start, LocalDateTime end) {
        long hours = ChronoUnit.HOURS.between(start, end);
        return (int) hours;
    }


}
