package org.thebreak.roombooking.model.enums;

public enum BookingStatusEnum {

    UNPAID(1, "Unpaid"),
    PAID(2, "Paid"),
    CLOSED(3, "Closed");

    private final int code;
    private final String description;

    BookingStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
