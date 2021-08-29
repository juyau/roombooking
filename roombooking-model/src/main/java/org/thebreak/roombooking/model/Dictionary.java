package org.thebreak.roombooking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Document(value = "dictionary")
@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Dictionary extends BaseEntity {

    @Field("name")
    @Schema(example = "city")
    private String name;

    @Field("values")
    @Schema(example = "[\n" +
            "                    \"sydney\",\n" +
            "                    \"melbourne\",\n" +
            "                    \"brisbane\",\n" +
            "                ]")
    private List<String> values;

}
