package org.thebreak.roombooking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(value = "payment")
@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Field("bookingId")
    @Schema(example = "34ere44df67876d8fd")
    private String bookingId;

    @Field("amount")
    @Schema(example = "3950")
    private int amount;

    @Field("transId")
    @Schema(example = "h8jkav57")
    private String transId;

}
