package org.thebreak.roombooking.app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingContact {

    @Schema(description = "contact name")
    @NotBlank
    private String name;

    @Schema(description = "contact email")
    @Email
    private String email;

    @Schema(description = "mobile number")
    @NotBlank
    private String mobile;

}
