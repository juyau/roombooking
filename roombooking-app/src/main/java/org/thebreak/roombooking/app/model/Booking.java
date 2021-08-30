package org.thebreak.roombooking.app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;


@Document(value = "booking")
@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Booking extends BaseEntity {

    @Field("user")
    @Schema(example = "userIdFromKeyCloak")
    private String userId;

    @Field("contact")
    @Schema(description = "contact person info")
    private BookingContact contact;

    @Field("remark")
    @Schema(example = "We need 2 extra white boards, thanks")
    private String remark;

    @Field("room")
    @Schema(example = "booked room detail info")
    private Room room;

    @Field("total_hours")
    @Schema(example = "2 (in hours)")
    private int totalHours;

    @Field("total_amount")
    @Schema(example = "1900")
    private int totalAmount;

    @Field("booked_time")
    @Schema(example = "example")
    private List<BookingTimeRange> bookedTime;

    @Field("status")
    @Schema(example = "use enum for 1 Unpaid, 2 Paid, 3 Closed")
    private int status = 1;

    @Field("close_reason")
    @Schema(example = "use enum for 1 payment timeout, 2 overdue, 3 cancelled")
    private int closeReason;

    @Field("paid_amount")
    @Schema(example = "19999")
    private int paidAmount;

    @Field("reviewed")
    private LocalDateTime reviewed;

    @Field("deleted")
    private LocalDateTime deleted;
}
