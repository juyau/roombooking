package org.thebreak.roombooking.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class EmailBO {
    @Schema(example = "example@gmail.com")
    private String to;

    @Schema(description = "email subject")
    private String subject;

    @Schema(description = "can be plain text or html string")
    private String body;
}
