package org.thebreak.roombooking.app.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.thebreak.roombooking.app.model.BookingContact;
import org.thebreak.roombooking.app.model.BookingTimeRange;

import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingBO {
    @Schema(example = "6118bac4b28c5e2f4d882c17")
    private String roomId;

    @Schema(description = "contact person info, frontend can allow user to checkbox - same as user register info")
    private BookingContact contact;

    @Schema(description = "user remarks eg special requirements to the place owner")
    private String remark;

    @Schema(example = "{\"start\":\"2021-08-22T22:00\",\n" +
            "        \"end\":\"2021-08-22T23:00\"}")
    private List<BookingTimeRange> bookingTime;
}
