package org.thebreak.roombooking.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Document(value = "booking_time_range")
@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingTimeRange {

    @Field("start")
    @Schema(example = "2021-08-15T15:17:12.611", description = "the time is localDateTime format, exactly what user has selected.")
    private LocalDateTime start;

    @Field("end")
    @Schema(example = "2021-08-15T15:17:12.611", description = "the time is localDateTime format, exactly what user has selected.")
    private LocalDateTime end;
}
