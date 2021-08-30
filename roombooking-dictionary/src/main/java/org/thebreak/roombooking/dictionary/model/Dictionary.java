package org.thebreak.roombooking.dictionary.model;


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
    private String name;

    @Field("values")
    private List<String> values;

}
