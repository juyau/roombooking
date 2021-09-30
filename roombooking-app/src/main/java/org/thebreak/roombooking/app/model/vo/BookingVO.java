package org.thebreak.roombooking.app.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.thebreak.roombooking.app.model.BookingContact;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.Room;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingVO {

    private String id;
    private String userId;
    private BookingContact contact;
    private String remark;
    private Room room;
    private int totalHours;
    private int totalAmount;
    private List<BookingTimeRange> bookedTime;
    private int status;
    private int closeReason;
    private int paidAmount;
    private LocalDateTime createdAt;

}
