package org.thebreak.roombooking.app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Document(value = "room")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Room extends BaseEntity {

    @Field("title")
    @Size(min = 3, max = 300, message = "title must be between 3 to 300 characters.")
    @Schema(example = "huge party room with music and lighting for big events")
    private String title;

    @Field("address")
    @NotBlank
    @Schema(example = "77 Kings St, Chatswood, NSW")
    private String address;

    @Field("city")
    @NotBlank
    @Schema(example = "Sydney")
    private String city;

    @Field("room_number")
    @NotBlank
    @Schema(example = "101")
    private String roomNumber;

    @Field("type")
    @NotBlank
    @Schema(example = "Party Room")
    private String type;

    @Field("available_type")
    @Schema(example = "weekend")
    private int availableType = 1;

    @Field("start_hour")
    @Schema(description = "the bookable start hour of the day.")
    private int startHour = 8;

    @Field("end_hour")
    @Schema(description = "the bookable end hour of the day.")
    private int endHour = 17;

    @Field("reserved_dates")
    @Schema(example = "[\"2002-01-06\",\"2002-01-07\"]", description = "for the special dates not available for booking, for example due to covid restriction")
    private List<LocalDate> reservedDates;

    @Field("description")
    @NotBlank
    @Schema(example = "This is a huge party room with a lot of facilities, included ....")
    private String description;

    @Field("floor")
    @Schema(example = "1")
    private int floor = 0;

    @Field("size")
    @Schema(example = "200 Sq")
    private int size;

    @Field("max_people")
    @Schema(example = "50")
    private int maxPeople;

    @Field("price")
    @Schema(example = "9999", description = "unit is in cents, need to display 99.99 in frontend")
    private int price = 0;

    @Field("discount")
    @Schema(example = "20")
    private int discount;

    @Field("rating")
    @Schema(example = "4.3", description = "this field is calculated and updated automatically in backend")
    private int rating;

    @Field("commentCount")
    @Schema(example = "17", description = "this field is calculated updated automatically in backend")
    private int commentCount;

    @Field("images")
    private List<String> images;

    @Field("facilities")
    private List<String> facilities;

}
