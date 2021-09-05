package org.thebreak.roombooking.payment.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TotalAndCountVO {

    @Schema(description = "int, 1 - room booking, 2 - event booking")
    private Integer type;
    private int count;
    private int totalAmount;


}
