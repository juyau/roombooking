package org.thebreak.roombooking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class RoomVO {

    private String id;
    private String title;
    private String address;
    private String roomNumber;
    private String type;
    private int availableType;
    private List<LocalDateTime> reservedDates;
    private String city;
    private String description;
    private int floor;
    private int size;
    private int maxPeople;
    private int price;
    private int discount;
    private int rating;
    private int commentCount;
    private List<String> images;
    private List<String> facilities;

}
