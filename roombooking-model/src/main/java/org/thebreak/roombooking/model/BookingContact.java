package org.thebreak.roombooking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingContact {

    @Schema(description = "contact name")
    private String name;

    @Schema(description = "contact email")
    @Email
    private String email;

    @Schema(description = "mobile number")
    private String mobile;

}
